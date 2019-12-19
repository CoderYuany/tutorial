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

    /**
     * 发生异常的时候进行渲染的异常实体，主要是给前端统一封装异常信息
     * @return
     */
    Class<?> returnType() default ResultVO.class;
}
