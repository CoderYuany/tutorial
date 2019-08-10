package com.github.dqqzj.security.aliyun.model;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */

import com.aliyuncs.AcsResponse;
import com.aliyuncs.transform.UnmarshallerContext;
import com.github.dqqzj.security.aliyun.transform.QueryInterSmsIsoInfoResponseUnmarshaller;

import java.util.List;

public class QueryInterSmsIsoInfoResponse extends AcsResponse {
    private String requestId;
    private String code;
    private String message;
    private String totalCount;
    private List<IsoSupportDTO> isoSupportDTOs;

    public QueryInterSmsIsoInfoResponse() {
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public String getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public List<IsoSupportDTO> getIsoSupportDTOs() {
        return this.isoSupportDTOs;
    }

    public void setIsoSupportDTOs(List<IsoSupportDTO> isoSupportDTOs) {
        this.isoSupportDTOs = isoSupportDTOs;
    }

    public QueryInterSmsIsoInfoResponse getInstance(UnmarshallerContext context) {
        return QueryInterSmsIsoInfoResponseUnmarshaller.unmarshall(this, context);
    }

    public static class IsoSupportDTO {
        private String countryName;
        private String countryCode;
        private String isoCode;

        public IsoSupportDTO() {
        }

        public String getCountryName() {
            return this.countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getCountryCode() {
            return this.countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getIsoCode() {
            return this.isoCode;
        }

        public void setIsoCode(String isoCode) {
            this.isoCode = isoCode;
        }
    }
}

