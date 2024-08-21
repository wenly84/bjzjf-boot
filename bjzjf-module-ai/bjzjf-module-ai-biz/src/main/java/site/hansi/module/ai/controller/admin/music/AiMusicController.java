package site.hansi.module.ai.controller.admin.music;

import static site.hansi.framework.common.pojo.CommonResult.success;
import static site.hansi.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.util.ObjUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import site.hansi.framework.common.pojo.CommonResult;
import site.hansi.framework.common.pojo.PageResult;
import site.hansi.framework.common.util.object.BeanUtils;
import site.hansi.module.ai.controller.admin.music.vo.AiMusicPageReqVO;
import site.hansi.module.ai.controller.admin.music.vo.AiMusicRespVO;
import site.hansi.module.ai.controller.admin.music.vo.AiMusicUpdateMyReqVO;
import site.hansi.module.ai.controller.admin.music.vo.AiMusicUpdateReqVO;
import site.hansi.module.ai.controller.admin.music.vo.AiSunoGenerateReqVO;
import site.hansi.module.ai.dal.dataobject.music.AiMusicDO;
import site.hansi.module.ai.service.music.AiMusicService;

@Tag(name = "管理后台 - AI 音乐")
@RestController
@RequestMapping("/ai/music")
public class AiMusicController {

    @Resource
    private AiMusicService musicService;

    @GetMapping("/my-page")
    @Operation(summary = "获得【我的】音乐分页")
    public CommonResult<PageResult<AiMusicRespVO>> getMusicMyPage(@Valid AiMusicPageReqVO pageReqVO) {
        PageResult<AiMusicDO> pageResult = musicService.getMusicMyPage(pageReqVO, getLoginUserId());
        return success(BeanUtils.toBean(pageResult, AiMusicRespVO.class));
    }

    @PostMapping("/generate")
    @Operation(summary = "音乐生成")
    public CommonResult<List<Long>> generateMusic(@RequestBody @Valid AiSunoGenerateReqVO reqVO) {
        return success(musicService.generateMusic(getLoginUserId(), reqVO));
    }

    @Operation(summary = "删除【我的】音乐记录")
    @DeleteMapping("/delete-my")
    @Parameter(name = "id", required = true, description = "音乐编号", example = "1024")
    public CommonResult<Boolean> deleteMusicMy(@RequestParam("id") Long id) {
        musicService.deleteMusicMy(id, getLoginUserId());
        return success(true);
    }

    @GetMapping("/get-my")
    @Operation(summary = "获取【我的】音乐")
    @Parameter(name = "id", required = true, description = "音乐编号", example = "1024")
    public CommonResult<AiMusicRespVO> getMusicMy(@RequestParam("id") Long id) {
        AiMusicDO music = musicService.getMusic(id);
        if (music == null || ObjUtil.notEqual(getLoginUserId(), music.getUserId())) {
            return success(null);
        }
        return success(BeanUtils.toBean(music, AiMusicRespVO.class));
    }

    @PostMapping("/update-my")
    @Operation(summary = "修改【我的】音乐 目前只支持修改标题")
    @Parameter(name = "title", required = true, description = "音乐名称", example = "夜空中最亮的星")
    public CommonResult<Boolean> updateMy(AiMusicUpdateMyReqVO updateReqVO) {
        musicService.updateMyMusic(updateReqVO, getLoginUserId());
        return success(true);
    }

    // ================ 音乐管理 ================

    @GetMapping("/page")
    @Operation(summary = "获得音乐分页")
    @PreAuthorize("@ss.hasPermission('ai:music:query')")
    public CommonResult<PageResult<AiMusicRespVO>> getMusicPage(@Valid AiMusicPageReqVO pageReqVO) {
        PageResult<AiMusicDO> pageResult = musicService.getMusicPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AiMusicRespVO.class));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除音乐")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ai:music:delete')")
    public CommonResult<Boolean> deleteMusic(@RequestParam("id") Long id) {
        musicService.deleteMusic(id);
        return success(true);
    }

    @PutMapping("/update")
    @Operation(summary = "更新音乐")
    @PreAuthorize("@ss.hasPermission('ai:music:update')")
    public CommonResult<Boolean> updateMusic(@Valid @RequestBody AiMusicUpdateReqVO updateReqVO) {
        musicService.updateMusic(updateReqVO);
        return success(true);
    }

}
