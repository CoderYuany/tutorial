package com.github.dqqzj.com.dynamic.utils;

import com.github.dqqzj.com.dynamic.core.DynamicRoutingDataSource;
import com.github.dqqzj.com.dynamic.po.DataSourcePo;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qinzhongjian
 * @date created in 2019-07-02 23:09
 * @description: TODD
 * @since JDK 1.8
 */
public class DynamicRoutingUtils {

    /**
     * @author qinzhongjian
     * @date 2019-07-04
     * @param: dataSourcePO
     * @return void
     * @description:
     * {@link DynamicRoutingDataSource#setTargetDataSources(Map<Object, Object>)}
     */
    public static void addDynamicRoutingDatasource(DataSourcePo dataSourcePO) {
        DataSource dataSource = createDataSource(dataSourcePO);
        DynamicRoutingDataSource dynamicRoutingDataSource = DynamicRoutingDataSource.getInstance();
        Map<Object,Object> dataSourceMap =  new HashMap<>(1);
        dataSourceMap.put(dataSourcePO.getName(), dataSource);
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);
    }

    public static void addDynamicRoutingDatasource(List<DataSourcePo> dataSourceList) {
        DynamicRoutingDataSource dynamicRoutingDataSource = DynamicRoutingDataSource.getInstance();
        Map<Object,Object> dataSourceMap =  new HashMap<>(dataSourceList.size());
        dataSourceList.forEach(v ->
                dataSourceMap.put(v.getName(), createDataSource(v))
        );
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);
    }

    private static DataSource createDataSource(DataSourcePo dataSourcePO) {
        DataSource dataSource = DataSourceBuilder
                .create()
                .driverClassName(dataSourcePO.getDriverClassName())
                .url(dataSourcePO.getUrl())
                .username(dataSourcePO.getUsername())
                .password(dataSourcePO.getPassword())
                .build();
        return dataSource;
    }
}
