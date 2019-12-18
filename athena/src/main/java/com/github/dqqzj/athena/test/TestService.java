package com.github.dqqzj.athena.test;

import com.alibaba.fastjson.JSON;
import com.github.dqqzj.athena.annotation.LogAdvice;
import com.github.dqqzj.athena.annotation.LogForAll;
import com.github.dqqzj.athena.annotation.LogForParams;
import com.github.dqqzj.athena.core.ResultVO;
import com.github.dqqzj.athena.core.enums.ResultCodeEnum;
import com.github.dqqzj.athena.core.exception.BizRuntimeException;
import org.springframework.stereotype.Service;

import java.util.prefs.BackingStoreException;

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
    @LogForParams(logForParams = false)
    public ResultVO<String> hello(Integer x,String y) throws Exception{
        throw new BizRuntimeException(ResultCodeEnum.REQUEST_TIMEOUT);
        //int xx = 9/0;
        //return null;
       // System.out.println(JSON.toJSONString(name));
        //return ResultVO.success("success");
    }
}
