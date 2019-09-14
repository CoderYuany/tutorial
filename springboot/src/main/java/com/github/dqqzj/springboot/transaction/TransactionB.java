package com.github.dqqzj.springboot.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qinzhongjian
 * @date created in 2019-08-30 22:56
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Service
public class TransactionB {
    @Transactional
    public void test() {
        System.out.println("TransactionB#test before");
        if (true) {
            throw new RuntimeException("error");
        }
        System.out.println("TransactionB#test after");
    }
}
