package com.github.dqqzj.refresh.test;

import com.github.dqqzj.refresh.annotation.RefreshScope;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author qinzhongjian
 * @date created in 2019/11/1 22:22
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Data
@ToString
@RefreshScope
@Component
@ConfigurationProperties(prefix = "spring.refresh")
public class Refresh {
    private String content;
}
