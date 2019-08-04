package com.github.dqqzj.seckill.response;

import com.github.dqqzj.seckill.enums.SeckillStatusEnum;
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

	private SeckillStatusEnum seckillStatusEnum;
	private Integer code;
	private String msg;
	private T data;

	public static Result error(SeckillStatusEnum seckillStatusEnum) {
		Result result = new Result();
		result.code = seckillStatusEnum.getCode();
		result.msg = seckillStatusEnum.getMsg();
		return result;
	}
	public static <T>Result error(SeckillStatusEnum seckillStatusEnum, @Nullable T t) {
		Result result = new Result();
		result.code = seckillStatusEnum.getCode();
		result.msg = seckillStatusEnum.getMsg();
		result.data = t;
		return result;
	}

	public static Result ok() {
		Result result = new Result();
		result.code = SeckillStatusEnum.SUCCESS.getCode();
		result.msg = SeckillStatusEnum.SUCCESS.getMsg();
		return result;
	}
	public static <T>Result ok(@Nullable T t) {
		Result result = new Result();
		result.code = SeckillStatusEnum.SUCCESS.getCode();
		result.msg = SeckillStatusEnum.SUCCESS.getMsg();
		result.data = t;
		return result;
	}

}