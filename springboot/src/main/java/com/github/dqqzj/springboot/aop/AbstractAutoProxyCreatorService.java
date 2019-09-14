package com.github.dqqzj.springboot.aop;

import org.springframework.stereotype.Service;

/**
 * @author qinzhongjian
 * @date created in 2019-08-25 09:33
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Service
public class AbstractAutoProxyCreatorService {
    public String hello() {
        System.out.println("hello, AbstractAutoProxyCreatorService");
        return "hello, AbstractAutoProxyCreatorService";
    }
}
