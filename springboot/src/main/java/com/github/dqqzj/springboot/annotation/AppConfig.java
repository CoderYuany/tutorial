package com.github.dqqzj.springboot.annotation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author qinzhongjian
 * @date created in 2019-07-30 20:44
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Component
public class AppConfig {
    /*@Bean()
    public BeanOne beanOne() {
        return new BeanOne(beanTwo());
    }*/

    @Bean
    @Qualifier
    public BeanTwo beanTwo() {
        return new BeanTwo();
    }
}
