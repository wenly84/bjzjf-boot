package site.hansi.framework.banner.core;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.ClassUtils;

import java.util.concurrent.TimeUnit;

/**
 * 项目启动成功后，提供文档相关的地址
 *
 * @author 北京智匠坊
 */
@Slf4j
public class BannerApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        ThreadUtil.execute(() -> {
            ThreadUtil.sleep(1, TimeUnit.SECONDS); // 延迟 1 秒，保证输出到结尾
            // 数据报表
            if (isNotPresent("site.hansi.module.report.framework.security.config.SecurityConfiguration")) {
                System.out.println("[报表模块 bjzjf-module-report - 已禁用]");
            }
            // 工作流
            if (isNotPresent("site.hansi.module.bpm.framework.flowable.config.BpmFlowableConfiguration")) {
                System.out.println("[工作流模块 bjzjf-module-bpm - 已禁用]");
            }
            // 商城系统
            if (isNotPresent("site.hansi.module.trade.framework.web.config.TradeWebConfiguration")) {
                System.out.println("[商城系统 bjzjf-module-mall - 已禁用]");
            }
            // ERP 系统
            if (isNotPresent("site.hansi.module.erp.framework.web.config.ErpWebConfiguration")) {
                System.out.println("[ERP 系统 bjzjf-module-erp - 已禁用]");
            }
            // CRM 系统
            if (isNotPresent("site.hansi.module.crm.framework.web.config.CrmWebConfiguration")) {
                System.out.println("[CRM 系统 bjzjf-module-crm - 已禁用]");
            }
            // 微信公众号
            if (isNotPresent("site.hansi.module.mp.framework.mp.config.MpConfiguration")) {
                System.out.println("[微信公众号 bjzjf-module-mp - 已禁用]");
            }
            // 支付平台
            if (isNotPresent("site.hansi.module.pay.framework.pay.config.PayConfiguration")) {
                System.out.println("[支付系统 bjzjf-module-pay - 已禁用]");
            }
            // AI 大模型
            if (isNotPresent("site.hansi.module.ai.framework.web.config.AiWebConfiguration")) {
                System.out.println("[AI 大模型 bjzjf-module-ai - 已禁用]");
            }
        });
    }

    private static boolean isNotPresent(String className) {
        return !ClassUtils.isPresent(className, ClassUtils.getDefaultClassLoader());
    }

}
