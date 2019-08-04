package com.github.dqqzj.spike.po;

import com.github.dqqzj.spike.enums.PayType;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 10:44
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Data
@Builder
@Entity
@Table
public class SpikeOrder implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	@Id
	private Long userId;
	private Long goodsId;
	private Double totalPrice;
	/**
	 * 0: 未支付 1:已支付
	 */
	private Short state;
	private LocalDateTime createTime;
	private PayType payType;
	private Long order;
}
