package site.hansi.module.pay.convert.transfer;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.framework.pay.core.client.dto.transfer.PayTransferUnifiedReqDTO;
import site.hansi.module.pay.api.transfer.dto.PayTransferCreateReqDTO;
import site.hansi.module.pay.controller.admin.demo.vo.transfer.PayDemoTransferCreateReqVO;
import site.hansi.module.pay.controller.admin.transfer.vo.PayTransferCreateReqVO;
import site.hansi.module.pay.controller.admin.transfer.vo.PayTransferPageItemRespVO;
import site.hansi.module.pay.controller.admin.transfer.vo.PayTransferRespVO;
import site.hansi.module.pay.dal.dataobject.transfer.PayTransferDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PayTransferConvert {

    PayTransferConvert INSTANCE = Mappers.getMapper(PayTransferConvert.class);

    PayTransferDO convert(PayTransferCreateReqDTO dto);

    PayTransferUnifiedReqDTO convert2(PayTransferDO dto);

    PayTransferCreateReqDTO convert(PayTransferCreateReqVO vo);

    PayTransferCreateReqDTO convert(PayDemoTransferCreateReqVO vo);

    PayTransferRespVO  convert(PayTransferDO bean);

    PageResult<PayTransferPageItemRespVO> convertPage(PageResult<PayTransferDO> pageResult);
}
