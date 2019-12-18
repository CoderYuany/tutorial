package com.github.dqqzj.athena.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:20
 * @description 日志打印
 * @since JDK 1.8.0_212-b10
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@LogForParams
@LogForResult
public @interface LogForAll {
    @AliasFor(annotation = LogForParams.class)
    boolean logForParams() default true;
    @AliasFor(annotation = LogForResult.class)
    boolean logForResult() default true;
}
