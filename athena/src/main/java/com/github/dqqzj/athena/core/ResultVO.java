package com.github.dqqzj.athena.core;

import com.github.dqqzj.athena.core.enums.ResultCodeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:36
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
@Data
@Builder
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = -4448696578582628909L;

    private String traceId;
    private boolean success;
    private int code;
    private String message;
    private T data;

    public static <T> ResultVO<T> ofSuccess(T data) {
        return ResultVO.<T>ofCommon(ResultCodeEnum.OK)
                .success(Boolean.TRUE)
                .data(data)
                .build();
    }
    public static <T> ResultVO<T> ofError(ResultCodeInterface resultCode, T data) {
        return ResultVO.<T>ofCommon(resultCode)
            .success(Boolean.FALSE)
            .data(data)
            .build();
    }
    public static <T> ResultVOBuilder<T> ofCommon(ResultCodeInterface resultCode) {
        return ResultVO.<T>builder()
                .code(resultCode.code())
                .message(resultCode.message());
    }
}