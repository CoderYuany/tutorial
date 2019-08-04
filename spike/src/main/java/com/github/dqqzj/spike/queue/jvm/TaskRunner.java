package com.github.dqqzj.spike.queue.jvm;

import com.github.dqqzj.spike.request.SpikeRequest;
import com.github.dqqzj.spike.service.ISpikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: 消费秒杀队列
 * @since JDK 1.8.0_212-b10
 */
@Component
public class TaskRunner implements ApplicationRunner {
	
	@Autowired
	ISpikeService seckillService;
	
	@Override
    public void run(ApplicationArguments var) throws Exception{
		while(true){
			//进程内队列
			SpikeRequest spikeRequest = SpikeQueue.getMailQueue().consume();
			if(spikeRequest != null){
				this.seckillService.startSeckill(spikeRequest);
			}
		}
    }
}