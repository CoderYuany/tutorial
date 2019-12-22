package com.github.dqqzj.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.util.concurrent.Callable;

/**
 * @author qinzhongjian
 * @date created in 2019/12/21 21:48
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
@RestController
public class TestController {
    @GetMapping("hello")
    public String hello() {
        log.info("TestMain start........");
        InetAddress address = noFail(() -> InetAddress.getByName("www.google.com"));
        // System.out.println("TestMain returned " + address);
        // noFail(() -> new Socket("www.google.con", 80));
        return Thread.currentThread().getName();
    }

    private <T> T noFail(Callable<T> action) {
        try {
            return action.call();
        } catch (Exception ex) {
            System.out.println("---------Error " + ex);
            return null;
        }
    }
}
