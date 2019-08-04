package com.github.dqqzj.com.dynamic.controller;

import com.github.dqqzj.com.dynamic.mapper.DataSourceMapper;
import com.github.dqqzj.com.dynamic.po.DataSourcePo;
import com.github.dqqzj.com.dynamic.service.DataSourceService;
import com.github.dqqzj.com.dynamic.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinzhongjian
 * @date created in 2019-07-02 16:00
 * @description: TODD
 * @since JDK 1.8
 */
@RestController
public class DataSourceController {

    @Autowired
    DataSourceMapper dataSourceMapper;

    @Autowired
    DataSourceService dataSourceService;
    @Autowired
    DemoService demoService;

    @GetMapping("table/add")
    public ResponseEntity add() {
        this.demoService.add("DataSourcePO");
        return ResponseEntity.ok("success");
    }
    @GetMapping("find")
    public ResponseEntity find() {
        return this.demoService.find();
    }

    @GetMapping("db/add")
    public void createDatabase() {
        DataSourcePo dataSourcePO = DataSourcePo
                .builder()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .id(0)
                .ip("192.168.1.25")
                .url("jdbc:mysql://192.168.1.25:3306/mysql?useUnicode=true&characterEncoding=utf-8&useSSL=false&autoReconnect=true&failOverReadOnly=false")
                .port(3306)
                .username("root")
                .password("123456")
                .name("slave")
                .build();
        this.dataSourceService.createDatabase(dataSourcePO);
    }

}
