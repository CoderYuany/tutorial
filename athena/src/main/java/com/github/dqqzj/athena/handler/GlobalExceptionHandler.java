package com.github.dqqzj.athena.handler;

import com.github.dqqzj.athena.Unify;
import com.github.dqqzj.athena.core.ResultVO;
import com.github.dqqzj.athena.core.enums.ResultCodeEnum;
import com.github.dqqzj.athena.core.exception.BizRuntimeException;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:44
 * @description 全局异常处理器
 * @since JDK 1.8.0_212-b10
 */
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResultVO handleIllegalArgumentException(IllegalArgumentException e) {
        return ResultVO.common(ResultCodeEnum.ILLEGAL_ARGUMENT)
            .success(false)
            .traceId(MDC.get(Unify.TRACE_KEY))
            .data(e.getMessage())
            .build();
    }

    @ExceptionHandler(BizRuntimeException.class)
    public ResultVO handleBizRuntimeException(BizRuntimeException e) {
        return ResultVO.builder()
            .success(Boolean.FALSE)
            .traceId(MDC.get(Unify.TRACE_KEY))
            .code(e.getErrorCode())
            .message(e.getMessage())
            .build();
    }

    // other handle logic...

    @ExceptionHandler(Exception.class)
    public ResultVO handleException(Exception e) {
        return ResultVO.common(ResultCodeEnum.INTERNAL_SERVER_ERROR)
            .success(Boolean.FALSE)
            .traceId(MDC.get(Unify.TRACE_KEY))
            .message(e.getMessage())
            .build();
    }
}
