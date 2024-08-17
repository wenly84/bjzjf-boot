package site.hansi.module.bpm.controller.admin.definition.vo.form;

import site.hansi.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 动态表单分页 Request VO")
@Data
public class BpmFormPageReqVO extends PageParam {

    @Schema(description = "表单名称", example = "智匠坊")
    private String name;

}
