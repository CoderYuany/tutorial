package com.github.dqqzj.springboot.aop;

import org.springframework.aop.SpringProxy;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author qinzhongjian
 * @date created in 2019-08-24 11:27
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public class ProxyFactoryBeanApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AopService.class);
        applicationContext.register(MyMethodBeforeAdvice.class);
        applicationContext.refresh();
        // 如果通过类型获取，会找到两个Bean：一个我们自己的实现类、一个ProxyFactoryBean所生产的代理类 而此处我们显然是希望要生成的代理类的  因此我们只能通过名称来(或者加上@Primary)
        // AopService bean = applicationContext.getBean(AopService.class);
        AopService bean = (AopService) applicationContext.getBean("proxyFactoryBean");
        System.out.println(bean.hello());
        System.out.println(bean);
        System.out.println(bean.getClass());
        SpringProxy springProxy = (SpringProxy) bean;
        System.out.println(springProxy);
        applicationContext.close();
    }

}
