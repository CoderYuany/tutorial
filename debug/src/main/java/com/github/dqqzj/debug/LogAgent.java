package com.github.dqqzj.debug;

import com.alibaba.fastjson.JSON;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qinzhongjian
 * @date created in 2019/12/21 16:29
 * @description Invoke as follows:
 * <pre>java -javaagent:target/debug-agent-0.1-SNAPSHOT.jar={opts} application.jar<pre/>
 *
 * Example:
 *  java -javaagent:target/debug-agent-0.1-SNAPSHOT.jar=debug=true;methods=java.net.InetAddress::getByName(java.lang.String)|java.net.InetAddress::getByName(java.lang.String, java.net.InetAddress) -jar application.jar
 *
 *
 * Agent opts (as key1=value1;key2=value2)
 *
 * - debug=true
 *         enable debug logging to standard error
 *
 * - methods=methodDefinitions
 *         definitions of methods to be intercepted separated by "|"
 *
 *         Example: "java.net.InetAddress::getByName(java.lang.String, java.net.InetAddress)|java.net.InetAddress::getByName(java.lang.String)"
 *
 *         Note: If not specified networking methods for DNS lookup and opening of TCP/UDP connections will be intercepted.
 * @since JDK 1.8.0_212-b10
 */
public class LogAgent {
    private static final Logger logger = LoggerFactory.getLogger(LogAgent.class);
    private static final Pattern PATTERN = Pattern.compile("([^:]+)::([^(]+)\\(([^)]+)\\)");
    private static final String TIME_STAMP_STRING_CODE = "java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss.SSS\"))";
    private static final String THREAD_NAME_STRING_CODE = "java.lang.Thread.currentThread().getName()";
    private static final String LOG_PREFIX_STRING_CODE = TIME_STAMP_STRING_CODE + " + \" [\" + " + THREAD_NAME_STRING_CODE + " + \"] Agent \"";

    private static final String[] DEFAULT_INSTRUMENT_METHODS = new String[]{
            "java.net.InetAddress::getByName(java.lang.String)",
            "java.net.InetAddress::getByName(java.lang.String, java.net.InetAddress)",
            "java.net.Socket::connect(java.net.SocketAddress, int)",
            "java.net.Socket::bind(java.net.SocketAddress)",
            "java.net.ServerSocket::bind(java.net.SocketAddress, int)",
            "java.net.DatagramSocket::bind(java.net.SocketAddress)",
            "java.net.DatagramSocket::connect(java.net.SocketAddress)",
            "java.net.DatagramSocket::connect(java.net.InetAddress, int)"
    };

    public static void premain(String agentArgs, Instrumentation inst) {
        // Parse args
        Map<String, String> agentArgsMap = parseAgentArgs(agentArgs);

        // Get methods argument
        Stream<String> methods = (agentArgsMap.get("methods") == null)
                ? Stream.of(DEFAULT_INSTRUMENT_METHODS)
                : Stream.of(agentArgsMap.get("methods").split("\\|"));

        // Parse method arguments into instruction map
        Map<String, List<MethodDesc>> instructionMap = methods
                .map(String::trim)
                .map(LogAgent::parseMethodDesc)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.groupingBy(MethodDesc::getClassName));

