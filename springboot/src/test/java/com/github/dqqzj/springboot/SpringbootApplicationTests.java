package com.github.dqqzj.springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.util.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SpringbootApplicationTests {

    @Test
    public void contextLoads() {
        String securityClientCode = "d2c729b663ba42ca9c3372ee62f05743";
        String securityClientSecret= "7ccd499632144be2926b08d23a167091";
        long reqTime = System.currentTimeMillis()/1000;
        System.out.println(reqTime);
        String bMd5Str = "";
        bMd5Str = bMd5Str + securityClientCode + securityClientSecret + "13996821613" + "胡宏伟" + reqTime;
        System.out.println(bMd5Str);
        System.out.println(DigestUtils.md5DigestAsHex(bMd5Str.getBytes()));

    }

}
