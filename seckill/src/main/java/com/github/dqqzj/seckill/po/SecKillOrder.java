package com.github.dqqzj.seckill.po;

import com.github.dqqzj.seckill.enums.PayType;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table
public class SecKillOrder implements Serializable{
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
