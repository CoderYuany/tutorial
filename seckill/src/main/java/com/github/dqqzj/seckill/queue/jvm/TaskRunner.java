package com.github.dqqzj.seckill.queue.jvm;

import com.github.dqqzj.seckill.request.SeckillRequest;
import com.github.dqqzj.seckill.service.ISeckillService;
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
	ISeckillService seckillService;
	
	@Override
    public void run(ApplicationArguments var) throws Exception{
		while(true){
			//进程内队列
			SeckillRequest seckillRequest = SeckillQueue.getMailQueue().consume();
			if(seckillRequest != null){
				this.seckillService.startSeckill(seckillRequest);
			}
		}
    }
}