package com.github.dqqzj.springboot.aop;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author qinzhongjian
 * @date created in 2019-08-25 09:43
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Component
public class MyBeanNameAutoProxyCreator extends BeanNameAutoProxyCreator {
    @PostConstruct
    public void init() {
        super.setBeanNames("aopService", "abstractAutoProxyCreatorService");
        super.setInterceptorNames("myMethodBeforeAdvice");
    }

}
