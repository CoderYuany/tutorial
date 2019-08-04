package com.github.dqqzj.spike.service.impl;

import com.github.dqqzj.spike.aop.LimitResource;
import com.github.dqqzj.spike.aop.SpikeLock;
import com.github.dqqzj.spike.enums.SpikeStatusEnum;
import com.github.dqqzj.spike.po.SpikeOrder;
import com.github.dqqzj.spike.po.SpikeGoods;
import com.github.dqqzj.spike.repository.SpikeGoodsRepository;
import com.github.dqqzj.spike.repository.SpikeOrderRepository;
import com.github.dqqzj.spike.request.SpikeRequest;
import com.github.dqqzj.spike.response.Result;
import com.github.dqqzj.spike.service.ISpikeService;
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
public class SpikeServiceImpl implements ISpikeService {
    /**
     * 思考：为什么不用synchronized
     * service 默认是单例的，并发下lock只有一个实例
     */
	private Lock lock = new ReentrantLock(true);
	

	@Autowired
	SpikeGoodsRepository spikeGoodsRepository;
	@Autowired
	SpikeOrderRepository spikeOrderRepository;
	@Override
	public List<SpikeGoods> listGoods() {
		return spikeGoodsRepository.findAll();
	}

	@Override
	@LimitResource(limitType= LimitResource.LimitType.IP)
	@Transactional
	public Result start(SpikeRequest spikeRequest) {
		final Result[] result = {Result.ok()};
		Long goodsId = spikeRequest.getGoodsId();
		Integer count = spikeRequest.getCount();
		//校验库存
		Optional<SpikeGoods> goodsOptional = this.spikeGoodsRepository.findById(goodsId);
		goodsOptional.ifPresent(spikeGoods -> {
			Integer stock = spikeGoods.getStock();
			if(stock > 0 && stock >= count){
				//扣库存
				spikeGoods.setStock(stock--);
				spikeGoodsRepository.save(spikeGoods);
				//创建订单
				SpikeOrder spikeOrder = SpikeOrder.builder()
						.createTime(LocalDateTime.now())
						.goodsId(goodsId)
						.state((short) 0)
						.userId(spikeRequest.getUserId())
						.totalPrice(count* spikeGoods.getPrice())
						.build();
				spikeOrderRepository.save(spikeOrder);
				//支付....
			}else{
				result[0] = Result.error(SpikeStatusEnum.END);
			}
		});
		return result[0];
	}
	@Override
	@Transactional
	public Result startLock(SpikeRequest spikeRequest) {
		 try {
			lock.lock();
			/**
			 * 1)这里、不清楚为啥、总是会被超卖、难道锁不起作用、lock是同一个对象
			 * 2)事物未提交之前，锁已经释放(事物提交是在整个方法执行完)，导致另一个事物读取到了这个事物未提交的数据，也就是传说中的脏读。建议锁上移
			 * 3)给自己留个坑思考：为什么分布式锁(zk和redis)没有问题？(事实是有问题的，由于redis释放锁需要远程通信，不那么明显而已)
			 * 4)数据库默认的事务隔离级别为 可重复读(repeatable-read)，也就不可能出现脏读
			 * 哪个这个级别是只能是幻读了？分析一下：幻读侧重于新增或删除，这里显然不是，那这里到底是什么，给各位大婶留个坑~~~~
			 */
			 Long goodsId = spikeRequest.getGoodsId();
			 Integer count = spikeRequest.getCount();
			 //校验库存
			 Optional<SpikeGoods> goodsOptional = this.spikeGoodsRepository.findById(goodsId);
			 if (goodsOptional.isPresent()) {
			 	SpikeGoods spikeGoods = goodsOptional.get();
				 Integer stock = spikeGoods.getStock();
				 if(stock > 0 && stock >= count){
					 //扣库存
					 spikeGoods.setStock(stock--);
					 spikeGoodsRepository.save(spikeGoods);
					 //创建订单
					 SpikeOrder spikeOrder = SpikeOrder.builder()
							 .createTime(LocalDateTime.now())
							 .goodsId(goodsId)
							 .state((short) 0)
							 .userId(spikeRequest.getUserId())
							 .totalPrice(count* spikeGoods.getPrice())
							 .build();
					 spikeOrderRepository.save(spikeOrder);
					 //支付....
				 } else {
					 return Result.error(SpikeStatusEnum.END);
				 }

			 } else {
			 	return Result.error(SpikeStatusEnum.PARAMS_REWRITE);
			 }
		} catch (Exception e) {
			log.error("SeckillServiceImpl#startSeckillLock 获取锁失败", e);
		}finally {
			lock.unlock();
		}
		return Result.ok();
	}
	@Override
	@SpikeLock
	@Transactional
	public Result startAopLock(SpikeRequest spikeRequest) {
		Long goodsId = spikeRequest.getGoodsId();
		Integer count = spikeRequest.getCount();
		//校验库存
		Optional<SpikeGoods> goodsOptional = this.spikeGoodsRepository.findById(goodsId);
		if (goodsOptional.isPresent()) {
			SpikeGoods spikeGoods = goodsOptional.get();
			Integer stock = spikeGoods.getStock();
			if(stock > 0 && stock >= count){
				//扣库存
				spikeGoods.setStock(stock--);
				spikeGoodsRepository.save(spikeGoods);
				//创建订单
				SpikeOrder spikeOrder = SpikeOrder.builder()
						.createTime(LocalDateTime.now())
						.goodsId(goodsId)
						.state((short) 0)
						.userId(spikeRequest.getUserId())
						.totalPrice(count* spikeGoods.getPrice())
						.build();
				spikeOrderRepository.save(spikeOrder);
				//支付....
			} else {
				return Result.error(SpikeStatusEnum.END);
			}
		} else {
			return Result.error(SpikeStatusEnum.PARAMS_REWRITE);
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
	public Result startPessimistic(SpikeRequest spikeRequest) {
		//单用户抢购一件商品或者多件都没有问题
		Long goodsId = spikeRequest.getGoodsId();
		Integer count = spikeRequest.getCount();
		//校验库存
		SpikeGoods spikeGoods = this.spikeGoodsRepository.queryByLockTable(goodsId);
		Integer stock = spikeGoods.getStock();
			if(stock > 0 && stock >= count){
				//扣库存
				spikeGoods.setStock(stock--);
				spikeGoodsRepository.save(spikeGoods);
				//创建订单
				SpikeOrder spikeOrder = SpikeOrder.builder()
						.createTime(LocalDateTime.now())
						.goodsId(goodsId)
						.state((short) 0)
						.userId(spikeRequest.getUserId())
						.totalPrice(count* spikeGoods.getPrice())
						.build();
				spikeOrderRepository.save(spikeOrder);
				//支付....
			} else {
				return Result.error(SpikeStatusEnum.END);
			}

		return Result.ok();
	}
    /**
     * SHOW STATUS LIKE 'innodb_row_lock%'; 
     * 如果发现锁争用比较严重，如InnoDB_row_lock_waits和InnoDB_row_lock_time_avg的值比较高
     */
	@Override
	@Transactional
	public Result startPessimisticByLockTable(SpikeRequest spikeRequest) {
		//单用户抢购一件商品没有问题、但是抢购多件商品不建议这种写法  UPDATE锁表
		Long goodsId = spikeRequest.getGoodsId();
		Integer count = spikeRequest.getCount();
		Integer updated = this.spikeGoodsRepository.updateByLockTable(count, goodsId);
		if (updated == 1) {
			//创建订单
			SpikeOrder spikeOrder = SpikeOrder.builder()
					.createTime(LocalDateTime.now())
					.goodsId(goodsId)
					.state((short) 0)
					.userId(spikeRequest.getUserId())
					.totalPrice(count* spikeRequest.getPrice())
					.build();
			spikeOrderRepository.save(spikeOrder);
			//支付....
		} else {
			return Result.error(SpikeStatusEnum.END);
		}
		return Result.ok();
	}
	@Override
	@Transactional
	public Result startOptimistic(SpikeRequest spikeRequest) {
		Long goodsId = spikeRequest.getGoodsId();
		Integer count = spikeRequest.getCount();
		Optional<SpikeGoods> goodsOptional = this.spikeGoodsRepository.findById(goodsId);
		if (goodsOptional.isPresent()) {
			SpikeGoods spikeGoods = goodsOptional.get();
			Integer stock = spikeGoods.getStock();
			/**
			 * 剩余的数量应该要大于等于秒杀的数量
			 */
			if(stock > 0 && stock >= count){
				Integer updated = this.spikeGoodsRepository.updateOptimistic(count, goodsId, spikeGoods.getVersion());
				if (updated == 1) {
					//创建订单
					SpikeOrder spikeOrder = SpikeOrder.builder()
							.createTime(LocalDateTime.now())
							.goodsId(goodsId)
							.state((short) 0)
							.userId(spikeRequest.getUserId())
							.totalPrice(count* spikeGoods.getPrice())
							.build();
					spikeOrderRepository.save(spikeOrder);
					//支付....
				} else {
					return Result.error(SpikeStatusEnum.END);
				}

			} else {
				return Result.error(SpikeStatusEnum.END);
			}
		} else {
			return Result.error(SpikeStatusEnum.PARAMS_REWRITE);
		}
		return Result.ok();
	}

}
