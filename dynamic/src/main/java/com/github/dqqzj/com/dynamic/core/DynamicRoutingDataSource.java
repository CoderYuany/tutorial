package com.github.dqqzj.com.dynamic.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinzhongjian
 * @date created in 2019-07-02 20:53
 * @description: TODD
 * @since JDK 1.8
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    private static DynamicRoutingDataSource instance;

    private static Map<Object,Object> dataSourceMap = new ConcurrentHashMap<>();

    /**
     * @author qinzhongjian
     * @date 2019-07-03
     * @param: targetDataSources
     * @return void
     * @description: 设置数据源
     */
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        dataSourceMap.putAll(targetDataSources);
        super.afterPropertiesSet();// 必须添加该句，否则新添加数据源无法识别到
    }
    /**
     * @author qinzhongjian
     * @date 2019-07-03
     * @param:
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     * @description: 获取存储已实例的数据源
     */
    public Map<Object, Object> getDataSourceMap() {
        return dataSourceMap;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        log.debug("DynamicRoutingDataSource#determineCurrentLookupKey change current DataSource :[{}]", DynamicDataSourceContextHolder.get());
        return DynamicDataSourceContextHolder.get();
    }
    public static synchronized DynamicRoutingDataSource getInstance(){
        if(instance == null){
            synchronized (DynamicRoutingDataSource.class){
                if(instance == null){
                    instance = new DynamicRoutingDataSource();
                }
            }
        }
        return instance;
    }

}

