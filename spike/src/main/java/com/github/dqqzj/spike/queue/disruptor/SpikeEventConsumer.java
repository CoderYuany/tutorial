package com.github.dqqzj.spike.queue.disruptor;

import com.github.dqqzj.spike.service.ISpikeService;
import com.lmax.disruptor.EventHandler;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: 消费者(秒杀处理器)
 * @since JDK 1.8.0_212-b10
 */

public class SpikeEventConsumer implements EventHandler<SpikeEvent> {

	ISpikeService spikeService;

	public SpikeEventConsumer(ISpikeService seckillService) {
		this.spikeService = seckillService;
	}
	@Override
	public void onEvent(SpikeEvent spikeEvent, long seq, boolean bool) throws Exception {
		this.spikeService.start(spikeEvent.getSpikeRequest());
	}
}
