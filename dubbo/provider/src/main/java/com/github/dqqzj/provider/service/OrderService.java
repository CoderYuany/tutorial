package com.github.dqqzj.provider.service;

import com.github.dqqzj.provider.vo.OrderVO;

/**
 * @author qinzhongjian
 * @date created in 2019/10/8 20:07
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public interface OrderService {
    OrderVO createOrder(String data);
}
