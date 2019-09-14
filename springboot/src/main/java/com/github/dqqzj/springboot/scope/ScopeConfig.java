package com.github.dqqzj.springboot.scope;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 18:53
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@ComponentScan
@Configuration
public class ScopeConfig {
    @Bean
    public static CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
        Map<String, Object> map = new HashMap<>(1);
        map.put("threadScope", new ThreadScope());
        customScopeConfigurer.setScopes(map);
        return customScopeConfigurer;
    }
}
