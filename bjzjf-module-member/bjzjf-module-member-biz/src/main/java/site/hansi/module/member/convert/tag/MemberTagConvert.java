package site.hansi.module.member.convert.tag;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.module.member.controller.admin.tag.vo.MemberTagCreateReqVO;
import site.hansi.module.member.controller.admin.tag.vo.MemberTagRespVO;
import site.hansi.module.member.controller.admin.tag.vo.MemberTagUpdateReqVO;
import site.hansi.module.member.dal.dataobject.tag.MemberTagDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 会员标签 Convert
 *
 * @author 北京智匠坊
 */
@Mapper
public interface MemberTagConvert {

    MemberTagConvert INSTANCE = Mappers.getMapper(MemberTagConvert.class);

    MemberTagDO convert(MemberTagCreateReqVO bean);

    MemberTagDO convert(MemberTagUpdateReqVO bean);

    MemberTagRespVO convert(MemberTagDO bean);

    List<MemberTagRespVO> convertList(List<MemberTagDO> list);

    PageResult<MemberTagRespVO> convertPage(PageResult<MemberTagDO> page);

}
