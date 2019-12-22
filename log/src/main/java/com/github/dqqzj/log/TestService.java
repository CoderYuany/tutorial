package com.github.dqqzj.log;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author qinzhongjian
 * @date created in 2019/12/22 15:17
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
@Service
public class TestService {
    public String hello(String name, Integer age) {
        throw new RuntimeException("hahahh");
       // return " hello " + name + "your age is " + age;
    }
}
