package com.github.dqqzj.athena.agent.utils;

import java.lang.reflect.Method;

import com.github.dqqzj.athena.agent.Advice;

/**
 * @author wb-qzj584329
 * @create 2019/12/20 10:57
 * @description TODO
 * @since JDK1.8.0_211-b12
 */
public class AdviceUtils {

    public static Advice timeTunnelInitAdvice(String className, String methodName) throws ClassNotFoundException {
        Advice advice = new Advice();
        // 保留类
        Class<?> tc = Class.forName(className);
        advice.setTargetClass(tc);
        Method[] methods = tc.getMethods();
        for (Method mTemp : methods) {
            if (mTemp.getName().equals(methodName)) {
                // 保留方法
                advice.setMethodName(mTemp.getName());
                // 保留方法参数类型
                advice.setParamTypes(mTemp.getParameterTypes());
            }
        }
        return advice;
    }
}

