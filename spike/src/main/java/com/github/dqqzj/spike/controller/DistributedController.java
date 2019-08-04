package com.github.dqqzj.spike.controller;

import com.github.dqqzj.spike.queue.kafka.KafkaSender;
import com.github.dqqzj.spike.queue.redis.RedisSender;
import com.github.dqqzj.spike.request.SpikeRequest;
import com.github.dqqzj.spike.response.Result;
import com.github.dqqzj.spike.service.ISpikeDistributedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 14:53
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Api(tags ="分布式秒杀")
@Slf4j
@RestController
@RequestMapping("/distributed")
public class DistributedController {
    private final Integer skillNum = 10000;
    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    /**
     * 调整队列数 拒绝服务
     */
    private ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(skillNum));

    @Autowired
    private ISpikeDistributedService spikeDistributedService;
    @Autowired
    private RedisSender redisSender;
    @Autowired
    private KafkaSender kafkaSender;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value="秒杀一(Redis分布式锁)",nickname="dqqzj")
    @PostMapping("/startRedisLock")
    public Result startRedisLock(SpikeRequest spikeRequest){
        AtomicReference<Result> result = new AtomicReference<>(Result.ok());
        for(int i = 0; i < skillNum; i++){
            Runnable task = () -> result.set(spikeDistributedService.startRedisLock(spikeRequest));
            executor.execute(task);
        }
        return result.get();
    }
    @ApiOperation(value="秒杀二(zookeeper分布式锁)",nickname="dqqzj")
    @PostMapping("/startZkLock")
    public Result startZkLock(SpikeRequest spikeRequest){
        AtomicReference<Result> result = new AtomicReference<>(Result.ok());
        for(int i = 0; i < skillNum; i++){
            Runnable task = () ->
               result.set(spikeDistributedService.startZksLock(spikeRequest));
            executor.execute(task);
        }
        return result.get();
    }
    @ApiOperation(value="秒杀三(Redis分布式队列-订阅监听)",nickname="dqqzj")
    @PostMapping("/startRedisQueue")
    public Result startRedisQueue(SpikeRequest spikeRequest){
        AtomicReference<Result> result = new AtomicReference<>(Result.ok());
        for(int i = 0; i < skillNum; i++){
            Runnable task = () -> redisSender.sendChannelMess("spike",spikeRequest);
            executor.execute(task);
        }
        return Result.ok();
    }
    @ApiOperation(value="秒杀四(Kafka分布式队列)",nickname="dqqzj")
    @PostMapping("/startKafkaQueue")
    public Result startKafkaQueue(SpikeRequest spikeRequest){
        AtomicReference<Result> result = new AtomicReference<>(Result.ok());
        for(int i = 0; i < skillNum; i++){
            Runnable task = () -> kafkaSender.sendMessage("spike",spikeRequest);
            executor.execute(task);
        }
        return result.get();
    }

}
