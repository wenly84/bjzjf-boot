package site.hansi.module.ai.service.mindmap;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import site.hansi.framework.ai.core.enums.AiPlatformEnum;
import site.hansi.framework.ai.core.util.AiUtils;
import site.hansi.framework.common.pojo.CommonResult;
import site.hansi.framework.common.util.object.BeanUtils;
import site.hansi.framework.tenant.core.util.TenantUtils;
import site.hansi.module.ai.controller.admin.mindmap.vo.AiMindMapGenerateReqVO;
import site.hansi.module.ai.dal.dataobject.mindmap.AiMindMapDO;
import site.hansi.module.ai.dal.dataobject.model.AiChatModelDO;
import site.hansi.module.ai.dal.dataobject.model.AiChatRoleDO;
import site.hansi.module.ai.dal.mysql.mindmap.AiMindMapMapper;
import site.hansi.module.ai.enums.AiChatRoleEnum;
import site.hansi.module.ai.enums.ErrorCodeConstants;
import site.hansi.module.ai.service.model.AiApiKeyService;
import site.hansi.module.ai.service.model.AiChatModelService;
import site.hansi.module.ai.service.model.AiChatRoleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static site.hansi.framework.common.pojo.CommonResult.error;
import static site.hansi.framework.common.pojo.CommonResult.success;

/**
 * AI 思维导图 Service 实现类
 *
 * @author xiaoxin
 */
@Service
@Slf4j
public class AiMindMapServiceImpl implements AiMindMapService {

    @Resource
    private AiApiKeyService apiKeyService;
    @Resource
    private AiChatModelService chatModalService;
    @Resource
    private AiChatRoleService chatRoleService;

    @Resource
    private AiMindMapMapper mindMapMapper;

    @Override
    public Flux<CommonResult<String>> generateMindMap(AiMindMapGenerateReqVO generateReqVO, Long userId) {
        // 1. 获取脑图模型。尝试获取思维导图助手角色，如果没有则使用默认模型
        AiChatRoleDO role = CollUtil.getFirst(
                chatRoleService.getChatRoleListByName(AiChatRoleEnum.AI_MIND_MAP_ROLE.getName()));
        // 1.1 获取脑图执行模型
        AiChatModelDO model = getModel(role);
        // 1.2 获取角色设定消息
        String systemMessage = role != null && StrUtil.isNotBlank(role.getSystemMessage())
                ? role.getSystemMessage() : AiChatRoleEnum.AI_MIND_MAP_ROLE.getSystemMessage();
        // 1.3 校验平台
        AiPlatformEnum platform = AiPlatformEnum.validatePlatform(model.getPlatform());
        ChatModel chatModel = apiKeyService.getChatModel(model.getKeyId());

        // 2. 插入思维导图信息
        AiMindMapDO mindMapDO = BeanUtils.toBean(generateReqVO, AiMindMapDO.class,
                mindMap -> mindMap.setUserId(userId).setModel(model.getModel()).setPlatform(platform.getPlatform()));
        mindMapMapper.insert(mindMapDO);

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
                    mindMapMapper.updateById(new AiMindMapDO().setId(mindMapDO.getId()).setGeneratedContent(contentBuffer.toString())));
        }).doOnError(throwable -> {
            log.error("[generateWriteContent][generateReqVO({}) 发生异常]", generateReqVO, throwable);
            // 忽略租户，因为 Flux 异步无法透传租户
            TenantUtils.executeIgnore(() ->
                    mindMapMapper.updateById(new AiMindMapDO().setId(mindMapDO.getId()).setErrorMessage(throwable.getMessage())));
        }).onErrorResume(error -> Flux.just(error(ErrorCodeConstants.WRITE_STREAM_ERROR)));

    }

    private Prompt buildPrompt(AiMindMapGenerateReqVO generateReqVO, AiChatModelDO model, String systemMessage) {
        // 1. 构建 message 列表
        List<Message> chatMessages = buildMessages(generateReqVO, systemMessage);
        // 2. 构建 options 对象
        AiPlatformEnum platform = AiPlatformEnum.validatePlatform(model.getPlatform());
        ChatOptions options = AiUtils.buildChatOptions(platform, model.getModel(), model.getTemperature(), model.getMaxTokens());
        return new Prompt(chatMessages, options);
    }

    private static List<Message> buildMessages(AiMindMapGenerateReqVO generateReqVO, String systemMessage) {
        List<Message> chatMessages = new ArrayList<>();
        // 1. 角色设定
        if (StrUtil.isNotBlank(systemMessage)) {
            chatMessages.add(new SystemMessage(systemMessage));
        }
        // 2. 用户输入
        chatMessages.add(new UserMessage(generateReqVO.getPrompt()));
        return chatMessages;
    }

    private AiChatModelDO getModel(AiChatRoleDO role) {
        AiChatModelDO model = null;
        if (role != null && role.getModelId() != null) {
            model = chatModalService.getChatModel(role.getModelId());
        }
        if (model == null) {
            model = chatModalService.getRequiredDefaultChatModel();
        }
        Assert.notNull(model, "[AI] 获取不到模型");
        return model;
    }

}
