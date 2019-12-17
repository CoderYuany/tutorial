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
    private Boolean success;
    private String code;
    private String message;
    private T data;

    public static <T> ResultVOBuilder<T> success(T data) {
        return ResultVO.<T>common(ResultCodeEnum.SUCCESS)
                .success(Boolean.TRUE)
                .data(data);
    }
    public static <T> ResultVOBuilder<T> error(ResultCodeInterface resultCode, T data) {
        return ResultVO.<T>common(resultCode)
            .success(Boolean.FALSE)
            .data(data);
    }
    public static <T> ResultVOBuilder<T> common(ResultCodeInterface resultCode) {
        return ResultVO.<T>builder()
                .code(resultCode.code())
                .message(resultCode.message());
    }
}