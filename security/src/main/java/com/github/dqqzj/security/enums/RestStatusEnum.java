package com.github.dqqzj.security.enums;

import com.google.common.collect.ImmutableMap;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 15:34
 * @description: API 统一返回状态码
 * @since JDK 1.8.0_212-b10
 */
public enum RestStatusEnum {
    /**
     * 凭证错误
     */
    INVALID_CREDENTIAL(1001, "用户名或密码错误！"),
    ACCOUNT_NOT_LOGGED_IN(1002, "用户未登陆,请重新登陆！"),
    ACCOUNT_FORBIDDEN(1003, "用户已被禁用！"),
    ACCOUNT_NOT_EXIST(1004, "用户不存在！"),
    ACCOUNT_EXISTED(1005, "用户已存在,请直接登陆！"),
    ACCOUNT_REGISTER_SUCCESS(1006, "用户注册成功！"),
    ACCOUNT_REGISTER_FAIL(1007, "用户注册失败！"),
    ACCOUNT_LOGIN_INVALID(1008, "用户未登陆或已失效,请重新登陆！"),
    OK(2000, "OK"),
    SMS_CODE_ERROR(2001, "获取阿里云短信码验证失败！"),
    SMS_CODE_CHECK_ERROR(2002, "阿里云短信码校验失败！"),
    SMS_GOOGLE_CACHE_ERROR(2003, "短信验证码过期，请重新发送！"),
    SESSION_INVALID(2004, "你的登陆已过期，请重新登陆！"),
    // 40xxx 客户端不合法的请求
    INVALID_MODEL_FIELDS(2005, "字段校验非法"),
    /**
     * 参数类型非法，常见于SpringMVC中String无法找到对应的enum而抛出的异常
     */
    INVALID_PARAMS_CONVERSION(2006, "参数类型非法"),
    // 41xxx 请求方式出错
    /**
     * http media type not supported
     */
    HTTP_MESSAGE_NOT_READABLE(2007, "HTTP消息不可读"),
    /**
     * 请求方式非法
     */
    REQUEST_METHOD_NOT_SUPPORTED(2008, "不支持的HTTP请求方法"),

    MEDIA_TYPE_NOT_SUPPORTED(2009, "不支持的MediaType请求方法"),
    /**
     * Duplicate Key
     */
    DUPLICATE_KEY(2010, "操作过快, 请稍后再试"),
    /**
     * 用于处理未知的服务端错误
     */
    SERVER_UNKNOWN_ERROR(2012, "服务端异常, 请稍后再试"),

    /* 权限错误：70001-79999 */
    PERMISSION_NOT_SUPPORT(2013, "无访问权限"),

    KAPTCHA_NOT_BLANK(2014, "验证码不能为空"),
    KAPTCHA_ERROR(2015, "验证码错误");


    private final Integer code;
    private final String message;

    private static final ImmutableMap<Integer, RestStatusEnum> CACHE;

    static {
        final ImmutableMap.Builder<Integer, RestStatusEnum> builder = ImmutableMap.builder();
        for (RestStatusEnum restStatusEnum : values()) {
            builder.put(restStatusEnum.getCode(), restStatusEnum);
        }
        CACHE = builder.build();
    }

    public static RestStatusEnum valueOfCode(Integer code) {
        final RestStatusEnum status = CACHE.get(code);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + code + "]");
        }
        return status;
    }

    RestStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
