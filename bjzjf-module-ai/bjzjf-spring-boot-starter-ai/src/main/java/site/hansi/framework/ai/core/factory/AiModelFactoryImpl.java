package site.hansi.framework.ai.core.factory;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import site.hansi.framework.ai.config.BjzjfAiAutoConfiguration;
import site.hansi.framework.ai.config.BjzjfAiProperties;
import site.hansi.framework.ai.core.enums.AiPlatformEnum;
import site.hansi.framework.ai.core.model.deepseek.DeepSeekChatModel;
import site.hansi.framework.ai.core.model.midjourney.api.MidjourneyApi;
import site.hansi.framework.ai.core.model.suno.api.SunoApi;
import site.hansi.framework.ai.core.model.xinghuo.XingHuoChatModel;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import org.springframework.ai.autoconfigure.ollama.OllamaAutoConfiguration;
import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;
import org.springframework.ai.autoconfigure.qianfan.QianFanAutoConfiguration;
import org.springframework.ai.autoconfigure.qianfan.QianFanChatProperties;
import org.springframework.ai.autoconfigure.qianfan.QianFanConnectionProperties;
import org.springframework.ai.autoconfigure.qianfan.QianFanImageProperties;
import org.springframework.ai.autoconfigure.zhipuai.ZhiPuAiAutoConfiguration;
import org.springframework.ai.autoconfigure.zhipuai.ZhiPuAiChatProperties;
import org.springframework.ai.autoconfigure.zhipuai.ZhiPuAiConnectionProperties;
import org.springframework.ai.autoconfigure.zhipuai.ZhiPuAiImageProperties;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.ai.qianfan.QianFanChatModel;
import org.springframework.ai.qianfan.QianFanImageModel;
import org.springframework.ai.qianfan.api.QianFanApi;
import org.springframework.ai.qianfan.api.QianFanImageApi;
import org.springframework.ai.stabilityai.StabilityAiImageModel;
import org.springframework.ai.stabilityai.api.StabilityAiApi;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiImageModel;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.ai.zhipuai.api.ZhiPuAiImageApi;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * AI Model 模型工厂的实现类
 *
 * @author 北京智匠坊
 */
public class AiModelFactoryImpl implements AiModelFactory {

    @Override
    public ChatModel getOrCreateChatModel(AiPlatformEnum platform, String apiKey, String url) {
        String cacheKey = buildClientCacheKey(ChatModel.class, platform, apiKey, url);
        return Singleton.get(cacheKey, (Func0<ChatModel>) () -> {
            //noinspection EnhancedSwitchMigration
            switch (platform) {
                case YI_YAN:
                    return buildYiYanChatModel(apiKey);
                case DEEP_SEEK:
                    return buildDeepSeekChatModel(apiKey);
                case ZHI_PU:
                    return buildZhiPuChatModel(apiKey, url);
                case XING_HUO:
                    return buildXingHuoChatModel(apiKey);
                case OPENAI:
                    return buildOpenAiChatModel(apiKey, url);
                case OLLAMA:
                    return buildOllamaChatModel(url);
                default:
                    throw new IllegalArgumentException(StrUtil.format("未知平台({})", platform));
            }
        });
    }

    @Override
    public ChatModel getDefaultChatModel(AiPlatformEnum platform) {
        //noinspection EnhancedSwitchMigration
        switch (platform) {
            case YI_YAN:
                return SpringUtil.getBean(QianFanChatModel.class);
            case DEEP_SEEK:
                return SpringUtil.getBean(DeepSeekChatModel.class);
            case ZHI_PU:
                return SpringUtil.getBean(ZhiPuAiChatModel.class);
            case XING_HUO:
                return SpringUtil.getBean(XingHuoChatModel.class);
            case OPENAI:
                return SpringUtil.getBean(OpenAiChatModel.class);
            case OLLAMA:
                return SpringUtil.getBean(OllamaChatModel.class);
            default:
                throw new IllegalArgumentException(StrUtil.format("未知平台({})", platform));
        }
    }

    @Override
    public ImageModel getDefaultImageModel(AiPlatformEnum platform) {
        //noinspection EnhancedSwitchMigration
        switch (platform) {
            case YI_YAN:
                return SpringUtil.getBean(QianFanImageModel.class);
            case ZHI_PU:
                return SpringUtil.getBean(ZhiPuAiImageModel.class);
            case OPENAI:
                return SpringUtil.getBean(OpenAiImageModel.class);
            case STABLE_DIFFUSION:
                return SpringUtil.getBean(StabilityAiImageModel.class);
            default:
                throw new IllegalArgumentException(StrUtil.format("未知平台({})", platform));
        }
    }

    @Override
    public ImageModel getOrCreateImageModel(AiPlatformEnum platform, String apiKey, String url) {
        //noinspection EnhancedSwitchMigration
        switch (platform) {
            case YI_YAN:
                return buildQianFanImageModel(apiKey);
            case ZHI_PU:
                return buildZhiPuAiImageModel(apiKey, url);
            case OPENAI:
                return buildOpenAiImageModel(apiKey, url);
            case STABLE_DIFFUSION:
                return buildStabilityAiImageModel(apiKey, url);
            default:
                throw new IllegalArgumentException(StrUtil.format("未知平台({})", platform));
        }
    }

    @Override
    public MidjourneyApi getOrCreateMidjourneyApi(String apiKey, String url) {
        String cacheKey = buildClientCacheKey(MidjourneyApi.class, AiPlatformEnum.MIDJOURNEY.getPlatform(), apiKey, url);
        return Singleton.get(cacheKey, (Func0<MidjourneyApi>) () -> {
            BjzjfAiProperties.MidjourneyProperties properties = SpringUtil.getBean(BjzjfAiProperties.class).getMidjourney();
            return new MidjourneyApi(url, apiKey, properties.getNotifyUrl());
        });
    }

