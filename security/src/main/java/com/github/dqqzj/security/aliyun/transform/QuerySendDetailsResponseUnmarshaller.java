package com.github.dqqzj.security.aliyun.transform;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */

import com.aliyuncs.transform.UnmarshallerContext;
import com.github.dqqzj.security.aliyun.model.QuerySendDetailsResponse;

import java.util.ArrayList;
import java.util.List;

public class QuerySendDetailsResponseUnmarshaller {
    public QuerySendDetailsResponseUnmarshaller() {
    }

    public static QuerySendDetailsResponse unmarshall(QuerySendDetailsResponse querySendDetailsResponse, UnmarshallerContext context) {
        querySendDetailsResponse.setRequestId(context.stringValue("QuerySendDetailsResponse.RequestId"));
        querySendDetailsResponse.setCode(context.stringValue("QuerySendDetailsResponse.Code"));
        querySendDetailsResponse.setMessage(context.stringValue("QuerySendDetailsResponse.Message"));
        querySendDetailsResponse.setTotalCount(context.stringValue("QuerySendDetailsResponse.TotalCount"));
        List<QuerySendDetailsResponse.SmsSendDetailDTO> smsSendDetailDTOs = new ArrayList();

        for(int i = 0; i < context.lengthValue("QuerySendDetailsResponse.SmsSendDetailDTOs.Length"); ++i) {
            QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO = new QuerySendDetailsResponse.SmsSendDetailDTO();
            smsSendDetailDTO.setPhoneNum(context.stringValue("QuerySendDetailsResponse.SmsSendDetailDTOs[" + i + "].PhoneNum"));
            smsSendDetailDTO.setSendStatus(context.longValue("QuerySendDetailsResponse.SmsSendDetailDTOs[" + i + "].SendStatus"));
            smsSendDetailDTO.setErrCode(context.stringValue("QuerySendDetailsResponse.SmsSendDetailDTOs[" + i + "].ErrCode"));
            smsSendDetailDTO.setTemplateCode(context.stringValue("QuerySendDetailsResponse.SmsSendDetailDTOs[" + i + "].TemplateCode"));
            smsSendDetailDTO.setContent(context.stringValue("QuerySendDetailsResponse.SmsSendDetailDTOs[" + i + "].Content"));
            smsSendDetailDTO.setSendDate(context.stringValue("QuerySendDetailsResponse.SmsSendDetailDTOs[" + i + "].SendDate"));
            smsSendDetailDTO.setReceiveDate(context.stringValue("QuerySendDetailsResponse.SmsSendDetailDTOs[" + i + "].ReceiveDate"));
            smsSendDetailDTO.setOutId(context.stringValue("QuerySendDetailsResponse.SmsSendDetailDTOs[" + i + "].OutId"));
            smsSendDetailDTOs.add(smsSendDetailDTO);
        }

        querySendDetailsResponse.setSmsSendDetailDTOs(smsSendDetailDTOs);
        return querySendDetailsResponse;
    }
}