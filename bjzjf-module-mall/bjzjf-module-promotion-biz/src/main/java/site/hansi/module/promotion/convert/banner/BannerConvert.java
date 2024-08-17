package site.hansi.module.promotion.convert.banner;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.module.promotion.controller.admin.banner.vo.BannerCreateReqVO;
import site.hansi.module.promotion.controller.admin.banner.vo.BannerRespVO;
import site.hansi.module.promotion.controller.admin.banner.vo.BannerUpdateReqVO;
import site.hansi.module.promotion.controller.app.banner.vo.AppBannerRespVO;
import site.hansi.module.promotion.dal.dataobject.banner.BannerDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BannerConvert {

    BannerConvert INSTANCE = Mappers.getMapper(BannerConvert.class);

    List<BannerRespVO> convertList(List<BannerDO> list);

    PageResult<BannerRespVO> convertPage(PageResult<BannerDO> pageResult);

    BannerRespVO convert(BannerDO banner);

    BannerDO convert(BannerCreateReqVO createReqVO);

    BannerDO convert(BannerUpdateReqVO updateReqVO);

    List<AppBannerRespVO> convertList01(List<BannerDO> bannerList);

}
