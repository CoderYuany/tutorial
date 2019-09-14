package com.github.dqqzj.springboot.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author qinzhongjian
 * @date created in 2019-07-28 07:57
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Hello
public class TestAnnotation {
    public static void main(String[] args) {
        //System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
        Hello hello = TestAnnotation.class.getAnnotation(Hello.class);
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(hello);
        Field value = null;
        try {
            value = invocationHandler.getClass().getDeclaredField("memberValues");
            value.setAccessible(true);
            try {
                Map<String, Object> memberValues = (Map<String, Object>) value.get(invocationHandler);
                memberValues.put("value", "dqqzj");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        System.out.println(hello.value());
    }
}
