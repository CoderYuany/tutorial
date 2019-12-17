package com.github.dqqzj.athena.utils;

import java.util.UUID;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:47
 * @description trace id 获取工具
 * @since JDK 1.8.0_212-b10
 */
public class TraceUtils {
    public static String getTraceId() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
