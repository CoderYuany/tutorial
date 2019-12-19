package com.github.dqqzj.athena.annotation;

import java.lang.annotation.*;

import com.github.dqqzj.athena.core.ResultVO;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:19
 * @description API的GlobalExceptionHandler标记
 * @since JDK 1.8.0_212-b10
 */

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@LogForAll
public @interface LogAdvice {
    boolean value() default true;
    Class<?> include() default ResultVO.class;
}
