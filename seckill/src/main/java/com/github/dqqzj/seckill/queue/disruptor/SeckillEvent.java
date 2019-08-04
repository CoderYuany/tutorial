package com.github.dqqzj.seckill.queue.disruptor;

import com.github.dqqzj.seckill.request.SeckillRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: 事件对象（秒杀事件）
 * @since JDK 1.8.0_212-b10
 */
@Data
public class SeckillEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	private SeckillRequest seckillRequest;
	
}