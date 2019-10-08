package com.github.dqqzj.consumer.controller;

import com.github.dqqzj.consumer.service.ConsumerService;
import com.github.dqqzj.provider.service.OrderService;
import com.github.dqqzj.provider.vo.OrderVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinzhongjian
 * @date created in 2019/10/8 23:45
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */

@RestController
public class ConsumerController {
    @Autowired
    ConsumerService consumerService;
    @GetMapping("consumer/{data}")
    public OrderVO consumer(@PathVariable String data) {
       return this.consumerService.consumer(data);
    }
}
