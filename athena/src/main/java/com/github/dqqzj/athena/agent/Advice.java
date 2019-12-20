package com.github.dqqzj.athena.agent;

import java.io.Serializable;

/**
 * @author wb-qzj584329
 * @create 2019/12/20 10:59
 * @description TODO
 * @since JDK1.8.0_211-b12
 */
public class Advice implements Serializable {

    public Class<?> targetClass;
    /**
     * 注意method是不能被序列化的，所以这就能解释arthas的源码里要自己再定义一个arthasMethod
     */
    public String methodName;
    public Class<?>[] paramTypes;
    /**
     * 存放着解析后的参数字符串
     */
    public String[] args;

    public Advice() {

    }

    public Advice(Class<?> tc, String m, Class<?>[] pt, String[] ar) {
        targetClass = tc;
        methodName = m;
        paramTypes = pt;
        args = ar;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

}

