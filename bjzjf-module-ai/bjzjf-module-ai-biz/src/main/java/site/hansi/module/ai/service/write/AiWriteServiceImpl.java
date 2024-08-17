package site.hansi.module.ai.service.write;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import site.hansi.framework.ai.core.enums.AiPlatformEnum;
import site.hansi.framework.ai.core.util.AiUtils;
import site.hansi.framework.common.pojo.CommonResult;
import site.hansi.framework.common.pojo.PageResult;
import site.hansi.framework.common.util.object.BeanUtils;
import site.hansi.framework.tenant.core.util.TenantUtils;
import site.hansi.module.ai.controller.admin.write.vo.AiWriteGenerateReqVO;
import site.hansi.module.ai.controller.admin.write.vo.AiWritePageReqVO;
import site.hansi.module.ai.dal.dataobject.model.AiChatModelDO;
import site.hansi.module.ai.dal.dataobject.model.AiChatRoleDO;
import site.hansi.module.ai.dal.dataobject.write.AiWriteDO;
import site.hansi.module.ai.dal.mysql.write.AiWriteMapper;
import site.hansi.module.ai.enums.AiChatRoleEnum;
import site.hansi.module.ai.enums.DictTypeConstants;
import site.hansi.module.ai.enums.ErrorCodeConstants;
import site.hansi.module.ai.enums.write.AiWriteTypeEnum;
import site.hansi.module.ai.service.model.AiApiKeyService;
import site.hansi.module.ai.service.model.AiChatModelService;
import site.hansi.module.ai.service.model.AiChatRoleService;
import site.hansi.module.system.api.dict.DictDataApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static site.hansi.framework.common.exception.util.ServiceExceptionUtil.exception;
import static site.hansi.framework.common.pojo.CommonResult.error;
import static site.hansi.framework.common.pojo.CommonResult.success;
import static site.hansi.module.ai.enums.ErrorCodeConstants.WRITE_NOT_EXISTS;

/**
 * AI 写作 Service 实现类
 *
 * @author xiaoxin
 */
@Service
@Slf4j
public class AiWriteServiceImpl implements AiWriteService {

    @Resource
    private AiApiKeyService apiKeyService;
    @Resource
    private AiChatModelService chatModalService;
    @Resource
    private AiChatRoleService chatRoleService;

    @Resource
    private DictDataApi dictDataApi;

    @Resource
    private AiWriteMapper writeMapper;

    @Override
    public Flux<CommonResult<String>> generateWriteContent(AiWriteGenerateReqVO generateReqVO, Long userId) {
        // 1 获取写作模型。尝试获取写作助手角色，没有则使用默认模型
        AiChatRoleDO writeRole = CollUtil.getFirst(
                chatRoleService.getChatRoleListByName(AiChatRoleEnum.AI_WRITE_ROLE.getName()));
        // 1.1 获取写作执行模型
        AiChatModelDO model = getModel(writeRole);
        // 1.2 获取角色设定消息
        String systemMessage = Objects.nonNull(writeRole) && StrUtil.isNotBlank(writeRole.getSystemMessage())
                ? writeRole.getSystemMessage() : AiChatRoleEnum.AI_WRITE_ROLE.getSystemMessage();
        // 1.3 校验平台
        AiPlatformEnum platform = AiPlatformEnum.validatePlatform(model.getPlatform());
        StreamingChatModel chatModel = apiKeyService.getChatModel(model.getKeyId());

        // 2. 插入写作信息
        AiWriteDO writeDO = BeanUtils.toBean(generateReqVO, AiWriteDO.class,
                write -> write.setUserId(userId).setPlatform(platform.getPlatform()).setModel(model.getModel()));
        writeMapper.insert(writeDO);

        // 3.1 构建 Prompt，并进行调用
        Prompt prompt = buildPrompt(generateReqVO, model, systemMessage);
        Flux<ChatResponse> streamResponse = chatModel.stream(prompt);

        // 3.2 流式返回
        StringBuffer contentBuffer = new StringBuffer();
        return streamResponse.map(chunk -> {
            String newContent = chunk.getResult() != null ? chunk.getResult().getOutput().getContent() : null;
            newContent = StrUtil.nullToDefault(newContent, ""); // 避免 null 的 情况
            contentBuffer.append(newContent);
            // 响应结果
            return success(newContent);
        }).doOnComplete(() -> {
            // 忽略租户，因为 Flux 异步无法透传租户
            TenantUtils.executeIgnore(() ->
                    writeMapper.updateById(new AiWriteDO().setId(writeDO.getId()).setGeneratedContent(contentBuffer.toString())));
        }).doOnError(throwable -> {
            log.error("[generateWriteContent][generateReqVO({}) 发生异常]", generateReqVO, throwable);
            // 忽略租户，因为 Flux 异步无法透传租户
            TenantUtils.executeIgnore(() ->
                    writeMapper.updateById(new AiWriteDO().setId(writeDO.getId()).setErrorMessage(throwable.getMessage())));
        }).onErrorResume(error -> Flux.just(error(ErrorCodeConstants.WRITE_STREAM_ERROR)));
    }

