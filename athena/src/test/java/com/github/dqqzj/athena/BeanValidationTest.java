package com.github.dqqzj.athena;

import com.roc.unify.clear.domain.exception.BizRuntimeException;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.constraints.NotBlank;

public class BeanValidationTest {

    @Test(expected = BizRuntimeException.class)
    public void checkAndThrow() {
        BeanDemo beanDemo = new BeanDemo();
        beanDemo.field = "Hi~";
        BeanValidation.checkAndThrow(beanDemo);
        System.out.println("Run...");
        beanDemo.field = "";
        BeanValidation.checkAndThrow(beanDemo);
    }

    @Test
    public void check() {
        BeanDemo beanDemo = new BeanDemo();
        beanDemo.field = "Hi~";
        String msg = BeanValidation.check(beanDemo);
        Assert.assertNull(msg);
        beanDemo.field = "";
        msg = BeanValidation.check(beanDemo);
        Assert.assertNotNull(msg);
    }

    static class BeanDemo {
        @NotBlank
        private String field;
    }
}