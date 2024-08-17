package site.hansi.module.pay.convert.demo;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.module.pay.controller.admin.demo.vo.order.PayDemoOrderCreateReqVO;
import site.hansi.module.pay.controller.admin.demo.vo.order.PayDemoOrderRespVO;
import site.hansi.module.pay.dal.dataobject.demo.PayDemoOrderDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 示例订单 Convert
 *
 * @author 北京智匠坊
 */
@Mapper
public interface PayDemoOrderConvert {

    PayDemoOrderConvert INSTANCE = Mappers.getMapper(PayDemoOrderConvert.class);

    PayDemoOrderDO convert(PayDemoOrderCreateReqVO bean);

    PayDemoOrderRespVO convert(PayDemoOrderDO bean);

    PageResult<PayDemoOrderRespVO> convertPage(PageResult<PayDemoOrderDO> page);

}
