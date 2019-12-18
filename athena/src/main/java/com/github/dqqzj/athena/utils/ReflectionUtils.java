package com.github.dqqzj.athena.utils;

import com.github.dqqzj.athena.core.InvokeMethod;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:46
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
public abstract class ReflectionUtils {

    /**
     * 对象类型字段
     */
    private static final String TYPE = "TYPE";

    public static Method getClassMethod(ProceedingJoinPoint pjp) {
        Object target = pjp.getTarget();
        Class<?> clazz = target.getClass();
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return getClassMethod(clazz, method);
    }

    public static InvokeMethod getInvokeMethod(ProceedingJoinPoint joinPoint) {
        Method classMethod = getClassMethod(joinPoint);
        return InvokeMethod.builder()
                .args(joinPoint.getArgs())
                .method(classMethod)
                .proxy(joinPoint.getThis())
                .target(joinPoint.getTarget())
                .build();
    }

    public static String getMethodFullName(Method method) {
       String packageName =  method.getDeclaringClass().getPackage().getName();
        return packageName + "." + method.getDeclaringClass().getSimpleName() + "#" + method.getName();
    }

    private static Method getClassMethod(Class<?> clazz, Method method) {
        for (Method m : clazz.getDeclaredMethods()) {
            String signature1 = generateMethodSignature(method);
            String signature2 = generateMethodSignature(m);
            if (StringUtils.endsWithIgnoreCase(signature1, signature2)) {
                return m;
            }
        }
        throw new RuntimeException("can`n match this method " + method + " in " + clazz);
    }

    private static String generateMethodSignature(Method method) {
        return method.getName() + "(" + StringUtils.join(getMethodSignature(method)) + ")";
    }

    /**
     * copy from org.springframework.jmx.support.JmxUtils#getMethodSignature(java.lang.reflect.Method)
     * <p>
     * Create a {@code String[]} representing the argument signature of a
     * method. Each element in the array is the fully qualified class name
     * of the corresponding argument in the methods signature.
     *
     * @param method the method to build an argument signature for
     * @return the signature as array of argument types
     */
    public static String[] getMethodSignature(Method method) {
        Class<?>[] types = method.getParameterTypes();
        String[] signature = new String[types.length];
        for (int x = 0; x < types.length; x++) {
            signature[x] = types[x].getName();
        }
        return signature;
    }

    public static boolean isPrimitive(Object object) {
        try {
            if (((Class<?>)object.getClass().getField(TYPE).get(null)).isPrimitive()) {
                return true;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            /**
             * if this field is String,it should be ignored.
             */
            return false;
        }
        return false;
    }
}
