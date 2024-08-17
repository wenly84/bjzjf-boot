package site.hansi.framework.pay.config;

import site.hansi.framework.pay.core.client.PayClientFactory;
import site.hansi.framework.pay.core.client.impl.PayClientFactoryImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 支付配置类
 *
 * @author 北京智匠坊
 */
@AutoConfiguration
public class BjzjfPayAutoConfiguration {

    @Bean
    public PayClientFactory payClientFactory() {
        return new PayClientFactoryImpl();
    }

}
