package com.github.dqqzj.security.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

/**
 * @author qinzhongjian
 * @date created in 2018/6/25 22:54
 * @since 1.0.0
 */
public final class Jacksons {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Jacksons() {
    }

    public static <T> String parse(T obj) {
        try {
            if (obj == null) {
                return null;
            }
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            Throwables.throwIfUnchecked(e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * description: <br>
     * 优雅的输出json字符串
     * @param: obj
     * @return: string
     * @method：parseInPrettyMode
     */

    public static <T> String parseInPrettyMode(T obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            Throwables.throwIfUnchecked(e);
            throw new IllegalArgumentException(e);
        }
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }
}

