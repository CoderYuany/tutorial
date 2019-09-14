package com.github.dqqzj.springboot.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qinzhongjian
 * @date created in 2019-07-28 07:54
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Component
public @interface Hello {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "hi" ;
}
