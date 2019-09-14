package com.github.dqqzj.springboot.aop;

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

/**
 * @author qinzhongjian
 * @date created in 2019-08-24 13:48
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public class AspectJProxyFactoryApplication {
    public static void main(String[] args) {
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(new AopService());
        // 注意：此处得MyAspect类上面的@Aspect注解必不可少
        proxyFactory.addAspect(MyAspect.class);
        //proxyFactory.setProxyTargetClass(true);//是否需要使用CGLIB代理
        AopService proxy = proxyFactory.getProxy();
        proxy.test();
    }
}
