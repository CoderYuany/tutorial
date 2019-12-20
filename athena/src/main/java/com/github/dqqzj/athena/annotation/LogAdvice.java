package com.github.dqqzj.athena.annotation;

import com.github.dqqzj.athena.core.ResultVO;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

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
@LogForParams
@LogForResult
public @interface LogAdvice {
    Class<?> value() default ResultVO.class;
    @AliasFor(annotation = LogForParams.class)
    boolean logForParams() default true;
    @AliasFor(annotation = LogForResult.class)
    boolean logForResult() default true;
}
