package com.github.dqqzj.springboot.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author qinzhongjian
 * @date created in 2019-08-19 18:54
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@EnableAsync
@SpringBootApplication
public class AsyncApplication {
    public static void main(String[] args) {
        SpringApplication.run(AsyncApplication.class, args);
    }
}
