package com.github.dqqzj.springboot.aop;

import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author qinzhongjian
 * @date created in 2019-08-25 09:26
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public class BeanNameAutoProxyCreatorApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext();
        configApplicationContext.register(AopServiceInstantiationAwareBeanPostProcessor.class);
        configApplicationContext.register(MyMethodBeforeAdvice.class);
        configApplicationContext.register(AopService.class);
        configApplicationContext.register(AbstractAutoProxyCreatorService.class);
        configApplicationContext.register(MyBeanNameAutoProxyCreator.class);
        configApplicationContext.refresh();
        AopService aopService = (AopService) configApplicationContext.getBean("aopService");
        AbstractAutoProxyCreatorService service = (AbstractAutoProxyCreatorService) configApplicationContext.getBean("abstractAutoProxyCreatorService");
        aopService.hello();
        service.hello();
        configApplicationContext.close();
    }
}
