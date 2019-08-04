package com.github.dqqzj.seckill.queue.disruptor;

import com.github.dqqzj.seckill.request.SeckillRequest;
import com.github.dqqzj.seckill.service.ISeckillService;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 14:36
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Service
public class DisruptorService implements DisposableBean, InitializingBean {
    private Disruptor<SeckillEvent> disruptor;
    private static final int RING_BUFFER_SIZE = 1024 * 1024;

    @Autowired
    ISeckillService seckillService;
    @Override
    public void destroy() throws Exception {
        disruptor.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        disruptor = new Disruptor<>(new SeckillEventFactory(),RING_BUFFER_SIZE, Executors.defaultThreadFactory(), ProducerType.SINGLE,new BlockingWaitStrategy());
        disruptor.setDefaultExceptionHandler(new DisruptorEventHandlerException());
        disruptor.handleEventsWith(new SeckillEventConsumer(seckillService));
        disruptor.start();
    }

    public void produce(SeckillRequest seckillRequest) {
        RingBuffer<SeckillEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent((event, sequence, request) -> event.setSeckillRequest(request), seckillRequest);
    }
}
