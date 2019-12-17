package com.github.dqqzj.athena;

import com.github.dqqzj.athena.config.Configuration;
import com.github.dqqzj.athena.core.InvokeMethod;
import com.github.dqqzj.athena.resolver.ExceptionResolver;
import com.github.dqqzj.athena.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.MDC;

import java.lang.reflect.Method;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:48
 * @description
 *  1. 扩展机制
 *  2. 日志打印格式确定
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
public class Unify {

    public static final String TRACE_KEY = Configuration.TRACE_KEY;

    /**
     * 统一异常处理入口
     *
     * @param pjp  切点
     * @param returnType  返回类型
     * @param globalExceptionHandler 全局异常处理器，Method返回值均为returnType
     * @return 执行结果，有可能是异常结果
     * @throws Throwable 不能处理的异常
     */
    public static Object process(ProceedingJoinPoint pjp, Class<?> returnType, Object globalExceptionHandler)
            throws Throwable {
        try {
            String traceId = Configuration.traceUtil.get();
            MDC.put(TRACE_KEY, traceId);
            Method classMethod = ReflectionUtils.getClassMethod(pjp);
            // 类型检查
            if (!returnType.isAssignableFrom(classMethod.getReturnType())) {
                return pjp.proceed();
            }
            InvokeMethod invokeMethod = ReflectionUtils.getInvokeMethod(pjp);

            Object result;
            try {
                Configuration.LogConfig.log4InputParams.accept(invokeMethod);
                Configuration.ValidatorConfig.jsrValidator.accept(invokeMethod);
                result = pjp.proceed();
                invokeMethod.setResult(result);
                Configuration.LogConfig.log4ReturnValues.accept(invokeMethod);
            } catch (Throwable throwable) {
                invokeMethod.setThrowable(throwable);
                Configuration.LogConfig.log4Exceptions.accept(invokeMethod);
                result = ExceptionResolver.processException(pjp, throwable, returnType, globalExceptionHandler);
            }
            return result;
        } finally {
            MDC.clear();
        }
    }
}