package com.github.dqqzj.security.aliyun.model;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
import com.aliyuncs.AcsResponse;
import com.aliyuncs.transform.UnmarshallerContext;
import com.github.dqqzj.security.aliyun.transform.SendSmsResponseUnmarshaller;

public class SendSmsResponse extends AcsResponse {
    private String requestId;
    private String bizId;
    private String code;
    private String message;

    public SendSmsResponse() {
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getBizId() {
        return this.bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SendSmsResponse getInstance(UnmarshallerContext context) {
        return SendSmsResponseUnmarshaller.unmarshall(this, context);
    }
}
