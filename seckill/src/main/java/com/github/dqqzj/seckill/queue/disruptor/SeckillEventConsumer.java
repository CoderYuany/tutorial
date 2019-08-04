package com.github.dqqzj.seckill.queue.disruptor;

import com.github.dqqzj.seckill.service.ISeckillService;
import com.lmax.disruptor.EventHandler;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: 消费者(秒杀处理器)
 * @since JDK 1.8.0_212-b10
 */

public class SeckillEventConsumer implements EventHandler<SeckillEvent> {

	ISeckillService seckillService;

	public SeckillEventConsumer(ISeckillService seckillService) {
		this.seckillService = seckillService;
	}
	@Override
	public void onEvent(SeckillEvent seckillEvent, long seq, boolean bool) throws Exception {
		this.seckillService.startSeckill(seckillEvent.getSeckillRequest());
	}
}
