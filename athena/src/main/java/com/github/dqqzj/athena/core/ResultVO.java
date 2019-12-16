package com.github.dqqzj.athena.core;

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
    private Boolean success;
    private String code;
    private String message;
    private T data;

    public static <T> ResultVOBuilder<T> successBuilder(T data) {
        return ResultVO.<T>commonBuilder(ResultCodeEnum.SUCCESS)
                .success(Boolean.TRUE)
                .data(data);
    }

    public static <T> ResultVOBuilder<T> commonBuilder(ResultCodeInterface resultCode) {
        return ResultVO.<T>builder()
                .code(resultCode.code())
                .message(resultCode.message());
    }
}