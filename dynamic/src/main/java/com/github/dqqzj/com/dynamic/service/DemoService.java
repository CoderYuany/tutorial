package com.github.dqqzj.com.dynamic.service;

import org.springframework.http.ResponseEntity;

/**
 * @author qinzhongjian
 * @date created in 2019-07-04 10:53
 * @description: TODD
 * @since JDK 1.8
 */
public interface DemoService {
     void add(String tableName);

     ResponseEntity find();

}
