package com.github.dqqzj.seckill.service;

import com.github.dqqzj.seckill.po.SeckillGoods;
import com.github.dqqzj.seckill.request.SeckillRequest;
import com.github.dqqzj.seckill.response.Result;

import java.util.List;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public interface ISeckillService {

	/**
	 * 查询全部的秒杀售卖商品
	 * @return
	 */
	List<SeckillGoods> listSeckillGoods();



	/**
	 * 秒杀 一、会出现数量错误
	 * @param seckillRequest
	 * @return
	 */
	Result startSeckill(SeckillRequest seckillRequest);

	/**
	 * 秒杀 二、程序锁
	 * @param seckillRequest
	 * @return
	 */
	Result startSeckillLock(SeckillRequest seckillRequest);
	/**
	 * 秒杀 三、程序锁AOP
	 * @param seckillRequest
	 * @return
	 */
	Result startSeckillAopLock(SeckillRequest seckillRequest);

	/**
	 * 秒杀 四、数据库悲观锁
	 * @param seckillRequest
	 * @return
	 */
	Result startSeckillPessimistic(SeckillRequest seckillRequest);
	/**
	 * 秒杀 五、数据库悲观锁
	 * @param seckillRequest
	 * @return
	 */
	Result startSeckillPessimisticByLockTable(SeckillRequest seckillRequest);

	/**
	 * 秒杀 六、数据库乐观锁
	 * @param seckillRequest
	 * @return
	 */
	Result startSeckillOptimistic(SeckillRequest seckillRequest);

    
}
