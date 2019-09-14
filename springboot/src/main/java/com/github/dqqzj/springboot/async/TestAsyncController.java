package com.github.dqqzj.springboot.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinzhongjian
 * @date created in 2019-08-19 18:58
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@RestController
public class TestAsyncController {
    @Autowired
    B b;
    @GetMapping("test")
    public String test() {
        return b.test();
    }

    /**
     * InfrastructureAdvisorAutoProxyCreator`/AspectJAwareAdvisorAutoProxyCreator/AnnotationAwareAspectJAutoProxyCreator的一个逻辑
     * @return
     */
    @GetMapping("self")
    public String self() {
        return b.self();
    }
}
