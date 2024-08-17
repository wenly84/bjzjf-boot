package site.hansi.module.system.framework.sms.core.client.impl;

import cn.hutool.core.collection.ListUtil;
import site.hansi.framework.common.core.KeyValue;
import site.hansi.module.system.framework.sms.core.client.dto.SmsReceiveRespDTO;
import site.hansi.module.system.framework.sms.core.client.dto.SmsSendRespDTO;
import site.hansi.module.system.framework.sms.core.client.dto.SmsTemplateRespDTO;
import site.hansi.module.system.framework.sms.core.property.SmsChannelProperties;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 各种 {@link SmsClientTests  集成测试
 *
 * @author 北京智匠坊
 */
public class SmsClientTests {

    @Test
    @Disabled
    public void testHuaweiSmsClient_sendSms() throws Throwable {
        SmsChannelProperties properties = new SmsChannelProperties()
                .setApiKey("123")
                .setApiSecret("456");
        HuaweiSmsClient client = new HuaweiSmsClient(properties);
        // 准备参数
        Long sendLogId = System.currentTimeMillis();
        String mobile = "15601691323";
        String apiTemplateId = "xx test01";
        List<KeyValue<String, Object>> templateParams = ListUtil.of(new KeyValue<>("code", "1024"));
        // 调用
        SmsSendRespDTO smsSendRespDTO = client.sendSms(sendLogId, mobile, apiTemplateId, templateParams);
        // 打印结果
        System.out.println(smsSendRespDTO);
    }

    // ========== 阿里云 ==========

    @Test
    @Disabled
    public void testAliyunSmsClient_getSmsTemplate() throws Throwable {
        SmsChannelProperties properties = new SmsChannelProperties()
                .setApiKey("")
                .setApiSecret("");
        AliyunSmsClient client = new AliyunSmsClient(properties);
        // 准备参数
        String apiTemplateId = "SMS_207945135";
        // 调用
        SmsTemplateRespDTO template = client.getSmsTemplate(apiTemplateId);
        // 打印结果
        System.out.println(template);
    }

    @Test
    @Disabled
    public void testAliyunSmsClient_sendSms() throws Throwable {
        SmsChannelProperties properties = new SmsChannelProperties()
                .setApiKey("")
                .setApiSecret("")
                .setSignature("Ballcat");
        AliyunSmsClient client = new AliyunSmsClient(properties);
        // 准备参数
        Long sendLogId = System.currentTimeMillis();
        String mobile = "173213154791";
        String apiTemplateId = "SMS_207945135";
        // 调用
        SmsSendRespDTO sendRespDTO = client.sendSms(sendLogId, mobile, apiTemplateId, ListUtil.of(new KeyValue<>("code", "1024")));
        // 打印结果
        System.out.println(sendRespDTO);
    }

    @Test
    @Disabled
    public void testAliyunSmsClient_parseSmsReceiveStatus() {
        SmsChannelProperties properties = new SmsChannelProperties()
                .setApiKey("")
                .setApiSecret("");
        AliyunSmsClient client = new AliyunSmsClient(properties);
        // 准备参数
        String text = "[\n" +
                "  {\n" +
                "    \"phone_number\" : \"13900000001\",\n" +
                "    \"send_time\" : \"2017-01-01 11:12:13\",\n" +
                "    \"report_time\" : \"2017-02-02 22:23:24\",\n" +
                "    \"success\" : true,\n" +
                "    \"err_code\" : \"DELIVERED\",\n" +
                "    \"err_msg\" : \"用户接收成功\",\n" +
                "    \"sms_size\" : \"1\",\n" +
                "    \"biz_id\" : \"12345\",\n" +
                "    \"out_id\" : \"67890\"\n" +
                "  }\n" +
                "]";
        // mock 方法

        // 调用
        List<SmsReceiveRespDTO> statuses = client.parseSmsReceiveStatus(text);
        // 打印结果
        System.out.println(statuses);
    }

}
