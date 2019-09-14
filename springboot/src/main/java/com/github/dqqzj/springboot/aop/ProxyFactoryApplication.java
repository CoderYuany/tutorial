package com.github.dqqzj.springboot.aop;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.framework.ProxyFactory;

/**
 * @author qinzhongjian
 * @date created in 2019-08-24 13:42
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public class ProxyFactoryApplication {
    public static void main(String[] args) {
        ProxyFactory proxyFactory = new ProxyFactory(new AopService());
        // 添加两个Advise，一个匿名内部类表示
        proxyFactory.addAdvice((AfterReturningAdvice) (returnValue, method, args1, target) ->
                System.out.println("AfterReturningAdvice ..........."));
        proxyFactory.addAdvice(new MyMethodBeforeAdvice());
        AopService proxy = (AopService) proxyFactory.getProxy();
        proxy.hello();

    }
}
