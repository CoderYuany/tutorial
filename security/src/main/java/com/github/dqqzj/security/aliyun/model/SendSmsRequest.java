package com.github.dqqzj.security.aliyun.model;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
import com.aliyuncs.RpcAcsRequest;

public class SendSmsRequest extends RpcAcsRequest<SendSmsResponse> {
    private String templateCode;
    private String phoneNumbers;
    private String signName;
    private String resourceOwnerAccount;
    private String templateParam;
    private Long resourceOwnerId;
    private Long ownerId;
    private String smsUpExtendCode;
    private String outId;

    public SendSmsRequest() {
        super("Dysmsapi", "2017-05-25", "SendSms");
    }

    public String getTemplateCode() {
        return this.templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
        if (templateCode != null) {
            this.putQueryParameter("TemplateCode", templateCode);
        }

    }

    public String getPhoneNumbers() {
        return this.phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        if (phoneNumbers != null) {
            this.putQueryParameter("PhoneNumbers", phoneNumbers);
        }

    }

    public String getSignName() {
        return this.signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
        if (signName != null) {
            this.putQueryParameter("SignName", signName);
        }

    }

    public String getResourceOwnerAccount() {
        return this.resourceOwnerAccount;
    }

    public void setResourceOwnerAccount(String resourceOwnerAccount) {
        this.resourceOwnerAccount = resourceOwnerAccount;
        if (resourceOwnerAccount != null) {
            this.putQueryParameter("ResourceOwnerAccount", resourceOwnerAccount);
        }

    }

    public String getTemplateParam() {
        return this.templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
        if (templateParam != null) {
            this.putQueryParameter("TemplateParam", templateParam);
        }

    }

    public Long getResourceOwnerId() {
        return this.resourceOwnerId;
    }

    public void setResourceOwnerId(Long resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
        if (resourceOwnerId != null) {
            this.putQueryParameter("ResourceOwnerId", resourceOwnerId.toString());
        }

    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        if (ownerId != null) {
            this.putQueryParameter("OwnerId", ownerId.toString());
        }

    }

    public String getSmsUpExtendCode() {
        return this.smsUpExtendCode;
    }

    public void setSmsUpExtendCode(String smsUpExtendCode) {
        this.smsUpExtendCode = smsUpExtendCode;
        if (smsUpExtendCode != null) {
            this.putQueryParameter("SmsUpExtendCode", smsUpExtendCode);
        }

    }

    public String getOutId() {
        return this.outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
        if (outId != null) {
            this.putQueryParameter("OutId", outId);
        }

    }

    public Class<SendSmsResponse> getResponseClass() {
        return SendSmsResponse.class;
    }
}

