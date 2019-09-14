package com.github.dqqzj.springboot.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinzhongjian
 * @date created in 2019-08-19 18:56
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Service
public class B {
    @Autowired
    A a;
    public String self() {
        return a.self();
    }
    public String test() {
        return a.test();
    }
}
