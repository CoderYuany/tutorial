package com.github.dqqzj.spike.service.impl;

import com.github.dqqzj.spike.distributedlock.redis.RedissonLock;
import com.github.dqqzj.spike.enums.SpikeStatusEnum;
import com.github.dqqzj.spike.po.SpikeGoods;
import com.github.dqqzj.spike.po.SpikeOrder;
import com.github.dqqzj.spike.repository.SpikeGoodsRepository;
import com.github.dqqzj.spike.repository.SpikeOrderRepository;
import com.github.dqqzj.spike.request.SpikeRequest;
import com.github.dqqzj.spike.response.Result;
import com.github.dqqzj.spike.service.ISpikeDistributedService;
import com.itstyle.seckill.common.entity.SuccessKilled;
import com.itstyle.seckill.common.enums.SeckillStatEnum;
import com.itstyle.seckill.distributedlock.redis.RedissLockUtil;
import com.itstyle.seckill.distributedlock.zookeeper.ZkLockUtil;
import com.itstyle.seckill.service.ISeckillDistributedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class SpikeDistributedServiceImpl implements ISpikeDistributedService {
	@Autowired
	SpikeGoodsRepository spikeGoodsRepository;

	@Autowired
	SpikeOrderRepository spikeOrderRepository;
	@Override
	@Transactional
	public Result startRedisLock(SpikeRequest spikeRequest) {
		final Result[] result = {Result.ok()};
		Long goodsId = spikeRequest.getGoodsId();
		Integer count = spikeRequest.getCount();
		boolean res=false;
		try {
			res = RedissonLock.tryLock(goodsId.toString(), TimeUnit.SECONDS, 3, 20);
			if(res){
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
			}else{
				result[0] = Result.error(SpikeStatusEnum.MUCH);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(res){
				RedissonLock.unlock(goodsId.toString());
			}
		}
		return result[0];
	}
	@Override
	@Transactional
	public Result startSeckilZksLock(long seckillId, long userId) {
		boolean res=false;
		try {
			//基于redis分布式锁 基本就是上面这个解释 但是 使用zk分布式锁 使用本地zk服务 并发到10000+还是没有问题，谁的锅？
			res = ZkLockUtil.acquire(3,TimeUnit.SECONDS);
			if(res){
				String nativeSql = "SELECT number FROM seckill WHERE seckill_id=?";
				Object object =  dynamicQuery.nativeQueryObject(nativeSql, new Object[]{seckillId});
				Long number =  ((Number) object).longValue();
				if(number>0){
					SuccessKilled killed = new SuccessKilled();
					killed.setSeckillId(seckillId);
					killed.setUserId(userId);
					killed.setState((short)0);
					killed.setCreateTime(new Timestamp(new Date().getTime()));
					dynamicQuery.save(killed);
					nativeSql = "UPDATE seckill  SET number=number-1 WHERE seckill_id=? AND number>0";
					dynamicQuery.nativeExecuteUpdate(nativeSql, new Object[]{seckillId});
				}else{
					return Result.error(SeckillStatEnum.END);
				}
			}else{
			    return Result.error(SeckillStatEnum.MUCH);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(res){//释放锁
				ZkLockUtil.release();
			}
		}
		return Result.ok(SeckillStatEnum.SUCCESS);
	}

	@Override
	@Transactional
	public Result startSeckilLock(long seckillId, long userId, long number) {
		boolean res=false;
		try {
			//尝试获取锁，最多等待3秒，上锁以后10秒自动解锁（实际项目中推荐这种，以防出现死锁）
			res = RedissLockUtil.tryLock(seckillId+"", TimeUnit.SECONDS, 3, 10);
			if(res){
				String nativeSql = "SELECT number FROM seckill WHERE seckill_id=?";
				Object object =  dynamicQuery.nativeQueryObject(nativeSql, new Object[]{seckillId});
				Long count =  ((Number) object).longValue();
				if(count>=number){
					SuccessKilled killed = new SuccessKilled();
					killed.setSeckillId(seckillId);
					killed.setUserId(userId);
					killed.setState((short)0);
					killed.setCreateTime(new Timestamp(new Date().getTime()));
					dynamicQuery.save(killed);
					nativeSql = "UPDATE seckill  SET number=number-? WHERE seckill_id=? AND number>0";
					dynamicQuery.nativeExecuteUpdate(nativeSql, new Object[]{number,seckillId});
				}else{
					return Result.error(SeckillStatEnum.END);
				}
			}else{
				return Result.error(SeckillStatEnum.MUCH);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(res){//释放锁
				RedissLockUtil.unlock(seckillId+"");
			}
		}
		return Result.ok(SeckillStatEnum.SUCCESS);
	}

}
