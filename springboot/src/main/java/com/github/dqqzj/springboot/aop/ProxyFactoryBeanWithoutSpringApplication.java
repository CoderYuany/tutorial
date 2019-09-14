package com.github.dqqzj.springboot.aop;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectInstanceFactory;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.SimpleAspectInstanceFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * @author qinzhongjian
 * @date created in 2019-08-24 12:59
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public class ProxyFactoryBeanWithoutSpringApplication {
    public static void main(String[] args) {
        String pointcutExpression = "execution(* com.github..aop.*.*(..))";
        ProxyFactoryBean factory = new ProxyFactoryBean();
        //AspectJProxyFactory factory = new AspectJProxyFactory(new AopService());
        factory.setTarget(new AopService());
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression(pointcutExpression);
        // 声明一个通知（此处使用环绕通知 MethodInterceptor ）
        Advice advice = (MethodInterceptor) invocation -> {
            System.out.println("============>before...");
            Object obj = invocation.proceed();
            System.out.println("============>after...");
            return obj;
        };
        Advisor advisor = new DefaultPointcutAdvisor(aspectJExpressionPointcut, advice);
        factory.addAdvisor(advisor);
        //AopService aopService = factory.getProxy();
        AopService aopService = (AopService) factory.getObject();
        aopService.hello();
    }
}
