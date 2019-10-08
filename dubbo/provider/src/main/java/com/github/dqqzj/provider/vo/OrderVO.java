package com.github.dqqzj.provider.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author qinzhongjian
 * @date created in 2019/10/8 20:19
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Data
@Builder
@ToString
public class OrderVO implements Serializable {

    private static final long serialVersionUID = 780260662541219554L;
    private Object data;
    private Integer code;
    private String message;

}
