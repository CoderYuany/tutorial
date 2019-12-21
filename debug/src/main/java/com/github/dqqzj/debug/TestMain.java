package com.github.dqqzj.debug;

import lombok.extern.slf4j.Slf4j;

import java.net.*;
import java.util.concurrent.Callable;

/**
 * @author qinzhongjian
 * @date created in 2019/12/21 16:48
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
public class TestMain {
    public static void main(String[] args) {
        System.out.println("TestMain invoked");
        InetAddress address = noFail(() -> InetAddress.getByName("www.google.com"));
       // System.out.println("TestMain returned " + address);
       // noFail(() -> new Socket("www.google.con", 80));

    }

    private static <T> T noFail(Callable<T> action) {
       try {
            return action.call();
        } catch (Exception ex) {
            System.out.println("---------Error " + ex);
            return null;
        }
    }
}

