package com.github.dqqzj.spike.service;

import com.github.dqqzj.spike.request.SpikeRequest;
import com.github.dqqzj.spike.response.Result;
/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public interface ISpikeDistributedService {

	/**
	 * 秒杀 一  多个商品？
	 * @param spikeRequest
	 * @return
	 */
	Result startRedisLock(SpikeRequest spikeRequest);
	/**
	 * 秒杀 一  单个商品
	 * @param spikeRequest
	 * @return
	 */
	Result startZksLock(SpikeRequest spikeRequest);

	
}
