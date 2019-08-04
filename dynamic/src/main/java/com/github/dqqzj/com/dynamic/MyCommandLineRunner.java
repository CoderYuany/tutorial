package com.github.dqqzj.com.dynamic;

import com.github.dqqzj.com.dynamic.mapper.DataSourceMapper;
import com.github.dqqzj.com.dynamic.po.DataSourcePo;
import com.github.dqqzj.com.dynamic.utils.DynamicRoutingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qinzhongjian
 * @date created in 2019-07-03 13:58
 * @description: TODD
 * @since JDK 1.8
 */
@Slf4j
@Component
public class MyCommandLineRunner implements CommandLineRunner {

    @Autowired
    DataSourceMapper dataSourceMapper;

    @Override
    public void run(String... args) throws Exception {
        List<DataSourcePo> dataSourcePos = this.dataSourceMapper.findAll();
        log.info("MyCommandLineRunner#run ----> init dynamicDatasourceProperties from database:[{}] ", dataSourcePos);
        DynamicRoutingUtils.addDynamicRoutingDatasource(dataSourcePos);
    }

}
