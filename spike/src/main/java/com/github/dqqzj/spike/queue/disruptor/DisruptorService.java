package com.github.dqqzj.spike.queue.disruptor;

import com.github.dqqzj.spike.request.SpikeRequest;
import com.github.dqqzj.spike.service.ISpikeService;
import com.lmax.disruptor.BlockingWaitStrategy;
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
    private Disruptor<SpikeEvent> disruptor;
    private static final int RING_BUFFER_SIZE = 1024 * 1024;

    @Autowired
    ISpikeService seckillService;
    @Override
    public void destroy() throws Exception {
        disruptor.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        disruptor = new Disruptor<>(new SpikeEventFactory(),RING_BUFFER_SIZE, Executors.defaultThreadFactory(), ProducerType.SINGLE,new BlockingWaitStrategy());
        disruptor.setDefaultExceptionHandler(new DisruptorEventHandlerException());
        disruptor.handleEventsWith(new SpikeEventConsumer(seckillService));
        disruptor.start();
    }

    public void produce(SpikeRequest spikeRequest) {
        RingBuffer<SpikeEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent((event, sequence, request) -> event.setSpikeRequest(request), spikeRequest);
    }
}
