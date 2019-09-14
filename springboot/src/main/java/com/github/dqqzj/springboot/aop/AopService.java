package com.github.dqqzj.springboot.aop;

import org.springframework.stereotype.Service;

/**
 * @author qinzhongjian
 * @date created in 2019-08-24 11:07
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Service
public class AopService {

    public String hello() {
        System.out.println("hello, AopService");
        return "hello, AopService";
    }
    public String test() {
        System.out.println("test");
        return hello();
    }
}
