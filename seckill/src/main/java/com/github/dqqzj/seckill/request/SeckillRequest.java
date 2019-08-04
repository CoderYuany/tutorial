package com.github.dqqzj.seckill.request;

import lombok.Data;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 10:44
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Data
public class SeckillRequest {
    private Long userId;
    private Long goodsId;
    private Double price;
    private Integer count;
}
