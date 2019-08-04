package com.github.dqqzj.spike.queue.redis;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.dqqzj.spike.request.SpikeRequest;
import com.github.dqqzj.spike.service.ISpikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author qinzhongjian
 * @date created in 2019-08-04 10:44
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Service
public class RedisConsumer {

	@Autowired
	private ISpikeService spikeService;

    public void receiveMessage(String message) {
        //收到通道的消息之后执行秒杀操作(超卖)
		SpikeRequest spikeRequest = JSONObject.parseObject(message, new TypeReference<SpikeRequest>(){});
		this.spikeService.startPessimisticByLockTable(spikeRequest);
    }
}