package com.github.dqqzj.log;

import java.util.Arrays;

public class MethodDesc {
    private String className;
    private String methodName;
    private String[] methodArgs;

    public MethodDesc(String className, String methodName, String... methodArgs) {
        this.className = className;
        this.methodName = methodName;
        this.methodArgs = methodArgs;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getMethodArgs() {
        return methodArgs;
    }

    @Override
    public String toString() {
        return className + "::" + methodName
                + Arrays.toString(methodArgs)
                .replace("[", "(")
                .replace("]", ")");
    }
}