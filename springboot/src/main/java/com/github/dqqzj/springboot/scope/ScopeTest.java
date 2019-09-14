package com.github.dqqzj.springboot.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 18:54
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Service
@Scope("threadScope")
public class ScopeTest {
    public String getMessage() {
        return "Hello World!";
    }
}
