package com.github.dqqzj.provider.service;

import com.github.dqqzj.provider.vo.OrderVO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.http.HttpStatus;

/**
 * @author qinzhongjian
 * @date created in 2019/10/8 20:30
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    @Override
    public OrderVO createOrder(String data) {
        return OrderVO.builder()
                .data(data)
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .build();
    }
}
