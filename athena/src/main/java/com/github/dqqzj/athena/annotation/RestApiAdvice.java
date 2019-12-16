package com.github.dqqzj.athena.annotation;

import java.lang.annotation.*;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:19
 * @description API的GlobalExceptionHandler标记
 * @since JDK 1.8.0_212-b10
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RestApiAdvice {
}
