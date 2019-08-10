package com.github.dqqzj.security.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dqqzj.security.enums.RestStatusEnum;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * @author qinzhongjian
 * @date created in 2018/6/25 22:41
 * @since 1.0.0
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestEntity implements Serializable {

    private static final long serialVersionUID = 3550224421750657701L;

    /**
     * [M] 平台状态码
     */
    @JsonProperty("code")
    private Integer code;

    /**
     * [M] 错误信息
     */
    @JsonProperty("msg")
    private String msg;

    /**
     * [C] 详细错误信息
     */
    @JsonProperty("details")
    private Object details;

    public RestEntity(RestStatusEnum restStatusEnum, @Null Object details) {
        this.code = restStatusEnum.getCode();
        this.msg = restStatusEnum.getMessage();
        if (details != null) {
            this.details = details;
        }
    }
}
