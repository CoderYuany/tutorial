package com.github.dqqzj.springboot;

/**
 * @author qinzhongjian
 * @date created in 2019-07-28 20:29
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public interface Server {
    void start();
    void stop();
    enum Type {
        HTTP,FTP;
    }
}
