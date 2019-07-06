package com.github.dqqzj.com.dynamic.service;

import com.example.demo.annotation.DS;
import com.example.demo.mapper.DataSourceMapper;
import com.example.demo.po.DataSourcePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author qinzhongjian
 * @date created in 2019-07-04 10:54
 * @description: TODD
 * @since JDK 1.8
 */
@DS
@Service
public class DemoServiceImpl implements DemoService {
    @Autowired
    DataSourceMapper dataSourceMapper;
    @Override
    public void add(String tableName) {
         this.dataSourceMapper.createTable(tableName);
    }

    @Override
    public ResponseEntity find() {
        List<DataSourcePO> list = this.dataSourceMapper.findAll();
        return ResponseEntity.ok(list);
    }
}
