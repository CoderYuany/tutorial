package com.github.dqqzj.security.exception;

/**
 * @author qinzhongjian
 * @date created in 2018/6/25 23:09
 * @since 1.0.0
 */
public class RestStatusException extends RuntimeException {

    private static final long serialVersionUID = -8541311111016065562L;

    public RestStatusException(String message) {
        super(message);
    }

    public RestStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestStatusException(Throwable cause) {
        super(cause);
    }

    protected RestStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
