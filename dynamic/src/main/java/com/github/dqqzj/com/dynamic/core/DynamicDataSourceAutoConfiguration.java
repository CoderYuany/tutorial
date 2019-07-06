package com.github.dqqzj.com.dynamic.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinzhongjian
 * @date created in 2019-07-02 20:27
 * @description: TODD
 * @since JDK 1.8
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({DynamicDataSourceProperties.class, MybatisProperties.class})
public class DynamicDataSourceAutoConfiguration {
    private MybatisProperties mybatisProperties;
    private DynamicDataSourceProperties dynamicDataSourceProperties;
    public DynamicDataSourceAutoConfiguration(MybatisProperties properties, DynamicDataSourceProperties dynamicDataSourceProperties) {
        this.mybatisProperties = properties;
        this.dynamicDataSourceProperties = dynamicDataSourceProperties;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Autowired DataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        sqlSessionFactoryBean.setMapperLocations(mybatisProperties.resolveMapperLocations());
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
        sqlSessionFactoryBean.setConfiguration(mybatisProperties.getConfiguration());
        return sqlSessionFactoryBean.getObject();
    }
    @Bean
    public SqlSessionTemplate sqlsessiontemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    @Bean
    public PlatformTransactionManager platformTransactionManager(
            @Autowired DataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

    @Bean
    DynamicDataSourceAspect dynamicDataSourceAspect() {
        return new DynamicDataSourceAspect();
    }

    @Bean
    public DynamicRoutingDataSource dynamicDataSource() {
        //获取动态数据库的实例（单例方式）
        DynamicRoutingDataSource dynamicDataSource = DynamicRoutingDataSource.getInstance();

        ConcurrentHashMap<Object, Object> defaultDataSource = dynamicDataSourceProperties.getDataSourceMap();
        dynamicDataSource.setTargetDataSources(defaultDataSource);
        //设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource.get("default"));
        return dynamicDataSource;
    }

}
