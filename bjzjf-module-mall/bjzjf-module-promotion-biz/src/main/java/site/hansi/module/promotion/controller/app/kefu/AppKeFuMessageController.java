package site.hansi.module.promotion.controller.app.kefu;

import site.hansi.framework.common.enums.UserTypeEnum;
import site.hansi.framework.common.pojo.CommonResult;
import site.hansi.framework.common.pojo.PageResult;
import site.hansi.framework.common.util.object.BeanUtils;
import site.hansi.framework.security.core.annotations.PreAuthenticated;
import site.hansi.module.promotion.controller.admin.kefu.vo.message.KeFuMessageRespVO;
import site.hansi.module.promotion.controller.app.kefu.vo.message.AppKeFuMessagePageReqVO;
import site.hansi.module.promotion.controller.app.kefu.vo.message.AppKeFuMessageSendReqVO;
import site.hansi.module.promotion.dal.dataobject.kefu.KeFuMessageDO;
import site.hansi.module.promotion.service.kefu.KeFuMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static site.hansi.framework.common.pojo.CommonResult.success;
import static site.hansi.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 客服消息")
@RestController
@RequestMapping("/promotion/kefu-message")
@Validated
public class AppKeFuMessageController {

    @Resource
    private KeFuMessageService kefuMessageService;

    @PostMapping("/send")
    @Operation(summary = "发送客服消息")
    @PreAuthenticated
    public CommonResult<Long> sendKefuMessage(@Valid @RequestBody AppKeFuMessageSendReqVO sendReqVO) {
        sendReqVO.setSenderId(getLoginUserId()).setSenderType(UserTypeEnum.MEMBER.getValue()); // 设置用户编号和类型
        return success(kefuMessageService.sendKefuMessage(sendReqVO));
    }

    @PutMapping("/update-read-status")
    @Operation(summary = "更新客服消息已读状态")
    @Parameter(name = "conversationId", description = "会话编号", required = true)
    @PreAuthenticated
    public CommonResult<Boolean> updateKefuMessageReadStatus(@RequestParam("conversationId") Long conversationId) {
        kefuMessageService.updateKeFuMessageReadStatus(conversationId, getLoginUserId(), UserTypeEnum.MEMBER.getValue());
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得客服消息分页")
    @PreAuthenticated
    public CommonResult<PageResult<KeFuMessageRespVO>> getKefuMessagePage(@Valid AppKeFuMessagePageReqVO pageReqVO) {
        PageResult<KeFuMessageDO> pageResult = kefuMessageService.getKeFuMessagePage(pageReqVO, getLoginUserId());
        return success(BeanUtils.toBean(pageResult, KeFuMessageRespVO.class));
    }

}