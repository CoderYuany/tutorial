package com.github.dqqzj.spike.response;

import com.github.dqqzj.spike.enums.SpikeStatusEnum;
import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Data
public class Result<T> {

	private SpikeStatusEnum spikeStatusEnum;
	private Integer code;
	private String msg;
	private T data;

	public static Result error(SpikeStatusEnum spikeStatusEnum) {
		Result result = new Result();
		result.code = spikeStatusEnum.getCode();
		result.msg = spikeStatusEnum.getMsg();
		return result;
	}
	public static <T>Result error(SpikeStatusEnum spikeStatusEnum, @Nullable T t) {
		Result result = new Result();
		result.code = spikeStatusEnum.getCode();
		result.msg = spikeStatusEnum.getMsg();
		result.data = t;
		return result;
	}

	public static Result ok() {
		Result result = new Result();
		result.code = SpikeStatusEnum.SUCCESS.getCode();
		result.msg = SpikeStatusEnum.SUCCESS.getMsg();
		return result;
	}
	public static <T>Result ok(@Nullable T t) {
		Result result = new Result();
		result.code = SpikeStatusEnum.SUCCESS.getCode();
		result.msg = SpikeStatusEnum.SUCCESS.getMsg();
		result.data = t;
		return result;
	}

}