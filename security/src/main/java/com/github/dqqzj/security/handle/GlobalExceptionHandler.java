package com.github.dqqzj.security.handle;

import com.github.dqqzj.security.enums.RestStatusEnum;
import com.github.dqqzj.security.exception.RestStatusException;
import com.github.dqqzj.security.model.response.RestEntity;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

        private static final ImmutableMap<Class<? extends Throwable>, RestStatusEnum> EXCEPTION_MAPPINGS;

        static {
                final ImmutableMap.Builder<Class<? extends Throwable>, RestStatusEnum> builder = ImmutableMap.builder();
                // SpringMVC中参数类型转换异常，常见于String找不到对应的ENUM而抛出的异常
                builder.put(MethodArgumentTypeMismatchException.class, RestStatusEnum.INVALID_PARAMS_CONVERSION);
                builder.put(UnsatisfiedServletRequestParameterException.class, RestStatusEnum.INVALID_PARAMS_CONVERSION);
                // HTTP Request Method不存在
                builder.put(HttpRequestMethodNotSupportedException.class, RestStatusEnum.REQUEST_METHOD_NOT_SUPPORTED);
                // 要求有RequestBody的地方却传入了NULL
                builder.put(HttpMessageNotReadableException.class, RestStatusEnum.HTTP_MESSAGE_NOT_READABLE);
                builder.put(HttpMediaTypeNotSupportedException.class,RestStatusEnum.MEDIA_TYPE_NOT_SUPPORTED);
                builder.put(CacheLoader.InvalidCacheLoadException.class,RestStatusEnum.SMS_GOOGLE_CACHE_ERROR);
                // 通常是操作过快导致DuplicateKey
                builder.put(DuplicateKeyException.class, RestStatusEnum.DUPLICATE_KEY);
                // 其他未被发现的异常
                builder.put(Exception.class, RestStatusEnum.SERVER_UNKNOWN_ERROR);
                EXCEPTION_MAPPINGS = builder.build();
        }

        @ExceptionHandler(value = RestStatusException.class)
        @ResponseBody
        public RestEntity RestStatusExceptionHandle(RestStatusException e) {
                log.error("【RestStatusException】{} -->>", e);
                Integer code = Integer.parseInt(e.getMessage());
                final RestEntity error = new RestEntity(RestStatusEnum.valueOfCode(code),e.getMessage());
                return error;
        }
        @ExceptionHandler(value = Exception.class)
        @ResponseBody
        public RestEntity handle(Exception e) {
                log.error("【GlobalExceptionHandler】{} -->>", e);
                Integer code = Integer.parseInt(e.getMessage());
                boolean isRestStatusException =  Arrays.stream(RestStatusEnum.values()).anyMatch(restStatusEnum -> restStatusEnum.getCode().equals(code));
                if (isRestStatusException){
                        return new RestEntity(RestStatusEnum.valueOfCode(code),e.getMessage());
                }
                final RestStatusEnum status = EXCEPTION_MAPPINGS.get(e.getClass());
                final RestEntity error;
                if (status != null) {
                        error = new RestEntity(status,e);
                } else {
                        error = new RestEntity(RestStatusEnum.SERVER_UNKNOWN_ERROR,e);
                }
                return error;
            }
}
