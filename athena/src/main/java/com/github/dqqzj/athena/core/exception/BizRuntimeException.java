package com.github.dqqzj.athena.core.exception;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:28
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
public class BizRuntimeException extends RuntimeException {
    private String errorCode;

    public BizRuntimeException(ResultCodeInterface error) {
        super(error.message());
        this.errorCode = error.code();
    }

    public BizRuntimeException(ResultCodeInterface error, String message) {
        super(message);
        this.errorCode = error.code();
    }

    public BizRuntimeException(String code, String message) {
        super(message);
        this.errorCode = code;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