        // Register transformer
        inst.addTransformer(new Transformer(instructionMap));
    }

    private static Map<String, String> parseAgentArgs(String agentArgs) {
        Map<String, String> result = new LinkedHashMap<>();

        if (agentArgs == null) {
            return result;
        }

        for (String arg : agentArgs.split(";")) {
            String[] argParts = arg.split("=");
            if (argParts.length != 2) {
                logger.error("Ignoring agentArg:{}", arg);
                continue;
            }
            result.put(argParts[0].trim(), argParts[1].trim());
        }

        return result;
    }

    private static Optional<MethodDesc> parseMethodDesc(String desc) {
        Matcher matcher = PATTERN.matcher(desc);
        if (matcher.matches()) {
            String className = matcher.group(1);
            String methodName = matcher.group(2);
            String argString = matcher.group(3);
            String[] methodArgs = Stream.of(argString.split(","))
                    .map(String::trim)
                    .toArray(String[]::new);
            return Optional.of(new MethodDesc(className, methodName, methodArgs));
        }
        return Optional.empty();
    }

    private static class Transformer implements ClassFileTransformer {
        private final Map<String, List<MethodDesc>> instructionMap;

        private Transformer(Map<String, List<MethodDesc>> instrumentMethods) {
            this.instructionMap = instrumentMethods;
        }

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            className = className.replace("/", ".");

            List<MethodDesc> methods = instructionMap.get(className);
            if (methods != null) {
                return instrumentClass(className, methods);
            }
            return null;
        }

        private byte[] instrumentClass(String className, Collection<MethodDesc> collection) {
            try {
                ClassPool pool = ClassPool.getDefault();
                CtClass ctClass = pool.get(className);
                for (MethodDesc methodDesc : collection) {
                    instrumentMethod(pool, ctClass, methodDesc);
                }
                byte[] byteCode = ctClass.toBytecode();
                ctClass.detach();
                return byteCode;
            } catch (Throwable e) {
                logger.error("instrumentClass invoked class error. className:{} methodDesc:{}", className, JSON.toJSONString(collection), e);
                return null;
            }
        }

        private void instrumentMethod(ClassPool pool, CtClass ctClass, MethodDesc methodDesc) {
            try {
                CtClass[] methodArgs = Stream.of(methodDesc.getMethodArgs())
                        .map(type -> getCtClass(pool, type))
                        .toArray(CtClass[]::new);
                CtMethod ctMethod = ctClass.getDeclaredMethod(methodDesc.getMethodName(), methodArgs);
                logReturn(ctMethod);
            } catch (Throwable e) {
                logger.error("instrumentMethod invoked method error.pool:{} ctClass:{} methodDesc:{}", JSON.toJSONString(pool), JSON.toJSONString(ctClass), JSON.toJSONString(methodDesc));
            }
        }

        private CtClass getCtClass(ClassPool pool, String className) {
            try {
                if (Objects.equals("int", className)) {
                    return CtClass.intType;
                }
                return pool.getCtClass(className);
            } catch (Throwable ex) {
                throw new RuntimeException("Class not found " + className, ex);
            }
        }

        private void logReturn(CtMethod m) {
            try {
                /**
                 * 方法调用成功的日志打印
                 */
                String successCode = getCodeLogMethodArgsAndResult(m, "$_");
                m.insertAfter(successCode);
                /**
                 * 方法调用过程中产生了异常的日志打印
                 */
                String errorCode = "{ " + getCodeLogMethodArgsAndResult(m, "$ex") + "; throw $ex; }";
                CtClass exceptionClass = ClassPool.getDefault().getCtClass("java.lang.Exception");
                m.addCatch(errorCode, exceptionClass, "$ex");
            } catch (Throwable e) {
                logger.error("logReturn to handler method error. method:{}", JSON.toJSONString(m), e);
            }
        }

        private String getCodeLogMethodArgsAndResult(CtMethod m, String result) throws NotFoundException {
            m.getDeclaringClass();
            StringBuilder code = new StringBuilder();

            // Prefix
            code.append("System.err.println(")
                    .append(LOG_PREFIX_STRING_CODE)
                    .append(" + ");

            // ClassName::methodName
            code.append("\"")
                    .append(m.getDeclaringClass().getSimpleName())
                    .append("::")
                    .append(m.getName())
                    .append("(\"");

            // Arguments
            code.append(" + ");
            int argCount = m.getParameterTypes().length;
            for (int i = 1; i <= argCount; i++) {
                if (i != 1) {
                    code.append(" + \", \" + ");
                }
                code.append("$").append(i);
            }

            // Right Bracket
            code.append(" + \")\"");

            // Result
            code.append(" + \" ==> \"");
            code.append(" + ");
            code.append(result);
            code.append(");");

            return code.toString();
        }
    }

    private static class MethodDesc {
        private String className;
        private String methodName;
        private String[] methodArgs;

        private MethodDesc(String className, String methodName, String... methodArgs) {
            this.className = className;
            this.methodName = methodName;
            this.methodArgs = methodArgs;
        }

        public String getClassName() {
            return className;
        }

        public String getMethodName() {
            return methodName;
        }

        public String[] getMethodArgs() {
            return methodArgs;
        }

        @Override
        public String toString() {
            return className + "::" + methodName
                    + Arrays.toString(methodArgs)
                    .replace("[", "(")
                    .replace("]", ")");
        }
    }

}
