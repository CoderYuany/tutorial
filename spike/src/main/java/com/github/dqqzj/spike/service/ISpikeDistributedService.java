package com.github.dqqzj.spike.service;

import com.github.dqqzj.spike.request.SpikeRequest;
import com.github.dqqzj.spike.response.Result;

public interface ISpikeDistributedService {

	/**
	 * 秒杀 一  单个商品
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

	/**
	 * 秒杀 二 多个商品
	 * @param spikeRequest
	 * @return
	 */
	Result startLock(SpikeRequest spikeRequest);
	
}
