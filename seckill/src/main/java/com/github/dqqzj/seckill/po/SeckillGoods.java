package com.github.dqqzj.seckill.po;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Data
@Entity
@Table
public class SeckillGoods implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private Integer stock;
	private Double price;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	@Version
	private Integer version;

}
