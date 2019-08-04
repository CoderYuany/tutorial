package com.github.dqqzj.spike.queue.redis;

import com.alibaba.fastjson.JSONObject;
import com.github.dqqzj.spike.request.SpikeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 10:44
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Service
public class RedisSender {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * @author qinzhongjian
     * @date 2019-08-04
     * @param: channel
     * @param: message
     * @return void
     * @description: 向通道发送消息的方法
     */
    public void sendChannelMess(String channel, SpikeRequest spikeRequest) {
        String message = JSONObject.toJSONString(spikeRequest);
        stringRedisTemplate.convertAndSend(channel, message);
    }
}
