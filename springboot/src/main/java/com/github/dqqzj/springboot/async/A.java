package com.github.dqqzj.springboot.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author qinzhongjian
 * @date created in 2019-08-19 18:55
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
//@DependsOn(value = {"b"})
@Service
public class A {
    @Autowired
    B b;
    public String self() {
        return test();
    }
    @Async
    public String test() {
        String test = Thread.currentThread().getName();
        System.out.println(test);
        return test;
    }
}