    private AiChatModelDO getModel(AiChatRoleDO writeRole) {
        AiChatModelDO model = null;
        if (Objects.nonNull(writeRole) && Objects.nonNull(writeRole.getModelId())) {
            model = chatModalService.getChatModel(writeRole.getModelId());
        }
        if (model == null) {
            model = chatModalService.getRequiredDefaultChatModel();
        }
        Assert.notNull(model, "[AI] 获取不到模型");
        return model;
    }

    private Prompt buildPrompt(AiWriteGenerateReqVO generateReqVO, AiChatModelDO model, String systemMessage) {
        // 1. 构建 message 列表
        List<Message> chatMessages = buildMessages(generateReqVO, systemMessage);
        // 2. 构建 options 对象
        AiPlatformEnum platform = AiPlatformEnum.validatePlatform(model.getPlatform());
        ChatOptions options = AiUtils.buildChatOptions(platform, model.getModel(), model.getTemperature(), model.getMaxTokens());
        return new Prompt(chatMessages, options);
    }

    private List<Message> buildMessages(AiWriteGenerateReqVO generateReqVO, String systemMessage) {
        List<Message> chatMessages = new ArrayList<>();
        if (StrUtil.isNotBlank(systemMessage)) {
            // 1.1 角色设定
            chatMessages.add(new SystemMessage(systemMessage));
        }
        // 1.2 用户输入
        chatMessages.add(new UserMessage(buildUserMessage(generateReqVO)));
        return chatMessages;
    }

    private String buildUserMessage(AiWriteGenerateReqVO generateReqVO) {
        String format = dictDataApi.getDictDataLabel(DictTypeConstants.AI_WRITE_FORMAT, generateReqVO.getFormat());
        String tone = dictDataApi.getDictDataLabel(DictTypeConstants.AI_WRITE_TONE, generateReqVO.getTone());
        String language = dictDataApi.getDictDataLabel(DictTypeConstants.AI_WRITE_LANGUAGE, generateReqVO.getLanguage());
        String length = dictDataApi.getDictDataLabel(DictTypeConstants.AI_WRITE_LENGTH, generateReqVO.getLength());
        // 格式化 prompt
        String prompt = generateReqVO.getPrompt();
        if (Objects.equals(generateReqVO.getType(), AiWriteTypeEnum.WRITING.getType())) {
            return StrUtil.format(AiWriteTypeEnum.WRITING.getPrompt(), prompt, format, tone, language, length);
        } else {
            return StrUtil.format(AiWriteTypeEnum.REPLY.getPrompt(), generateReqVO.getOriginalContent(), prompt, format, tone, language, length);
        }
    }

    @Override
    public void deleteWrite(Long id) {
        // 校验存在
        validateWriteExists(id);
        // 删除
        writeMapper.deleteById(id);
    }

    private void validateWriteExists(Long id) {
        if (writeMapper.selectById(id) == null) {
            throw exception(WRITE_NOT_EXISTS);
        }
    }

    @Override
    public PageResult<AiWriteDO> getWritePage(AiWritePageReqVO pageReqVO) {
        return writeMapper.selectPage(pageReqVO);
    }

}
