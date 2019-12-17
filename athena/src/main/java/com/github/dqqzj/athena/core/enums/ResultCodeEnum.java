package com.github.dqqzj.athena.core.enums;

import com.github.dqqzj.athena.core.ResultCodeInterface;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:34
 * @description 各种状态码
 * @since JDK 1.8.0_212-b10
 */
public enum ResultCodeEnum implements ResultCodeInterface {
    OK(200, "OK"),
    ILLEGAL_ARGUMENT (301, "ILLEGAL ARGUMENT"),
    UNAUTHORIZED(401, "Unauthorized"),
    NOT_FOUND(404, "Not Found"),
    REQUEST_TIMEOUT(408, "Request Timeout"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private int code;
    private String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
