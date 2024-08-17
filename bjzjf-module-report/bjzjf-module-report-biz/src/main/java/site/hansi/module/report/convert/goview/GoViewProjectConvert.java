package site.hansi.module.report.convert.goview;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.module.report.controller.admin.goview.vo.project.GoViewProjectCreateReqVO;
import site.hansi.module.report.controller.admin.goview.vo.project.GoViewProjectRespVO;
import site.hansi.module.report.controller.admin.goview.vo.project.GoViewProjectUpdateReqVO;
import site.hansi.module.report.dal.dataobject.goview.GoViewProjectDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GoViewProjectConvert {

    GoViewProjectConvert INSTANCE = Mappers.getMapper(GoViewProjectConvert.class);

    GoViewProjectDO convert(GoViewProjectCreateReqVO bean);

    GoViewProjectDO convert(GoViewProjectUpdateReqVO bean);

    GoViewProjectRespVO convert(GoViewProjectDO bean);

    PageResult<GoViewProjectRespVO> convertPage(PageResult<GoViewProjectDO> page);

}
