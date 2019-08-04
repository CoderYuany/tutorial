package com.github.dqqzj.spike.controller;

import com.github.dqqzj.spike.po.SpikeGoods;
import com.github.dqqzj.spike.queue.disruptor.DisruptorService;
import com.github.dqqzj.spike.queue.jvm.SpikeQueue;
import com.github.dqqzj.spike.request.SpikeRequest;
import com.github.dqqzj.spike.response.Result;
import com.github.dqqzj.spike.service.ISpikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
@Api(tags ="秒杀api")
@RestController
@RequestMapping("/seckill")
public class SpikeController {

	private final static int corePoolSize = Runtime.getRuntime().availableProcessors();
	private final Integer skillNum = 1000;
	/**
	 * 创建线程池  调整队列数 拒绝服务
	 */
	private final ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(skillNum));

	private final CountDownLatch countDownLatch = new CountDownLatch(skillNum);

	@Autowired
	private ISpikeService spikeService;
	@Autowired
	DisruptorService disruptorService;

	@ApiOperation(value="查询全部的秒杀售卖商品",nickname="dqqzj")
	@PostMapping("/listGoods")
	public ResponseEntity listGoods(){
		List<SpikeGoods> spikeGoods = this.spikeService.listGoods();
		return ResponseEntity.ok(spikeGoods);
	}
	@ApiOperation(value="秒杀一(最low实现)",nickname="dqqzj")
	@PostMapping("/start")
	public Result start(SpikeRequest spikeRequest){
		final long goodsId =  spikeRequest.getGoodsId();
		final long userId = spikeRequest.getUserId();
		AtomicReference<Result> result = new AtomicReference<>(Result.ok());
		log.info("SpikeController#start 开始秒杀一(会出现超卖) userId: [{}], goodsId: [{}]", userId, goodsId);
		for(int i = 0; i < skillNum; i++){
			Runnable task = () -> {
				result.set(spikeService.start(spikeRequest));
				countDownLatch.countDown();
			};
			executor.execute(task);
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result.get();
	}
	@ApiOperation(value="秒杀二(程序锁)",nickname="dqqzj")
	@PostMapping("/startLock")
	public Result startLock(SpikeRequest spikeRequest){
		AtomicReference<Result> result = new AtomicReference<>(Result.ok());
		final long goodsId =  spikeRequest.getGoodsId();
		final long userId = spikeRequest.getUserId();
		log.info("SpikeController#startLock 开始秒杀二(正常) userId: [{}], goodsId: [{}]", userId, goodsId);
		for(int i = 0; i < skillNum; i++){
			Runnable task = () -> {
				result.set(spikeService.startLock(spikeRequest));
				countDownLatch.countDown();
			};
			executor.execute(task);
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result.get();
	}
	@ApiOperation(value="秒杀三(AOP程序锁)",nickname="dqqzj")
	@PostMapping("/startAopLock")
	public Result startAopLock(SpikeRequest spikeRequest){
		AtomicReference<Result> result = new AtomicReference<>(Result.ok());
		final long goodsId =  spikeRequest.getGoodsId();
		final long userId = spikeRequest.getUserId();
		log.info("SpikeController#startAopLock 开始秒杀三(正常) userId: [{}], goodsId: [{}]", userId, goodsId);
		for(int i = 0; i < skillNum; i++){
			Runnable task = () -> {
				result.set(spikeService.startAopLock(spikeRequest));
				countDownLatch.countDown();
			};
			executor.execute(task);
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result.get();
	}
	@ApiOperation(value="秒杀四(数据库悲观锁)",nickname="dqqzj")
	@PostMapping("/startPessimistic")
	public Result startPessimistic(SpikeRequest spikeRequest){
		AtomicReference<Result> result = new AtomicReference<>(Result.ok());
		final long goodsId =  spikeRequest.getGoodsId();
		final long userId = spikeRequest.getUserId();
		log.info("SpikeController#startSeckillPessimistic 开始秒杀四(正常) userId: [{}], goodsId: [{}]", userId, goodsId);
		for(int i = 0; i < skillNum; i++){
			Runnable task = new Runnable() {
				@Override
				public void run() {
					result.set(spikeService.startPessimistic(spikeRequest));
					countDownLatch.countDown();
				}
			};
			executor.execute(task);
		}
		try {
			countDownLatch.await();// 等待所有人任务结束
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result.get();
	}
	@ApiOperation(value="秒杀五(数据库悲观锁)",nickname="dqqzj")
	@PostMapping("/startPessimisticByLockTable")
	public Result startPessimisticByLockTable(SpikeRequest spikeRequest){
		AtomicReference<Result> result = new AtomicReference<>(Result.ok());
		final long goodsId =  spikeRequest.getGoodsId();
		final long userId = spikeRequest.getUserId();
		log.info("SpikeController#startSeckillPessimisticByLockTable 开始秒杀五(正常、数据库锁最优实现) userId: [{}], goodsId: [{}]", userId, goodsId);

		for(int i = 0; i < skillNum; i++){
			Runnable task = () -> {
				result.set(spikeService.startPessimisticByLockTable(spikeRequest));
				countDownLatch.countDown();
			};
			executor.execute(task);
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result.get();
	}
	@ApiOperation(value="秒杀六(数据库乐观锁)",nickname="dqqzj")
	@PostMapping("/startOptimistic")
	public Result startOptimistic(SpikeRequest spikeRequest){
		AtomicReference<Result> result = new AtomicReference<>(Result.ok());
		final long goodsId =  spikeRequest.getGoodsId();
		final long userId = spikeRequest.getUserId();
		log.info("SpikeController#startSeckillOptimistic 开始秒杀六(正常、数据库锁最优实现) userId: [{}], goodsId: [{}]", userId, goodsId);

		for(int i = 0; i < skillNum; i++){
			Runnable task = new Runnable() {
				@Override
				public void run() {
					//这里使用的乐观锁、可以自定义抢购数量、如果配置的抢购人数比较少、比如120:100(人数:商品) 会出现少买的情况
					//用户同时进入会出现更新失败的情况
					result.set(spikeService.startOptimistic(spikeRequest));
					countDownLatch.countDown();
				}
			};
			executor.execute(task);
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Result.ok();
	}
	@ApiOperation(value="秒杀柒(进程内队列)",nickname="dqqzj")
	@PostMapping("/startQueue")
	public Result startQueue(SpikeRequest spikeRequest){
		final long goodsId =  spikeRequest.getGoodsId();
		final long userId = spikeRequest.getUserId();
		log.info("SpikeController#startQueue 开始秒杀柒(正常) userId: [{}], goodsId: [{}]", userId, goodsId);
		for(int i = 0; i < skillNum; i++){
			Runnable task = () -> {
				try {
					Boolean flag = SpikeQueue.getMailQueue().produce(spikeRequest);
					if(flag){
						log.info("秒杀成功: userId: [{}], goodsId: [{}]", userId, goodsId);
					}else{
						log.info("秒杀失败: userId: [{}], goodsId: [{}]", userId, goodsId);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					log.info("秒杀失败: userId: [{}], goodsId: [{}]", userId, goodsId);
				}
			};
			executor.execute(task);
		}
		return Result.ok();
	}
	@ApiOperation(value="秒杀柒(Disruptor队列)",nickname="dqqzj")
	@PostMapping("/startDisruptorQueue")
	public Result startDisruptorQueue(SpikeRequest spikeRequest){
		final long goodsId =  spikeRequest.getGoodsId();
		final long userId = spikeRequest.getUserId();
		log.info("SpikeController#startDisruptorQueue 开始秒杀八(正常) userId: [{}], goodsId: [{}]", userId, goodsId);

		for(int i = 0; i < skillNum; i++){
			Runnable task = () -> disruptorService.produce(spikeRequest);
			executor.execute(task);
		}
		return Result.ok();
	}
}
