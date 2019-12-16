package com.github.dqqzj.athena.resolver;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.Assert;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:40
 * @description 将异常转化为Result对象
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
public class ExceptionResolver {
    public static Object processException(ProceedingJoinPoint pjp, Throwable throwable, Class<?> returnType,
                                          Object globalExceptionHandler) {
        Method classMethod = RefUtil.getClassMethod(pjp);
        // for inner throwable handle
        Object target = pjp.getTarget();
        log.error(RefUtil.getMethodFullName(classMethod) + ": ", throwable);

        // 防御式做法
        if (!(throwable instanceof Exception)) {
            throwable = new RuntimeException(throwable);
        }
        Exception exception = ((Exception) throwable);

        // inner throwable handler
        Object exceptionHandler = target;
        ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(exceptionHandler.getClass());
        Method resolveMethod = resolver.resolveMethod(exception);
        if (resolveMethod == null) {
            // global throwable handler
            exceptionHandler = globalExceptionHandler;
            resolver = new ExceptionHandlerMethodResolver(exceptionHandler.getClass());
            resolveMethod = resolver.resolveMethod(exception);
        }

        Assert.isTrue(returnType.isAssignableFrom(classMethod.getReturnType()), "Type Must Be SAME");
        Object invoke = null;
        try {
            invoke = resolveMethod.invoke(exceptionHandler, throwable);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return invoke;
    }
}
