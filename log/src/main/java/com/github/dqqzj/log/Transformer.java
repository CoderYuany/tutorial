package com.github.dqqzj.log;

import com.alibaba.fastjson.JSON;
import javassist.*;
import lombok.extern.slf4j.Slf4j;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author qinzhongjian
 * @date created in 2019/12/23 12:13
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
public class Transformer implements ClassFileTransformer {
    private final LogPrinter logPrinter = new LogPrinter();
    private final Map<String, List<MethodDesc>> instructionMap;
    private boolean logForParams;
    private boolean logForResult;

    public Transformer(boolean logForParams, boolean logForResult, Map<String, List<MethodDesc>> instrumentMethods) {
        this.logForParams = logForParams;
        this.logForResult = logForResult;
        this.instructionMap = instrumentMethods;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
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
            //ClassFile classFile = ctClass.getClassFile();
            //ConstPool constPool = classFile.getConstPool();
            //String className = ctClass.toClass().getName();
            CtField ctField = CtField.make("private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(" + className + ".class);", ctClass);
            ctClass.addField(ctField);
            /*FieldInfo fieldInfo = new FieldInfo(constPool,"logger","Lorg/slf4j/Logger");
            fieldInfo.setAccessFlags(AccessFlag.PUBLIC);
            fieldInfo.
            classFile.addField(fieldInfo);*/
            for (MethodDesc methodDesc : collection) {
                instrumentMethod(pool, ctClass, methodDesc);
            }
            byte[] byteCode = ctClass.toBytecode();
            ctClass.detach();
            return byteCode;
        } catch (Throwable e) {
            System.out.println("instrumentClass invoked class error....");
            log.error("instrumentClass invoked class error. ", e);
            return null;
        }
    }

    private void instrumentMethod(ClassPool pool, CtClass ctClass, MethodDesc methodDesc) {
        try {
            CtClass[] methodArgs = Stream.of(methodDesc.getMethodArgs())
                    .map(type -> getCtClass(pool, type))
                    .toArray(CtClass[]::new);
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodDesc.getMethodName(), methodArgs);
            logForMethod(ctMethod);
        } catch (Throwable e) {
            log.error("instrumentMethod ctClass.getDeclaredMethod method error.pool:{} ctClass:{} methodDesc:{}", JSON.toJSONString(pool), JSON.toJSONString(ctClass), JSON.toJSONString(methodDesc), e);
        }
    }

    private CtClass getCtClass(ClassPool pool, String className) {
        try {
            switch (className) {
                case "boolean":
                    return CtClass.booleanType;
                case "char":
                    return CtClass.charType;
                case "byte":
                    return CtClass.byteType;
                case "shot":
                    return CtClass.shortType;
                case "int":
                    return CtClass.intType;
                case "long":
                    return CtClass.longType;
                case "float":
                    return CtClass.floatType;
                case "double":
                    return CtClass.doubleType;
                case "void":
                    return CtClass.voidType;
                default:
                    return pool.getCtClass(className);
            }
        } catch (Throwable ex) {
            throw new RuntimeException("class not found " + className, ex);
        }
    }

    private void logForMethod(CtMethod m) {
        try {
            /**
             * 方法调用入参的日志打印
             */
            if (logForParams) {
                String successCode = logPrinter.getCodeLogMethodArgs(m);
                m.insertAfter(successCode);
            }
            /**
             * 方法调用出参的日志打印
             */
            if (logForResult) {
                String successCode = logPrinter.getCodeLogMethodResult(m, "$_");
                m.insertAfter(successCode);
            }

            /**
             * 方法调用过程中产生了异常的日志打印
             */
            String errorCode = "{ " + logPrinter.getCodeLogMethodArgsAndResult(m, "$ex") + "; throw $ex; }";
            CtClass exceptionClass = ClassPool.getDefault().getCtClass("java.lang.Exception");
            m.addCatch(errorCode, exceptionClass, "$ex");
        } catch (Throwable e) {
            log.error("logForMethod to handler method error.", e);
        }
    }
}