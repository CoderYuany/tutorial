package com.github.dqqzj.security.aliyun.transform;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
import com.aliyuncs.transform.UnmarshallerContext;
import com.github.dqqzj.security.aliyun.model.SendSmsResponse;

public class SendSmsResponseUnmarshaller {
    public SendSmsResponseUnmarshaller() {
    }

    public static SendSmsResponse unmarshall(SendSmsResponse sendSmsResponse, UnmarshallerContext context) {
        sendSmsResponse.setRequestId(context.stringValue("SendSmsResponse.RequestId"));
        sendSmsResponse.setBizId(context.stringValue("SendSmsResponse.BizId"));
        sendSmsResponse.setCode(context.stringValue("SendSmsResponse.Code"));
        sendSmsResponse.setMessage(context.stringValue("SendSmsResponse.Message"));
        return sendSmsResponse;
    }
}