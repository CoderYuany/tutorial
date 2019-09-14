package com.github.dqqzj.springboot.annotation;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author qinzhongjian
 * @date created in 2019-07-30 20:45
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */

@Component
public class BeanOne implements InitializingBean {

    //@Autowired
    BeanTwo beanTwo;
    @Override
    public void afterPropertiesSet() throws Exception {

    }


    public BeanOne(@Autowired  BeanTwo beanTwo) {
        this.beanTwo = beanTwo;
    }

    public BeanTwo getBeanTwo() {
        return beanTwo;
    }
}
