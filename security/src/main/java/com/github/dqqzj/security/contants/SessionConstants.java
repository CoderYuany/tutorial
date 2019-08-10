package com.github.dqqzj.security.contants;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 14:54
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public class SessionConstants {
    /**
     * 同一个用户在系统中的最大session数，默认1
     */
    public static final int maximumSessions = 1;
    /**
     * 达到最大session时是否阻止新的登录请求，默认为false，不阻止，新的登录会将老的登录失效掉
     */
    public static final boolean maxSessionsPreventsLogin =false;
    /**
     * session失效时跳转的地址
     */
    public static final String sessionInvalidUrl = SecurityConstants.DEFAULT_URL;
}
