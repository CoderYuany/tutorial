package com.github.dqqzj.athena.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:58
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvokeMethod {
    private Object proxy;
    private Object target;
    private Method method;
    private Object[] args;
    private Object result;
    private Throwable throwable;
}
