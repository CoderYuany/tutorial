package com.github.dqqzj.com.dynamic.po;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author qinzhongjian
 * @date created in 2019-07-02 15:47
 * @description: TODD
 * @since JDK 1.8
 */
@Data
public class TenantPo {
    private int id;
    private String username;
    private String connection;
    private int dbId;
    private LocalDate createTime;
}
