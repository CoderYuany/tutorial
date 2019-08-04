package com.github.dqqzj.seckill.exception;

import com.github.dqqzj.seckill.enums.SeckillStatusEnum;
import com.github.dqqzj.seckill.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: 异常处理器
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
@RestControllerAdvice
public class RrExceptionHandler {

	@ExceptionHandler(RrException.class)
	public Result handleRrException(RrException e){
		return Result.error(e.getSeckillStatusEnum());
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Result handleDuplicateKeyException(DuplicateKeyException e){
		log.error("RrExceptionHandler#handleDuplicateKeyException 数据库中已存在该记录", e);
		return Result.error(SeckillStatusEnum.REPEAT_KILL, e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public Result handleException(Exception e){
		log.error("RrExceptionHandler#handleException 未知异常", e);
		return Result.error(SeckillStatusEnum.INNER_ERROR, e);
	}
}
