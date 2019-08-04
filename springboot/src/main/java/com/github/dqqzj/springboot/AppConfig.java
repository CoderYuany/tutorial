package com.github.dqqzj.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public BeanTwo beanTwo() {
        return new BeanTwo();
    }
}
