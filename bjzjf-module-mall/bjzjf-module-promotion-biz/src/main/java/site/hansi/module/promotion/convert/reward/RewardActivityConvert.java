package site.hansi.module.promotion.convert.reward;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.module.promotion.controller.admin.reward.vo.RewardActivityCreateReqVO;
import site.hansi.module.promotion.controller.admin.reward.vo.RewardActivityRespVO;
import site.hansi.module.promotion.controller.admin.reward.vo.RewardActivityUpdateReqVO;
import site.hansi.module.promotion.dal.dataobject.reward.RewardActivityDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 满减送活动 Convert
 *
 * @author 北京智匠坊
 */
@Mapper
public interface RewardActivityConvert {

    RewardActivityConvert INSTANCE = Mappers.getMapper(RewardActivityConvert.class);

    RewardActivityDO convert(RewardActivityCreateReqVO bean);

    RewardActivityDO convert(RewardActivityUpdateReqVO bean);

    RewardActivityRespVO convert(RewardActivityDO bean);

    PageResult<RewardActivityRespVO> convertPage(PageResult<RewardActivityDO> page);

}
