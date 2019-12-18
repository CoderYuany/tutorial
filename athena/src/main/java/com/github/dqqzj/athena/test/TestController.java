package com.github.dqqzj.athena.test;

import com.github.dqqzj.athena.core.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wb-qzj584329
 * @create 2019/12/17 12:58
 * @description TODO
 * @since JDK1.8.0_211-b12
 */

@RestController
public class TestController {
    @Autowired
    TestService testService;
    @GetMapping("hello")
    public ResultVO hello(Integer x,double y) {
        return testService.hello(x,y);
    }
}
