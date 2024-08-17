package site.hansi.module.promotion.convert.seckill;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.module.promotion.controller.admin.seckill.vo.config.SeckillConfigCreateReqVO;
import site.hansi.module.promotion.controller.admin.seckill.vo.config.SeckillConfigRespVO;
import site.hansi.module.promotion.controller.admin.seckill.vo.config.SeckillConfigSimpleRespVO;
import site.hansi.module.promotion.controller.admin.seckill.vo.config.SeckillConfigUpdateReqVO;
import site.hansi.module.promotion.controller.app.seckill.vo.config.AppSeckillConfigRespVO;
import site.hansi.module.promotion.dal.dataobject.seckill.SeckillConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 秒杀时段 Convert
 *
 * @author 北京智匠坊
 */
@Mapper
public interface SeckillConfigConvert {

    SeckillConfigConvert INSTANCE = Mappers.getMapper(SeckillConfigConvert.class);

    SeckillConfigDO convert(SeckillConfigCreateReqVO bean);

    SeckillConfigDO convert(SeckillConfigUpdateReqVO bean);

    SeckillConfigRespVO convert(SeckillConfigDO bean);

    List<SeckillConfigRespVO> convertList(List<SeckillConfigDO> list);

    List<SeckillConfigSimpleRespVO> convertList1(List<SeckillConfigDO> list);

    PageResult<SeckillConfigRespVO> convertPage(PageResult<SeckillConfigDO> page);

    List<AppSeckillConfigRespVO> convertList2(List<SeckillConfigDO> list);

    AppSeckillConfigRespVO convert1(SeckillConfigDO filteredConfig);
}
