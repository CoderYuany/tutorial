package com.github.dqqzj.springboot;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qinzhongjian
 * @date created in 2019-07-28 20:38
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Import(ServerImportSelector.class)
public @interface EnableServer {
    Server.Type type();
}
