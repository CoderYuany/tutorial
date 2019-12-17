package com.github.dqqzj.athena.test;

import com.github.dqqzj.athena.annotation.LogForAll;
import com.github.dqqzj.athena.annotation.RestApiAdvice;
import com.github.dqqzj.athena.core.ResultVO;
import org.springframework.stereotype.Service;

/**
 * @author wb-qzj584329
 * @create 2019/12/17 12:59
 * @description TODO
 * @since JDK1.8.0_211-b12
 */
@LogForAll(logForParams = true,logForResult = true)
@RestApiAdvice
@Service
public class TestService {
    public ResultVO<String> hello(String name) {
        return ResultVO.success("success");
    }
}
