package com.github.dqqzj.consumer.service;

import com.github.dqqzj.provider.service.OrderService;
import com.github.dqqzj.provider.vo.OrderVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * @author qinzhongjian
 * @date created in 2019/10/8 23:54
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Service
public class ConsumerService {
    @Reference(interfaceClass = OrderService.class,url = "dubbo://192.168.1.104:9090/com.github.dqqzj.provider.service.OrderService")
    OrderService orderService;
    public OrderVO consumer(String data) {
        return this.orderService.createOrder(data);
    }
}
