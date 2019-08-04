package com.github.dqqzj.springboot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qinzhongjian
 * @date created in 2019-07-30 19:14
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dqqzj {
    String value();
}
