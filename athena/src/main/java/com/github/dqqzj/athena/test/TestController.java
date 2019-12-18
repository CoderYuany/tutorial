package com.github.dqqzj.athena.test;

import com.github.dqqzj.athena.annotation.LogAdvice;
import com.github.dqqzj.athena.annotation.LogForAll;
import com.github.dqqzj.athena.core.ResultVO;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @author wb-qzj584329
 * @create 2019/12/17 12:58
 * @description TODO
 * @since JDK1.8.0_211-b12
 */
@LogAdvice
@RestController
public class TestController {
    @Autowired
    TestService testService;
    @GetMapping("hello")
    public ResultVO hello(Integer x, Test y)throws Exception {
        x = x+100;
      //  y = y+100;
        return testService.hello(x,null);
    }
}
