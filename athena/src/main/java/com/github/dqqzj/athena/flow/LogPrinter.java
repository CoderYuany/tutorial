package com.github.dqqzj.athena.flow;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.ParameterNameProvider;
import com.alibaba.fastjson.JSON;
import com.github.dqqzj.athena.annotation.LogForAll;
import com.github.dqqzj.athena.annotation.LogForParams;
import com.github.dqqzj.athena.annotation.LogForResult;
import com.github.dqqzj.athena.core.InvokeMethod;
import com.github.dqqzj.athena.utils.DefaultParameterProvider;
import com.github.dqqzj.athena.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;


/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:42
 * @description 打印入参出参日志
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
public class LogPrinter {

    private static ParameterNameProvider discoverer = new DefaultParameterProvider();

    private static boolean checkLogConfigForInputParams(Method method) {
        LogForParams logForParams = AnnotationUtils.getAnnotation(method, LogForParams.class);
        if (logForParams != null) {
            return logForParams.logForParams();
        }
       /* LogForAll logForAll = AnnotationUtils.getAnnotation(method, LogForAll.class);
        if (logForAll != null) {
            return logForAll.logForParams();
        }*/
        logForParams = AnnotationUtils.getAnnotation(method.getDeclaringClass(), LogForParams.class);
        if (logForParams != null) {
            return logForParams.logForParams();
        }
        LogForAll logForAll = AnnotationUtils.getAnnotation(method.getDeclaringClass(), LogForAll.class);
        if (logForAll != null) {
            return logForAll.logForParams();
        }
        return false;
    }

    private static boolean checkLogConfigForResult(Method method) {
        LogForResult logForResult = AnnotationUtils.getAnnotation(method, LogForResult.class);
        if (logForResult != null) {
            return logForResult.logForResult();
        }
       /* LogForAll logForAll = AnnotationUtils.getAnnotation(method, LogForAll.class);
        if (logForAll != null) {
            return logForAll.logForResult();
        }*/
        logForResult = AnnotationUtils.getAnnotation(method.getDeclaringClass(), LogForResult.class);
        if (logForResult != null) {
            return logForResult.logForResult();
        }
        LogForAll logForAll = AnnotationUtils.getAnnotation(method.getDeclaringClass(), LogForAll.class);
        if (logForAll != null) {
            return logForAll.logForResult();
        }
        return false;
    }

    public static void printLog4InputParams(InvokeMethod invokeMethod) {
        Method classMethod = invokeMethod.getMethod();
        boolean needPrintLog = checkLogConfigForInputParams(classMethod);
        if (!needPrintLog) {
            return;
        }
        Map<String, Object> objectMap = getInnerInputParams(invokeMethod);
        String methodFullName = ReflectionUtils.getMethodFullName(classMethod);
        log.info(methodFullName + " printLog4InputParams: there are parameters = " + JSON.toJSONString(objectMap));
    }

    public static void printLog4ReturnValues(InvokeMethod invokeMethod) {
        Method classMethod = invokeMethod.getMethod();
        boolean needPrintLog = LogPrinter.checkLogConfigForResult(classMethod);
        if (!needPrintLog) {
            return;
        }
        String methodFullName = ReflectionUtils.getMethodFullName(classMethod);
        log.info(methodFullName + " printLog4ReturnValues: there is result = {}", JSON.toJSONString(invokeMethod.getResult()));
    }

    public static void printLog4Exceptions(InvokeMethod invokeMethod) {
        Method classMethod = invokeMethod.getMethod();
        Map<String, Object> objectMap = getInnerInputParams(invokeMethod);
        String methodFullName = ReflectionUtils.getMethodFullName(classMethod);
        log.info(methodFullName + " printLog4Exceptions: there are parameters = " + JSON.toJSONString(objectMap));
        log.error(methodFullName + " ", invokeMethod.getThrowable());
    }

    public static Map<String, Object> getInnerInputParams(InvokeMethod invokeMethod) {
        Method classMethod = invokeMethod.getMethod();
        List<String> parameterNames = discoverer.getParameterNames(classMethod);
        Object[] args = invokeMethod.getArgs();
        Map<String, Object> objectMap = new HashMap<>(args.length);
        for (int i = 0; i < args.length; i++) {
            String parameterName = parameterNames.get(i);
            boolean isPrimitive = ReflectionUtils.isPrimitive(args[i]);
            if (isPrimitive) {
                objectMap.put(parameterName, args[i]);
            } else {
                objectMap.put(parameterName, JSON.toJSONString(args[i]));
            }
        }
        return objectMap;
    }
}
