package com.github.dqqzj.athena.core.exception;

import com.github.dqqzj.athena.core.ResultCodeInterface;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:29
 * @description 简单业务异常，无堆栈信息
 * 另，JDK默认对异常有优化，参见OmitStackTraceInFastThrow
 * @since JDK 1.8.0_212-b10
 */
public class SimpleBizException extends BizRuntimeException {
    public SimpleBizException(ResultCodeInterface error) {
        super(error);
    }

    public SimpleBizException(ResultCodeInterface error, String message) {
        super(error, message);
    }

    public SimpleBizException(int code, String message) {
        super(code, message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
