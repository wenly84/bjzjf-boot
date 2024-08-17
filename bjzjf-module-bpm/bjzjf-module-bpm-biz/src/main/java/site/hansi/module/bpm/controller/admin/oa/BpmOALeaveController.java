package site.hansi.module.bpm.controller.admin.oa;

import site.hansi.framework.common.pojo.CommonResult;
import site.hansi.framework.common.pojo.PageResult;
import site.hansi.framework.common.util.object.BeanUtils;
import site.hansi.module.bpm.controller.admin.oa.vo.BpmOALeaveCreateReqVO;
import site.hansi.module.bpm.controller.admin.oa.vo.BpmOALeavePageReqVO;
import site.hansi.module.bpm.controller.admin.oa.vo.BpmOALeaveRespVO;
import site.hansi.module.bpm.dal.dataobject.oa.BpmOALeaveDO;
import site.hansi.module.bpm.service.oa.BpmOALeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static site.hansi.framework.common.pojo.CommonResult.success;
import static site.hansi.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * OA 请假申请 Controller，用于演示自己存储数据，接入工作流的例子
 *
 * @author jason
 * @author 北京智匠坊
 */
@Tag(name = "管理后台 - OA 请假申请")
@RestController
@RequestMapping("/bpm/oa/leave")
@Validated
public class BpmOALeaveController {

    @Resource
    private BpmOALeaveService leaveService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('bpm:oa-leave:create')")
    @Operation(summary = "创建请求申请")
    public CommonResult<Long> createLeave(@Valid @RequestBody BpmOALeaveCreateReqVO createReqVO) {
        return success(leaveService.createLeave(getLoginUserId(), createReqVO));
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('bpm:oa-leave:query')")
    @Operation(summary = "获得请假申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<BpmOALeaveRespVO> getLeave(@RequestParam("id") Long id) {
        BpmOALeaveDO leave = leaveService.getLeave(id);
        return success(BeanUtils.toBean(leave, BpmOALeaveRespVO.class));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('bpm:oa-leave:query')")
    @Operation(summary = "获得请假申请分页")
    public CommonResult<PageResult<BpmOALeaveRespVO>> getLeavePage(@Valid BpmOALeavePageReqVO pageVO) {
        PageResult<BpmOALeaveDO> pageResult = leaveService.getLeavePage(getLoginUserId(), pageVO);
        return success(BeanUtils.toBean(pageResult, BpmOALeaveRespVO.class));
    }

}
