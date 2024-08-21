package site.hansi.module.ai.controller.admin.music.vo;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - AI 音乐修改 Request VO")
@Data
public class AiMusicUpdateReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15583")
    @NotNull(message = "编号不能为空")
    private Long id;

    @Schema(description = "是否发布", example = "true")
    private Boolean publicStatus;

}