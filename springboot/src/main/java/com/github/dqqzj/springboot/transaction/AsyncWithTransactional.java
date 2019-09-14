package com.github.dqqzj.springboot.transaction;

import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qinzhongjian
 * @date created in 2019-08-31 10:07
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Component
public class AsyncWithTransactional {


    @Transactional
    @Async
    public void transactional() {
        AsyncWithTransactional asyncWithTransactional = AsyncWithTransactional.class.cast(AopContext.currentProxy());
        asyncWithTransactional.async();

    }


    public void async() {

    }
}
