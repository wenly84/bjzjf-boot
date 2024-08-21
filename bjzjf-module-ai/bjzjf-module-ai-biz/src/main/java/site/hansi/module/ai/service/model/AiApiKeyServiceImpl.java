package site.hansi.module.ai.service.model;

import static site.hansi.framework.common.exception.util.ServiceExceptionUtil.exception;
import static site.hansi.module.ai.enums.ErrorCodeConstants.API_KEY_DISABLE;
import static site.hansi.module.ai.enums.ErrorCodeConstants.API_KEY_IMAGE_NODE_FOUND;
import static site.hansi.module.ai.enums.ErrorCodeConstants.API_KEY_MIDJOURNEY_NOT_FOUND;
import static site.hansi.module.ai.enums.ErrorCodeConstants.API_KEY_NOT_EXISTS;
import static site.hansi.module.ai.enums.ErrorCodeConstants.API_KEY_SUNO_NOT_FOUND;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import site.hansi.framework.ai.core.enums.AiPlatformEnum;
import site.hansi.framework.ai.core.factory.AiModelFactory;
import site.hansi.framework.ai.core.model.midjourney.api.MidjourneyApi;
import site.hansi.framework.ai.core.model.suno.api.SunoApi;
import site.hansi.framework.common.enums.CommonStatusEnum;
import site.hansi.framework.common.pojo.PageResult;
import site.hansi.framework.common.util.object.BeanUtils;
import site.hansi.module.ai.controller.admin.model.vo.apikey.AiApiKeyPageReqVO;
import site.hansi.module.ai.controller.admin.model.vo.apikey.AiApiKeySaveReqVO;
import site.hansi.module.ai.dal.dataobject.model.AiApiKeyDO;
import site.hansi.module.ai.dal.mysql.model.AiApiKeyMapper;

/**
 * AI API 密钥 Service 实现类
 *
 * @author 北京智匠坊
 */
@Service
@Validated
public class AiApiKeyServiceImpl implements AiApiKeyService {

    @Resource
    private AiApiKeyMapper apiKeyMapper;

    @Resource
    private AiModelFactory modelFactory;

    @Override
    public Long createApiKey(AiApiKeySaveReqVO createReqVO) {
        // 插入
        AiApiKeyDO apiKey = BeanUtils.toBean(createReqVO, AiApiKeyDO.class);
        apiKeyMapper.insert(apiKey);
        // 返回
        return apiKey.getId();
    }

    @Override
    public void updateApiKey(AiApiKeySaveReqVO updateReqVO) {
        // 校验存在
        validateApiKeyExists(updateReqVO.getId());
        // 更新
        AiApiKeyDO updateObj = BeanUtils.toBean(updateReqVO, AiApiKeyDO.class);
        apiKeyMapper.updateById(updateObj);
    }

    @Override
    public void deleteApiKey(Long id) {
        // 校验存在
        validateApiKeyExists(id);
        // 删除
        apiKeyMapper.deleteById(id);
    }

    private AiApiKeyDO validateApiKeyExists(Long id) {
        AiApiKeyDO apiKey = apiKeyMapper.selectById(id);
        if (apiKey == null) {
            throw exception(API_KEY_NOT_EXISTS);
        }
        return apiKey;
    }

    @Override
    public AiApiKeyDO getApiKey(Long id) {
        return apiKeyMapper.selectById(id);
    }

    @Override
    public AiApiKeyDO validateApiKey(Long id) {
        AiApiKeyDO apiKey = validateApiKeyExists(id);
        if (CommonStatusEnum.isDisable(apiKey.getStatus())) {
            throw exception(API_KEY_DISABLE);
        }
        return apiKey;
    }

    @Override
    public PageResult<AiApiKeyDO> getApiKeyPage(AiApiKeyPageReqVO pageReqVO) {
        return apiKeyMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AiApiKeyDO> getApiKeyList() {
        return apiKeyMapper.selectList();
    }

    // ========== 与 spring-ai 集成 ==========

    @Override
    public ChatModel getChatModel(Long id) {
        AiApiKeyDO apiKey = validateApiKey(id);
        AiPlatformEnum platform = AiPlatformEnum.validatePlatform(apiKey.getPlatform());
        return modelFactory.getOrCreateChatModel(platform, apiKey.getApiKey(), apiKey.getUrl());
    }

    @Override
    public ImageModel getImageModel(AiPlatformEnum platform) {
        AiApiKeyDO apiKey = apiKeyMapper.selectFirstByPlatformAndStatus(platform.getPlatform(), CommonStatusEnum.ENABLE.getStatus());
        if (apiKey == null) {
            throw exception(API_KEY_IMAGE_NODE_FOUND, platform.getName());
        }
        return modelFactory.getOrCreateImageModel(platform, apiKey.getApiKey(), apiKey.getUrl());
    }

    @Override
    public MidjourneyApi getMidjourneyApi() {
        AiApiKeyDO apiKey = apiKeyMapper.selectFirstByPlatformAndStatus(
                AiPlatformEnum.MIDJOURNEY.getPlatform(), CommonStatusEnum.ENABLE.getStatus());
        if (apiKey == null) {
            throw exception(API_KEY_MIDJOURNEY_NOT_FOUND);
        }
        return modelFactory.getOrCreateMidjourneyApi(apiKey.getApiKey(), apiKey.getUrl());
    }

    @Override
    public SunoApi getSunoApi() {
        AiApiKeyDO apiKey = apiKeyMapper.selectFirstByPlatformAndStatus(
                AiPlatformEnum.SUNO.getPlatform(), CommonStatusEnum.ENABLE.getStatus());
        if (apiKey == null) {
            throw exception(API_KEY_SUNO_NOT_FOUND);
        }
        return modelFactory.getOrCreateSunoApi(apiKey.getApiKey(), apiKey.getUrl());
    }
}