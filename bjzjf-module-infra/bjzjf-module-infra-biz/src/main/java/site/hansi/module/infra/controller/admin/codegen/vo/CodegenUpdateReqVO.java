package site.hansi.module.infra.controller.admin.codegen.vo;

import site.hansi.module.infra.controller.admin.codegen.vo.column.CodegenColumnSaveReqVO;
import site.hansi.module.infra.controller.admin.codegen.vo.table.CodegenTableSaveReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "管理后台 - 代码生成表和字段的修改 Request VO")
@Data
public class CodegenUpdateReqVO {

    @Valid // 校验内嵌的字段
    @NotNull(message = "表定义不能为空")
    private CodegenTableSaveReqVO table;

    @Valid // 校验内嵌的字段
    @NotNull(message = "字段定义不能为空")
    private List<CodegenColumnSaveReqVO> columns;

}
