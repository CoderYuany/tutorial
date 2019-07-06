package com.github.dqqzj.com.dynamic.core;

/**
 * @author qinzhongjian
 * @date created in 2019-07-02 20:21
 * @description: 通过ThreadLocal维护一个全局唯一的map来实现数据源的动态切换
 * @since JDK 1.8
 */
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<String> DATASOURCE_HOLDER = new ThreadLocal<>();

    private DynamicDataSourceContextHolder(){};
    public static void clear() {
        DATASOURCE_HOLDER.remove();
    }

    public static String get() {
        return DATASOURCE_HOLDER.get();
    }

    public static void set(String value) {
        DATASOURCE_HOLDER.set(value);
    }

}