    @Override
    public SunoApi getOrCreateSunoApi(String apiKey, String url) {
        String cacheKey = buildClientCacheKey(SunoApi.class, AiPlatformEnum.SUNO.getPlatform(), apiKey, url);
        return Singleton.get(cacheKey, (Func0<SunoApi>) () -> new SunoApi(url));
    }

    private static String buildClientCacheKey(Class<?> clazz, Object... params) {
        if (ArrayUtil.isEmpty(params)) {
            return clazz.getName();
        }
        return StrUtil.format("{}#{}", clazz.getName(), ArrayUtil.join(params, "_"));
    }

    /**
     * 可参考 {@link QianFanAutoConfiguration#qianFanChatModel(QianFanConnectionProperties, QianFanChatProperties, RestClient.Builder, RetryTemplate, ResponseErrorHandler)}
     */
    private static QianFanChatModel buildYiYanChatModel(String key) {
        List<String> keys = StrUtil.split(key, '|');
        Assert.equals(keys.size(), 2, "YiYanChatClient 的密钥需要 (appKey|secretKey) 格式");
        String appKey = keys.get(0);
        String secretKey = keys.get(1);
        QianFanApi qianFanApi = new QianFanApi(appKey, secretKey);
        return new QianFanChatModel(qianFanApi);
    }

    /**
     * 可参考 {@link QianFanAutoConfiguration#qianFanImageModel(QianFanConnectionProperties, QianFanImageProperties, RestClient.Builder, RetryTemplate, ResponseErrorHandler)}
     */
    private QianFanImageModel buildQianFanImageModel(String key) {
        List<String> keys = StrUtil.split(key, '|');
        Assert.equals(keys.size(), 2, "YiYanChatClient 的密钥需要 (appKey|secretKey) 格式");
        String appKey = keys.get(0);
        String secretKey = keys.get(1);
        QianFanImageApi qianFanApi = new QianFanImageApi(appKey, secretKey);
        return new QianFanImageModel(qianFanApi);
    }

    /**
     * 可参考 {@link BjzjfAiAutoConfiguration#deepSeekChatModel(BjzjfAiProperties)}
     */
    private static DeepSeekChatModel buildDeepSeekChatModel(String apiKey) {
        return new DeepSeekChatModel(apiKey);
    }

    /**
     * 可参考 {@link ZhiPuAiAutoConfiguration#zhiPuAiChatModel(
     * ZhiPuAiConnectionProperties, ZhiPuAiChatProperties, RestClient.Builder, List, FunctionCallbackContext, RetryTemplate, ResponseErrorHandler)}
     */
    private ZhiPuAiChatModel buildZhiPuChatModel(String apiKey, String url) {
        url = StrUtil.blankToDefault(url, ZhiPuAiConnectionProperties.DEFAULT_BASE_URL);
        ZhiPuAiApi zhiPuAiApi = new ZhiPuAiApi(url, apiKey);
        return new ZhiPuAiChatModel(zhiPuAiApi);
    }

    /**
     * 可参考 {@link ZhiPuAiAutoConfiguration#zhiPuAiImageModel(
     * ZhiPuAiConnectionProperties, ZhiPuAiImageProperties, RestClient.Builder, RetryTemplate, ResponseErrorHandler)}
     */
    private ZhiPuAiImageModel buildZhiPuAiImageModel(String apiKey, String url) {
        url = StrUtil.blankToDefault(url, ZhiPuAiConnectionProperties.DEFAULT_BASE_URL);
        //ZhiPuAiImageApi zhiPuAiApi = new ZhiPuAiImageApi(url, apiKey, RestClient.builder());
        return new ZhiPuAiImageModel(null);
    }

    /**
     * 可参考 {@link BjzjfAiAutoConfiguration#xingHuoChatClient(BjzjfAiProperties)}
     */
    private static XingHuoChatModel buildXingHuoChatModel(String key) {
        List<String> keys = StrUtil.split(key, '|');
        Assert.equals(keys.size(), 3, "XingHuoChatClient 的密钥需要 (appid|appKey|secretKey) 格式");
        String appKey = keys.get(1);
        String secretKey = keys.get(2);
        return new XingHuoChatModel(appKey, secretKey);
    }

    /**
     * 可参考 {@link OpenAiAutoConfiguration} 
     */
    private static OpenAiChatModel buildOpenAiChatModel(String openAiToken, String url) {
        url = StrUtil.blankToDefault(url, "https://api.openai.com");
        OpenAiApi openAiApi = new OpenAiApi(url, openAiToken);
        return new OpenAiChatModel(openAiApi);
    }

    /**
     * 可参考 {@link OpenAiAutoConfiguration}
     */
    private OpenAiImageModel buildOpenAiImageModel(String openAiToken, String url) {
        url = StrUtil.blankToDefault(url, "https://api.openai.com");
        //OpenAiImageApi openAiApi = new OpenAiImageApi(url, openAiToken, RestClient.builder());
        return new OpenAiImageModel(null);
    }

    /**
     * 可参考 {@link OllamaAutoConfiguration}
     */
    private static OllamaChatModel buildOllamaChatModel(String url) {
        OllamaApi ollamaApi = new OllamaApi(url);
        return new OllamaChatModel(ollamaApi);
    }

    private StabilityAiImageModel buildStabilityAiImageModel(String apiKey, String url) {
        url = StrUtil.blankToDefault(url, StabilityAiApi.DEFAULT_BASE_URL);
        StabilityAiApi stabilityAiApi = new StabilityAiApi(apiKey, StabilityAiApi.DEFAULT_IMAGE_MODEL, url);
        return new StabilityAiImageModel(stabilityAiApi);
    }

}
