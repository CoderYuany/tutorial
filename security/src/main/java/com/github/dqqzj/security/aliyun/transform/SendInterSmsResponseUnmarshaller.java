package com.github.dqqzj.security.aliyun.transform;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */

import com.aliyuncs.transform.UnmarshallerContext;
import com.github.dqqzj.security.aliyun.model.SendInterSmsResponse;

public class SendInterSmsResponseUnmarshaller {
    public SendInterSmsResponseUnmarshaller() {
    }

    public static SendInterSmsResponse unmarshall(SendInterSmsResponse sendInterSmsResponse, UnmarshallerContext context) {
        sendInterSmsResponse.setRequestId(context.stringValue("SendInterSmsResponse.RequestId"));
        sendInterSmsResponse.setBizId(context.stringValue("SendInterSmsResponse.BizId"));
        sendInterSmsResponse.setCode(context.stringValue("SendInterSmsResponse.Code"));
        sendInterSmsResponse.setMessage(context.stringValue("SendInterSmsResponse.Message"));
        return sendInterSmsResponse;
    }
}
