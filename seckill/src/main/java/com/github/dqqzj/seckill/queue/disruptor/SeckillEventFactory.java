package com.github.dqqzj.seckill.queue.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: 事件生成工厂（用来初始化预分配事件对象）
 * @since JDK 1.8.0_212-b10
 */
public class SeckillEventFactory implements EventFactory<SeckillEvent> {
	/**
	 * @author qinzhongjian
	 * @date 2019-08-04
	 * @param:
	 * @return com.github.dqqzj.seckill.queue.disruptor.SeckillEvent
	 * @description: 生成事件对象
	 */
	@Override
	public SeckillEvent newInstance() {
		return new SeckillEvent();
	}

}
