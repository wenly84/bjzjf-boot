package site.hansi.framework.ai.config;

import site.hansi.framework.ai.core.factory.AiModelFactory;
import site.hansi.framework.ai.core.factory.AiModelFactoryImpl;
import site.hansi.framework.ai.core.model.deepseek.DeepSeekChatModel;
import site.hansi.framework.ai.core.model.deepseek.DeepSeekChatOptions;
import site.hansi.framework.ai.core.model.midjourney.api.MidjourneyApi;
import site.hansi.framework.ai.core.model.suno.api.SunoApi;
import site.hansi.framework.ai.core.model.xinghuo.XingHuoChatModel;
import site.hansi.framework.ai.core.model.xinghuo.XingHuoChatOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * AI 自动配置
 *
 * @author fansili
 */
@AutoConfiguration
@EnableConfigurationProperties(BjzjfAiProperties.class)
@Slf4j
public class BjzjfAiAutoConfiguration {

    @Bean
    public AiModelFactory aiModelFactory() {
        return new AiModelFactoryImpl();
    }

    // ========== 各种 AI Client 创建 ==========

    @Bean
    @ConditionalOnProperty(value = "bjzjf.ai.deepseek.enable", havingValue = "true")
    public DeepSeekChatModel deepSeekChatModel(BjzjfAiProperties bjzjfAiProperties) {
        BjzjfAiProperties.DeepSeekProperties properties = bjzjfAiProperties.getDeepSeek();
        DeepSeekChatOptions options = DeepSeekChatOptions.builder()
                .model(properties.getModel())
                .temperature(properties.getTemperature())
                .maxTokens(properties.getMaxTokens())
                .topP(properties.getTopP())
                .build();
        return new DeepSeekChatModel(properties.getApiKey(), options);
    }

    @Bean
    @ConditionalOnProperty(value = "bjzjf.ai.xinghuo.enable", havingValue = "true")
    public XingHuoChatModel xingHuoChatClient(BjzjfAiProperties bjzjfAiProperties) {
        BjzjfAiProperties.XingHuoProperties properties = bjzjfAiProperties.getXinghuo();
        XingHuoChatOptions options = XingHuoChatOptions.builder()
                .model(properties.getModel())
                .temperature(properties.getTemperature())
                .maxTokens(properties.getMaxTokens())
                .topK(properties.getTopK())
                .build();
        return new XingHuoChatModel(properties.getAppKey(), properties.getSecretKey(), options);
    }

    @Bean
    @ConditionalOnProperty(value = "bjzjf.ai.midjourney.enable", havingValue = "true")
    public MidjourneyApi midjourneyApi(BjzjfAiProperties bjzjfAiProperties) {
        BjzjfAiProperties.MidjourneyProperties config = bjzjfAiProperties.getMidjourney();
        return new MidjourneyApi(config.getBaseUrl(), config.getApiKey(), config.getNotifyUrl());
    }

    @Bean
    @ConditionalOnProperty(value = "bjzjf.ai.suno.enable", havingValue = "true")
    public SunoApi sunoApi(BjzjfAiProperties bjzjfAiProperties) {
        return new SunoApi(bjzjfAiProperties.getSuno().getBaseUrl());
    }

}