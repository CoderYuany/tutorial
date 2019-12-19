package com.github.dqqzj.athena.test;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSON;
import com.github.dqqzj.athena.annotation.LogAdvice;
import com.github.dqqzj.athena.annotation.LogForAll;
import com.github.dqqzj.athena.annotation.LogForParams;
import com.github.dqqzj.athena.core.ResultVO;
import org.springframework.stereotype.Service;

/**
 * @author wb-qzj584329
 * @create 2019/12/17 12:59
 * @description TODO
 * @since JDK1.8.0_211-b12
 */
//@LogAdvice
//@LogForAll
@Service
public class TestService {
    //@LogAdvice
    public String hello(Integer x,double y) {
        if (x != null) {
            throw new RuntimeException("ssssss");
        }
        return "ss";
       // System.out.println(JSON.toJSONString(x+y));
        //return ResultVO.success("success");
    }
}
