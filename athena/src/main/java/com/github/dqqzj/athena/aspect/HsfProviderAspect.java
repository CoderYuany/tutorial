package com.github.dqqzj.athena.aspect;

import com.github.dqqzj.athena.Unify;
import com.github.dqqzj.athena.core.ResultVO;
import com.github.dqqzj.athena.handler.GlobalExceptionHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:24
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
@Aspect
@Component
public class HsfProviderAspect {

    private static final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Pointcut("@within(com.github.dqqzj.athena.annotation.RestApiAdvice)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object testPointcutAround(ProceedingJoinPoint pjp) throws Throwable {
        return Unify.process(pjp, ResultVO.class, globalExceptionHandler);
    }
}
