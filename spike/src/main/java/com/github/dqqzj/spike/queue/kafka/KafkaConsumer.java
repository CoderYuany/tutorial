package com.github.dqqzj.spike.queue.kafka;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.dqqzj.spike.request.SpikeRequest;
import com.github.dqqzj.spike.service.ISpikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 10:44
 * @description: 消费者 spring-kafka 2.0 + 依赖JDK8
 * @since JDK 1.8.0_212-b10
 */
@Component
public class KafkaConsumer {
	@Autowired
	private ISpikeService spikeService;
	
    /**
     * 监听spike主题,有消息就读取
     * @param message
     */
    @KafkaListener(topics = {"spike"})
    public void receiveMessage(String message){
    	//收到通道的消息之后执行秒杀操作
		SpikeRequest spikeRequest = JSONObject.parseObject(message, new TypeReference<SpikeRequest>(){});
		this.spikeService.startPessimisticByLockTable(spikeRequest);
    }
}