package com.github.dqqzj.spike.service;

import com.github.dqqzj.spike.po.SpikeGoods;
import com.github.dqqzj.spike.request.SpikeRequest;
import com.github.dqqzj.spike.response.Result;

import java.util.List;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public interface ISpikeService {

	/**
	 * 查询全部的秒杀售卖商品
	 * @return
	 */
	List<SpikeGoods> listGoods();



	/**
	 * 秒杀 一、会出现数量错误
	 * @param spikeRequest
	 * @return
	 */
	Result start(SpikeRequest spikeRequest);

	/**
	 * 秒杀 二、程序锁
	 * @param spikeRequest
	 * @return
	 */
	Result startLock(SpikeRequest spikeRequest);
	/**
	 * 秒杀 三、程序锁AOP
	 * @param spikeRequest
	 * @return
	 */
	Result startAopLock(SpikeRequest spikeRequest);

	/**
	 * 秒杀 四、数据库悲观锁
	 * @param spikeRequest
	 * @return
	 */
	Result startPessimistic(SpikeRequest spikeRequest);
	/**
	 * 秒杀 五、数据库悲观锁
	 * @param spikeRequest
	 * @return
	 */
	Result startPessimisticByLockTable(SpikeRequest spikeRequest);

	/**
	 * 秒杀 六、数据库乐观锁
	 * @param spikeRequest
	 * @return
	 */
	Result startOptimistic(SpikeRequest spikeRequest);

    
}
