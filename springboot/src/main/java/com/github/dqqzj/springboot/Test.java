package com.github.dqqzj.springboot;

/**
 * @author qinzhongjian
 * @date created in 2019-07-28 07:57
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Hello
public class Test {
    public static void main(String[] args) throws NoSuchMethodException {
        Hello hello = Test.class.getAnnotation(Hello.class);
        System.out.println(hello.value());
    }
}
