package com.github.dqqzj.com.dynamic.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinzhongjian
 * @date created in 2019-07-02 20:28
 * @description: TODD
 * @since JDK 1.8
 */
@Data
@ConfigurationProperties(prefix = "spring.dynamic")
public class DynamicDataSourceProperties {

    private List<DynamicDataSource> datasource;

    private ConcurrentHashMap<Object, Object> dataSourceMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        datasource.forEach(v -> {
            DataSource dataSource = DataSourceBuilder
                    .create()
                    .username(v.username)
                    .password(v.password)
                    .url(v.url)
                    .driverClassName(v.driverClassName)
                    .build();
            dataSourceMap.put(v.name, dataSource);
        });

    }

    @Data
    static class DynamicDataSource{
        private String name = "default";
        private String url;
        private String password;
        private String username;
        private String driverClassName;

    }
}