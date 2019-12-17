package com.github.dqqzj.athena.utils;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

class BeanValidationTest {

    @Test
    void checkAndThrow() {
        BeanDemo beanDemo = new BeanDemo();
        beanDemo.name = "Hi~";
        beanDemo.age = 20;
        BeanDemo beanDemo1 = new BeanDemo();
        beanDemo1.name = "dqq~";
        beanDemo1.age = 22;
        beanDemo.setBeanDemo(beanDemo1);
        System.out.println(JSON.toJSONString(beanDemo));
    }
    @Data
    public class BeanDemo {
        private String name;
        private int age;
        private BeanDemo beanDemo;

    }
}