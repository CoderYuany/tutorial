package com.github.dqqzj.athena.annotation;

import java.lang.annotation.*;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:23
 * @description 返回值打印
 * @since JDK 1.8.0_212-b10
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogForResult {
    boolean logForResult() default false;
}