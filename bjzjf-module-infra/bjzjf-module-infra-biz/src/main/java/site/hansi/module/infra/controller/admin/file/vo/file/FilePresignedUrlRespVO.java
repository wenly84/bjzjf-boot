package site.hansi.module.infra.controller.admin.file.vo.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "管理后台 - 文件预签名地址 Response VO")
@Data
public class FilePresignedUrlRespVO {

    @Schema(description = "配置编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11")
    private Long configId;

    @Schema(description = "文件上传 URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://s3.cn-south-1.qiniucs.com/bjzjf-boot/***")
    private String uploadUrl;

    /**
     * 为什么要返回 url 字段？
     *
     * 前端上传完文件后，需要使用该 URL 进行访问
     */
    @Schema(description = "文件访问 URL", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "https://file.hansi.site/96de1c7f5097ff6a7722b34772fb7b76b140f.png")
    private String url;

}
