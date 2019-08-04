package com.github.dqqzj.seckill.service.impl;

import com.github.dqqzj.seckill.aop.LimitResource;
import com.github.dqqzj.seckill.aop.SeckillLock;
import com.github.dqqzj.seckill.enums.SeckillStatusEnum;
import com.github.dqqzj.seckill.po.SecKillOrder;
import com.github.dqqzj.seckill.po.SeckillGoods;
import com.github.dqqzj.seckill.repository.SeckillGoodsRepository;
import com.github.dqqzj.seckill.repository.SeckillOrderRepository;
import com.github.dqqzj.seckill.request.SeckillRequest;
import com.github.dqqzj.seckill.response.Result;
import com.github.dqqzj.seckill.service.ISeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
@Service
public class SeckillServiceImpl implements ISeckillService {
    /**
     * 思考：为什么不用synchronized
     * service 默认是单例的，并发下lock只有一个实例
     */
	private Lock lock = new ReentrantLock(true);
	

	@Autowired
	SeckillGoodsRepository seckillGoodsRepository;
	@Autowired
	SeckillOrderRepository seckillOrderRepository;
	@Override
	public List<SeckillGoods> listSeckillGoods() {
		return seckillGoodsRepository.findAll();
	}

	@Override
	@LimitResource(limitType= LimitResource.LimitType.IP)
	@Transactional
	public Result startSeckill(SeckillRequest seckillRequest) {
		final Result[] result = {Result.ok()};
		Long goodsId = seckillRequest.getGoodsId();
		Integer count = seckillRequest.getCount();
		//校验库存
		Optional<SeckillGoods> goodsOptional = this.seckillGoodsRepository.findById(goodsId);
		goodsOptional.ifPresent(seckillGoods -> {
			Integer stock = seckillGoods.getStock();
			if(stock > 0 && stock >= count){
				//扣库存
				seckillGoods.setStock(stock--);
				seckillGoodsRepository.save(seckillGoods);
				//创建订单
				SecKillOrder secKillOrder = SecKillOrder.builder()
						.createTime(LocalDateTime.now())
						.goodsId(goodsId)
						.state((short) 0)
						.userId(seckillRequest.getUserId())
						.totalPrice(count*seckillGoods.getPrice())
						.build();
				seckillOrderRepository.save(secKillOrder);
				//支付....
			}else{
				result[0] = Result.error(SeckillStatusEnum.END);
			}
		});
		return result[0];
	}
	@Override
	@Transactional
	public Result startSeckillLock(SeckillRequest seckillRequest) {
		 try {
			lock.lock();
			/**
			 * 1)这里、不清楚为啥、总是会被超卖、难道锁不起作用、lock是同一个对象
			 * 2)事物未提交之前，锁已经释放(事物提交是在整个方法执行完)，导致另一个事物读取到了这个事物未提交的数据，也就是传说中的脏读。建议锁上移
			 * 3)给自己留个坑思考：为什么分布式锁(zk和redis)没有问题？(事实是有问题的，由于redis释放锁需要远程通信，不那么明显而已)
			 * 4)数据库默认的事务隔离级别为 可重复读(repeatable-read)，也就不可能出现脏读
			 * 哪个这个级别是只能是幻读了？分析一下：幻读侧重于新增或删除，这里显然不是，那这里到底是什么，给各位大婶留个坑~~~~
			 */
			 Long goodsId = seckillRequest.getGoodsId();
			 Integer count = seckillRequest.getCount();
			 //校验库存
			 Optional<SeckillGoods> goodsOptional = this.seckillGoodsRepository.findById(goodsId);
			 if (goodsOptional.isPresent()) {
			 	SeckillGoods seckillGoods = goodsOptional.get();
				 Integer stock = seckillGoods.getStock();
				 if(stock > 0 && stock >= count){
					 //扣库存
					 seckillGoods.setStock(stock--);
					 seckillGoodsRepository.save(seckillGoods);
					 //创建订单
					 SecKillOrder secKillOrder = SecKillOrder.builder()
							 .createTime(LocalDateTime.now())
							 .goodsId(goodsId)
							 .state((short) 0)
							 .userId(seckillRequest.getUserId())
							 .totalPrice(count*seckillGoods.getPrice())
							 .build();
					 seckillOrderRepository.save(secKillOrder);
					 //支付....
				 } else {
					 return Result.error(SeckillStatusEnum.END);
				 }

			 } else {
			 	return Result.error(SeckillStatusEnum.PARAMS_REWRITE);
			 }
		} catch (Exception e) {
			log.error("SeckillServiceImpl#startSeckillLock 获取锁失败", e);
		}finally {
			lock.unlock();
		}
		return Result.ok();
	}
	@Override
	@SeckillLock
	@Transactional
	public Result startSeckillAopLock(SeckillRequest seckillRequest) {
		Long goodsId = seckillRequest.getGoodsId();
		Integer count = seckillRequest.getCount();
		//校验库存
		Optional<SeckillGoods> goodsOptional = this.seckillGoodsRepository.findById(goodsId);
		if (goodsOptional.isPresent()) {
			SeckillGoods seckillGoods = goodsOptional.get();
			Integer stock = seckillGoods.getStock();
			if(stock > 0 && stock >= count){
				//扣库存
				seckillGoods.setStock(stock--);
				seckillGoodsRepository.save(seckillGoods);
				//创建订单
				SecKillOrder secKillOrder = SecKillOrder.builder()
						.createTime(LocalDateTime.now())
						.goodsId(goodsId)
						.state((short) 0)
						.userId(seckillRequest.getUserId())
						.totalPrice(count*seckillGoods.getPrice())
						.build();
				seckillOrderRepository.save(secKillOrder);
				//支付....
			} else {
				return Result.error(SeckillStatusEnum.END);
			}
		} else {
			return Result.error(SeckillStatusEnum.PARAMS_REWRITE);
		}
		return Result.ok();
	}
	/**
	 * @author qinzhongjian
	 * @date 2019-08-04
	 * @param: seckillRequest
	 * @return com.github.dqqzj.seckill.response.Result
	 * @description: 注意这里 限流注解 可能会出现少买 自行调整
	 */
	@Override
	@LimitResource(limitType= LimitResource.LimitType.IP)
	@Transactional
	public Result startSeckillPessimistic(SeckillRequest seckillRequest) {
		//单用户抢购一件商品或者多件都没有问题
		Long goodsId = seckillRequest.getGoodsId();
		Integer count = seckillRequest.getCount();
		//校验库存
		SeckillGoods seckillGoods = this.seckillGoodsRepository.queryByLockTable(goodsId);
		Integer stock = seckillGoods.getStock();
			if(stock > 0 && stock >= count){
				//扣库存
				seckillGoods.setStock(stock--);
				seckillGoodsRepository.save(seckillGoods);
				//创建订单
				SecKillOrder secKillOrder = SecKillOrder.builder()
						.createTime(LocalDateTime.now())
						.goodsId(goodsId)
						.state((short) 0)
						.userId(seckillRequest.getUserId())
						.totalPrice(count*seckillGoods.getPrice())
						.build();
				seckillOrderRepository.save(secKillOrder);
				//支付....
			} else {
				return Result.error(SeckillStatusEnum.END);
			}

		return Result.ok();
	}
    /**
     * SHOW STATUS LIKE 'innodb_row_lock%'; 
     * 如果发现锁争用比较严重，如InnoDB_row_lock_waits和InnoDB_row_lock_time_avg的值比较高
     */
	@Override
	@Transactional
	public Result startSeckillPessimisticByLockTable(SeckillRequest seckillRequest) {
		//单用户抢购一件商品没有问题、但是抢购多件商品不建议这种写法  UPDATE锁表
		Long goodsId = seckillRequest.getGoodsId();
		Integer count = seckillRequest.getCount();
		Integer updated = this.seckillGoodsRepository.updateByLockTable(count, goodsId);
		if (updated == 1) {
			//创建订单
			SecKillOrder secKillOrder = SecKillOrder.builder()
					.createTime(LocalDateTime.now())
					.goodsId(goodsId)
					.state((short) 0)
					.userId(seckillRequest.getUserId())
					.totalPrice(count*seckillRequest.getPrice())
					.build();
			seckillOrderRepository.save(secKillOrder);
			//支付....
		} else {
			return Result.error(SeckillStatusEnum.END);
		}
		return Result.ok();
	}
	@Override
	@Transactional
	public Result startSeckillOptimistic(SeckillRequest seckillRequest) {
		Long goodsId = seckillRequest.getGoodsId();
		Integer count = seckillRequest.getCount();
		Optional<SeckillGoods> goodsOptional = this.seckillGoodsRepository.findById(goodsId);
		if (goodsOptional.isPresent()) {
			SeckillGoods seckillGoods = goodsOptional.get();
			Integer stock = seckillGoods.getStock();
			/**
			 * 剩余的数量应该要大于等于秒杀的数量
			 */
			if(stock > 0 && stock >= count){
				Integer updated = this.seckillGoodsRepository.updateOptimistic(count, goodsId, seckillGoods.getVersion());
				if (updated == 1) {
					//创建订单
					SecKillOrder secKillOrder = SecKillOrder.builder()
							.createTime(LocalDateTime.now())
							.goodsId(goodsId)
							.state((short) 0)
							.userId(seckillRequest.getUserId())
							.totalPrice(count*seckillGoods.getPrice())
							.build();
					seckillOrderRepository.save(secKillOrder);
					//支付....
				} else {
					return Result.error(SeckillStatusEnum.END);
				}

			} else {
				return Result.error(SeckillStatusEnum.END);
			}
		} else {
			return Result.error(SeckillStatusEnum.PARAMS_REWRITE);
		}
		return Result.ok();
	}

}
