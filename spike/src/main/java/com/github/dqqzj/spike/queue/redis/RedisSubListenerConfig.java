package com.github.dqqzj.spike.queue.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
/**
 * @author qinzhongjian
 * @date created in 2019-08-04 10:44
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Configuration
public class RedisSubListenerConfig {

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("spike"));
        return container;
    }
    @Bean
    MessageListenerAdapter listenerAdapter(RedisConsumer redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, "receiveMessage");
    }
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}