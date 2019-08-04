package com.github.dqqzj.spike.queue.kafka;

import com.alibaba.fastjson.JSONObject;
import com.github.dqqzj.spike.request.SpikeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 10:44
 * @description: 生产者 spring-kafka 2.0 + 依赖JDK8
 * @since JDK 1.8.0_212-b10
 */
@Component
public class KafkaSender {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    /**
     * 发送消息到kafka
     */
    public void sendMessage(String channel, SpikeRequest spikeRequest){
        String message = JSONObject.toJSONString(spikeRequest);
        this.kafkaTemplate.send(channel,message);
    }
}
