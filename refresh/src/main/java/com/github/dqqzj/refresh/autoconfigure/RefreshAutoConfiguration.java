package com.github.dqqzj.refresh.autoconfigure;

import com.github.dqqzj.refresh.ContextRefresher;
import com.github.dqqzj.refresh.scope.RefreshScope;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({RefreshScope.class})
public class RefreshAutoConfiguration {
    public RefreshAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean({RefreshScope.class})
    public static RefreshScope refreshScope() {
        return new RefreshScope();
    }

    @Bean
    @ConditionalOnMissingBean
    public ContextRefresher contextRefresher(ConfigurableApplicationContext context, RefreshScope scope) {
        return new ContextRefresher(context, scope);
    }

}
