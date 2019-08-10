package com.github.dqqzj.security.aliyun.transform;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */

import com.aliyuncs.transform.UnmarshallerContext;
import com.github.dqqzj.security.aliyun.model.QueryInterSmsIsoInfoResponse;

import java.util.ArrayList;
import java.util.List;


public class QueryInterSmsIsoInfoResponseUnmarshaller {
    public QueryInterSmsIsoInfoResponseUnmarshaller() {
    }

    public static QueryInterSmsIsoInfoResponse unmarshall(QueryInterSmsIsoInfoResponse queryInterSmsIsoInfoResponse, UnmarshallerContext context) {
        queryInterSmsIsoInfoResponse.setRequestId(context.stringValue("QueryInterSmsIsoInfoResponse.RequestId"));
        queryInterSmsIsoInfoResponse.setCode(context.stringValue("QueryInterSmsIsoInfoResponse.Code"));
        queryInterSmsIsoInfoResponse.setMessage(context.stringValue("QueryInterSmsIsoInfoResponse.Message"));
        queryInterSmsIsoInfoResponse.setTotalCount(context.stringValue("QueryInterSmsIsoInfoResponse.TotalCount"));
        List<QueryInterSmsIsoInfoResponse.IsoSupportDTO> isoSupportDTOs = new ArrayList();

        for(int i = 0; i < context.lengthValue("QueryInterSmsIsoInfoResponse.IsoSupportDTOs.Length"); ++i) {
            QueryInterSmsIsoInfoResponse.IsoSupportDTO isoSupportDTO = new QueryInterSmsIsoInfoResponse.IsoSupportDTO();
            isoSupportDTO.setCountryName(context.stringValue("QueryInterSmsIsoInfoResponse.IsoSupportDTOs[" + i + "].CountryName"));
            isoSupportDTO.setCountryCode(context.stringValue("QueryInterSmsIsoInfoResponse.IsoSupportDTOs[" + i + "].CountryCode"));
            isoSupportDTO.setIsoCode(context.stringValue("QueryInterSmsIsoInfoResponse.IsoSupportDTOs[" + i + "].IsoCode"));
            isoSupportDTOs.add(isoSupportDTO);
        }

        queryInterSmsIsoInfoResponse.setIsoSupportDTOs(isoSupportDTOs);
        return queryInterSmsIsoInfoResponse;
    }
}