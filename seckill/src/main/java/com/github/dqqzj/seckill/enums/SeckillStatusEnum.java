package com.github.dqqzj.seckill.enums;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public enum SeckillStatusEnum {

	MUCH(2,"哎呦喂，人也太多了，请稍后！"),
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"未知异常，请联系管理员"),
    PARAMS_REWRITE(-3,"数据篡改");

    private Integer code;
    private String msg;

    SeckillStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static SeckillStatusEnum stateOf(Integer code)
    {
        for (SeckillStatusEnum seckillStatusEnum : values())
        {
            if (seckillStatusEnum.getCode().intValue() == code)
            {
                return seckillStatusEnum;
            }
        }
        return SeckillStatusEnum.INNER_ERROR;
    }
}
