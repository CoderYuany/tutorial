package com.github.dqqzj.springboot.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author qinzhongjian
 * @date created in 2019-08-24 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Aspect
public class MyAspect {
    @Pointcut("execution(* com.github..aop.*.*(..))")
    //@Pointcut("execution(* com.github..aop.AopService.hello(..))")
    private void pointcut() {
    }

    @Before("pointcut()")
    public void before() {
        System.out.println("-----------MyAspect#before-----------");
    }
}
