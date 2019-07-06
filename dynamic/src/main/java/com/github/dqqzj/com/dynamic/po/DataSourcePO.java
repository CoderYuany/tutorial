package com.github.dqqzj.com.dynamic.po;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author qinzhongjian
 * @date created in 2019-07-02 15:50
 * @description: url may be complete connection like jdbc:mysql://192.168.1.25:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&autoReconnect=true&failOverReadOnly=false
 * @since JDK 1.8
 */
@Data
@Builder
public class DataSourcePO {
    private int id;
    private long active;
    private LocalDate createTime;
    private String name;
    private long initialSize;
    private String password;
    private int port;
    private String url;
    private String username;
    private String driverClassName;
    private String ip;

}
