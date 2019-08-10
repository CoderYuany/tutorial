package com.github.dqqzj.security.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.github.dqqzj.security.aliyun.model.QuerySendDetailsRequest;
import com.github.dqqzj.security.aliyun.model.QuerySendDetailsResponse;
import com.github.dqqzj.security.aliyun.model.SendSmsRequest;
import com.github.dqqzj.security.aliyun.model.SendSmsResponse;
import com.github.dqqzj.security.config.GoogleGuavaCache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public class AliyunSMS {
    /**
     * 产品名称:云通信短信API产品,开发者无需替换
     */
    private static final String product = "Dysmsapi";

    /**
     * 产品域名,开发者无需替换
     */
    private static final String domain = "dysmsapi.aliyuncs.com";

    /**
     * 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
     */
    private static final String accessKeyId = "LTAIUrIj6m28n7xw";

    private static final String accessKeySecret = "xTD34HprVLS95r5mJpPvhNkFY97H0w";

    public static String sendSms(String phone) throws ClientException {

        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        // 必填:待发送手机号
        request.setPhoneNumbers(phone);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName("管知网");
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_114395400");
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        // 您的验证码为：${code}，该验证码5分钟内有效，请勿泄露他人。
        String randomCode = getRandomCode();
        GoogleGuavaCache.CACHE.put(phone,randomCode);
        request.setTemplateParam("{\"code\":\"" + randomCode + "\"}");
        // 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        // request.setSmsUpExtendCode("90997");

        // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        // request.setOutId("yourOutId");

        // hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse.getCode();
    }

    public static String querySendDetails(String phone) throws ClientException {

        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");
        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        // 组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        // 必填-号码
        request.setPhoneNumber(phone);
        // 可选-流水号
        // request.setBizId(bizId);
        // 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        // 必填-页大小
        request.setPageSize(10L);
        // 必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        // hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
        QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO = querySendDetailsResponse.getSmsSendDetailDTOs()
                .get(0);
        String content = smsSendDetailDTO.getContent();
        String code = getCodeFromContent(content);
        return code;
    }

    /**
     * java生成一个4位的随机数（验证码）
     *
     * @author Administrator
     */

    public static String getRandomCode() {
        String str = "0123456789";
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            char ch = str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * 利用正则表达式从一个指定的字符串中提取数字串前4位
     */
    public static String getCodeFromContent(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        str = m.replaceAll("").trim().substring(0, 4);
        return str;
    }
}
