package com.github.dqqzj.athena.core;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:28
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
public interface ResultCodeInterface {
    /**
     * 错误码
     *
     * @return 错误码
     */
    String code();

    /**
     * 错误具体信息
     *
     * @return 错误具体信息
     */
    String message();
}
