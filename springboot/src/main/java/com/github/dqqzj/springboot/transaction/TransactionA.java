package com.github.dqqzj.springboot.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qinzhongjian
 * @date created in 2019-08-30 22:56
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Service
public class TransactionA {
    @Autowired
    TransactionB transactionB;

    @Transactional
    public void test() {
        System.out.println("TransactionA#test before");
        try {
            transactionB.test();
        } catch (Exception e) {
            System.out.println("transactionB error");
        }
        System.out.println("TransactionA#test before");
    }
}
