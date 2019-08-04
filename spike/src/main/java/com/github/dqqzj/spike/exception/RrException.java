package com.github.dqqzj.spike.exception;

import com.github.dqqzj.spike.enums.SpikeStatusEnum;
import lombok.Data;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: 自定义异常
 * @since JDK 1.8.0_212-b10
 */
@Data
public class RrException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private SpikeStatusEnum spikeStatusEnum;
    private String msg;
    
    private Integer code;
    
    public RrException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public RrException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public RrException(SpikeStatusEnum spikeStatusEnum) {
		super(spikeStatusEnum.getMsg());
		this.msg = spikeStatusEnum.getMsg();
		this.code = spikeStatusEnum.getCode();
	}

}
