package com.github.dqqzj.athena.annotation;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:22
 * @description 入参参数打印
 * @since JDK 1.8.0_212-b10
 */

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogForParams {
    boolean value() default true;
}
