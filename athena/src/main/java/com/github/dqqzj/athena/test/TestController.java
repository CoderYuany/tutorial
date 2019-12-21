package com.github.dqqzj.athena.test;

import com.github.dqqzj.athena.annotation.LogAdvice;
import com.github.dqqzj.athena.annotation.LogForAll;
import com.github.dqqzj.athena.core.ResultVO;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
//@LogAdvice
@RestController
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("hello")
    @LogAdvice
    public ResultVO hello(Integer x,double y) {
        try {
            testService.hello(x,y);
        }catch (Exception e) {
            log.error("hello error. x:{},y:{}",x,y,e);
        }
         return ResultVO.ofSuccess("ss");
    }
}
