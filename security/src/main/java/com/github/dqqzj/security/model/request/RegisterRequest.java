package com.github.dqqzj.security.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author qinzhongjian
 * @date created in 2018/7/27 12:51
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@ApiModel
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterRequest {
    private static final long serialVersionUID = -7019570768557438079L;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\d{11}$", message = "请输入11位手机号")
    @JsonProperty("phone")
    @ApiModelProperty(value = "手机号", example = "13136701494", required = true)
    private String username;

    @NotNull
    @Size(min = 6, max = 20, message = "请输入6~20位的密码")
    @JsonProperty("password")
    @ApiModelProperty(value = "登录密码", example = "123123123", required = true)
    private String password;

    @NotNull
    @Size(min = 4, max = 4,message = "请输入4位短信验证码")
    @JsonProperty("sms")
    @ApiModelProperty(value = "短信验证码", example = "0000", required = true)
    private String sms;

}

