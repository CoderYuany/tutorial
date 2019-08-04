package com.github.dqqzj.seckill.controller;

import com.github.dqqzj.seckill.po.SeckillGoods;
import com.github.dqqzj.seckill.queue.disruptor.DisruptorService;
import com.github.dqqzj.seckill.queue.jvm.SeckillQueue;
import com.github.dqqzj.seckill.request.SeckillRequest;
import com.github.dqqzj.seckill.response.Result;
import com.github.dqqzj.seckill.service.ISeckillService;
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
public class SeckillController {

	private static int corePoolSize = Runtime.getRuntime().availableProcessors();

	/**
	 * 创建线程池  调整队列数 拒绝服务
	 */
	private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000));

	private final CountDownLatch countDownLatch = new CountDownLatch(1000);
	private final Integer skillNum = 1000;
	@Autowired
	private ISeckillService seckillService;
	@Autowired
	DisruptorService disruptorService;

	@ApiOperation(value="查询全部的秒杀售卖商品",nickname="dqqzj")
	@PostMapping("/listSeckillGoods")
	public ResponseEntity listSeckillGoods(){
		List<SeckillGoods> seckillGoods = this.seckillService.listSeckillGoods();
		return ResponseEntity.ok(seckillGoods);
	}
	@ApiOperation(value="秒杀一(最low实现)",nickname="dqqzj")
	@PostMapping("/start")
	public Result start(SeckillRequest seckillRequest){
		final long goodsId =  seckillRequest.getGoodsId();
		final long userId = seckillRequest.getUserId();
		AtomicReference<Result> result = new AtomicReference<>(Result.ok());
		log.info("SeckillController#start 开始秒杀一(会出现超卖) userId: [{}], goodsId: [{}]", userId, goodsId);
		for(int i = 0; i < skillNum; i++){
			Runnable task = () -> {
				result.set(seckillService.startSeckill(seckillRequest));
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
	public Result startLock(SeckillRequest seckillRequest){
		AtomicReference<Result> result = new AtomicReference<>(Result.ok());
		final long goodsId =  seckillRequest.getGoodsId();
		final long userId = seckillRequest.getUserId();
		log.info("SeckillController#startLock 开始秒杀二(正常) userId: [{}], goodsId: [{}]", userId, goodsId);
		for(int i = 0; i < skillNum; i++){
			Runnable task = () -> {
				result.set(seckillService.startSeckillLock(seckillRequest));
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
	public Result startAopLock(SeckillRequest seckillRequest){
		AtomicReference<Result> result = new AtomicReference<>(Result.ok());
		final long goodsId =  seckillRequest.getGoodsId();
		final long userId = seckillRequest.getUserId();
		log.info("SeckillController#startAopLock 开始秒杀三(正常) userId: [{}], goodsId: [{}]", userId, goodsId);
		for(int i = 0; i < skillNum; i++){
			Runnable task = () -> {
				result.set(seckillService.startSeckillAopLock(seckillRequest));
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
	@PostMapping("/startSeckillPessimistic")
	public Result startSeckillPessimistic(SeckillRequest seckillRequest){
		AtomicReference<Result> result = new AtomicReference<>(Result.ok());
		final long goodsId =  seckillRequest.getGoodsId();
		final long userId = seckillRequest.getUserId();
		log.info("SeckillController#startSeckillPessimistic 开始秒杀四(正常) userId: [{}], goodsId: [{}]", userId, goodsId);
		for(int i = 0; i < skillNum; i++){
			Runnable task = new Runnable() {
				@Override
				public void run() {
					result.set(seckillService.startSeckillPessimistic(seckillRequest));
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
	@PostMapping("/startSeckillPessimisticByLockTable")
	public Result startSeckillPessimisticByLockTable(SeckillRequest seckillRequest){
		AtomicReference<Result> result = new AtomicReference<>(Result.ok());
		final long goodsId =  seckillRequest.getGoodsId();
		final long userId = seckillRequest.getUserId();
		log.info("SeckillController#startSeckillPessimisticByLockTable 开始秒杀五(正常、数据库锁最优实现) userId: [{}], goodsId: [{}]", userId, goodsId);

		for(int i = 0; i < skillNum; i++){
			Runnable task = () -> {
				result.set(seckillService.startSeckillPessimisticByLockTable(seckillRequest));
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
	@PostMapping("/startSeckillOptimistic")
	public Result startSeckillOptimistic(SeckillRequest seckillRequest){
		AtomicReference<Result> result = new AtomicReference<>(Result.ok());
		final long goodsId =  seckillRequest.getGoodsId();
		final long userId = seckillRequest.getUserId();
		log.info("SeckillController#startSeckillOptimistic 开始秒杀六(正常、数据库锁最优实现) userId: [{}], goodsId: [{}]", userId, goodsId);

		for(int i = 0; i < skillNum; i++){
			Runnable task = new Runnable() {
				@Override
				public void run() {
					//这里使用的乐观锁、可以自定义抢购数量、如果配置的抢购人数比较少、比如120:100(人数:商品) 会出现少买的情况
					//用户同时进入会出现更新失败的情况
					result.set(seckillService.startSeckillOptimistic(seckillRequest));
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
	public Result startQueue(SeckillRequest seckillRequest){
		final long goodsId =  seckillRequest.getGoodsId();
		final long userId = seckillRequest.getUserId();
		log.info("SeckillController#startQueue 开始秒杀柒(正常) userId: [{}], goodsId: [{}]", userId, goodsId);
		for(int i = 0; i < skillNum; i++){
			Runnable task = () -> {
				try {
					Boolean flag = SeckillQueue.getMailQueue().produce(seckillRequest);
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
	public Result startDisruptorQueue(SeckillRequest seckillRequest){
		final long goodsId =  seckillRequest.getGoodsId();
		final long userId = seckillRequest.getUserId();
		log.info("SeckillController#startDisruptorQueue 开始秒杀八(正常) userId: [{}], goodsId: [{}]", userId, goodsId);

		for(int i = 0; i < skillNum; i++){
			Runnable task = () -> disruptorService.produce(seckillRequest);
			executor.execute(task);
		}
		return Result.ok();
	}
}
