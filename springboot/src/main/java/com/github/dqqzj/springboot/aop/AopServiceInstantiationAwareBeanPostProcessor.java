package com.github.dqqzj.springboot.aop;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

/**
 * @author qinzhongjian
 * @date created in 2019-08-25 11:35
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public class AopServiceInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (beanClass.isInstance(AopService.class)) {
            return new AopService();
        }
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
