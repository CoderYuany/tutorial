package com.github.dqqzj.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author qinzhongjian
 * @date created in 2019/12/21 21:48
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("hello/{name}/{age}")
    public String hello(@PathVariable String name,@PathVariable Integer age) {
        log.info("change params");
        name = "----" + name;
        age = 100 + age;
        return testService.hello(name,age);
    }
}
