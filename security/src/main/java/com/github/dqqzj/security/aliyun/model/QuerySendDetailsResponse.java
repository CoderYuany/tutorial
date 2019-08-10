package com.github.dqqzj.security.aliyun.model;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
import com.aliyuncs.AcsResponse;
import com.aliyuncs.transform.UnmarshallerContext;
import com.github.dqqzj.security.aliyun.transform.QuerySendDetailsResponseUnmarshaller;

import java.util.List;

public class QuerySendDetailsResponse extends AcsResponse {
    private String requestId;
    private String code;
    private String message;
    private String totalCount;
    private List<SmsSendDetailDTO> smsSendDetailDTOs;

    public QuerySendDetailsResponse() {
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

    public List<SmsSendDetailDTO> getSmsSendDetailDTOs() {
        return this.smsSendDetailDTOs;
    }

    public void setSmsSendDetailDTOs(List<SmsSendDetailDTO> smsSendDetailDTOs) {
        this.smsSendDetailDTOs = smsSendDetailDTOs;
    }

    public QuerySendDetailsResponse getInstance(UnmarshallerContext context) {
        return QuerySendDetailsResponseUnmarshaller.unmarshall(this, context);
    }

    public static class SmsSendDetailDTO {
        private String phoneNum;
        private Long sendStatus;
        private String errCode;
        private String templateCode;
        private String content;
        private String sendDate;
        private String receiveDate;
        private String outId;

        public SmsSendDetailDTO() {
        }

        public String getPhoneNum() {
            return this.phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public Long getSendStatus() {
            return this.sendStatus;
        }

        public void setSendStatus(Long sendStatus) {
            this.sendStatus = sendStatus;
        }

        public String getErrCode() {
            return this.errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public String getTemplateCode() {
            return this.templateCode;
        }

        public void setTemplateCode(String templateCode) {
            this.templateCode = templateCode;
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSendDate() {
            return this.sendDate;
        }

        public void setSendDate(String sendDate) {
            this.sendDate = sendDate;
        }

        public String getReceiveDate() {
            return this.receiveDate;
        }

        public void setReceiveDate(String receiveDate) {
            this.receiveDate = receiveDate;
        }

        public String getOutId() {
            return this.outId;
        }

        public void setOutId(String outId) {
            this.outId = outId;
        }
    }
}
