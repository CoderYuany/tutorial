package com.github.dqqzj.athena.test;

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
@LogAdvice
@LogForAll
@Service
public class TestService {
    public ResultVO<String> hello(Integer x,double y) {
        throw new RuntimeException("ssssss");
       // System.out.println(JSON.toJSONString(name));
        //return ResultVO.success("success");
    }
}
