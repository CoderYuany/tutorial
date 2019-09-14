package com.github.dqqzj.springboot.selector;

import org.springframework.stereotype.Component;

/**
 * @author qinzhongjian
 * @date created in 2019-07-28 20:29
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Component
public class HttpServer implements Server {
    @Override
    public void start() {
        System.out.println("http server starting......");
    }

    @Override
    public void stop() {
        System.out.println("http server stop......");
    }
}
