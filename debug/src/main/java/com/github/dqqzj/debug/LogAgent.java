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
 * <pre>java -javaagent:target/agent-0.1-SNAPSHOT.jar={opts} application.jar<pre/>
 *
 * Example:
 *  java -javaagent:target/agent-0.1-SNAPSHOT.jar=debug=true;methods=java.net.InetAddress::getByName(java.lang.String)|java.net.InetAddress::getByName(java.lang.String, java.net.InetAddress) -jar application.jar
 *TODO 后期考虑支持class级别和扫描包级别等其他手段，以做到更加灵活配置
 *
 * Agent opts (as key1=value1;key2=value2)
 *
 * - logForParams=true
 *         enable logging to standard logForParams
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
    private static final String SPLIT_EQUALS = "=";
    private static final String SPLIT_SEMICOLON = ";";
    private static final String LOG_FOR_PARAMS = "logForParams";
    private static final String LOG_FOR_RESULT = "logForResult";
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
        //Get logForParams argument
        boolean logForParams = agentArgsMap.get(LOG_FOR_PARAMS) == null ? false : Boolean.valueOf(agentArgsMap.get(LOG_FOR_PARAMS));
        //Get logForResult argument
        boolean logForResult = agentArgsMap.get(LOG_FOR_RESULT) == null ? false : Boolean.valueOf(agentArgsMap.get(LOG_FOR_RESULT));
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
        inst.addTransformer(new Transformer(logForParams,logForResult,instructionMap));
    }

    private static Map<String, String> parseAgentArgs(String agentArgs) {
        Map<String, String> result = new LinkedHashMap<>();
        if (agentArgs == null) {
            return result;
        }
        for (String arg : agentArgs.split(SPLIT_SEMICOLON)) {
            String[] argParts = arg.split(SPLIT_EQUALS);
            System.out.println(argParts.length);
            if (argParts.length == 2) {
                result.put(argParts[0].trim(), argParts[1].trim());
            } else {
                logger.error("parseAgentArgs ignoring agentArg:{}", arg);
            }
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
        private boolean logForParams = false;
        private boolean logForResult = false;
        private Transformer(boolean logForParams, boolean logForResult, Map<String, List<MethodDesc>> instrumentMethods) {
            this.logForParams = logForParams;
            this.logForResult = logForResult;
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
