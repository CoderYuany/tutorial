package com.github.dqqzj.athena.utils;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

class BeanValidationTest {

    @Test
    void checkAndThrow() {
        BeanDemo beanDemo = new BeanDemo();
        beanDemo.field = "Hi~";
        BeanValidation.checkAndThrow(beanDemo);
        System.out.println("Run...");
        beanDemo.field = "";
        BeanValidation.checkAndThrow(beanDemo);
    }

    @Test
    void check() {
        BeanDemo beanDemo = new BeanDemo();
        beanDemo.field = "Hi~";
        String msg = BeanValidation.check(beanDemo);
        Assert.notNull(msg);
        beanDemo.field = "";
        msg = BeanValidation.check(beanDemo);
        Assert.notNull(msg);
    }
    public class BeanDemo {
        private String field;
    }
}