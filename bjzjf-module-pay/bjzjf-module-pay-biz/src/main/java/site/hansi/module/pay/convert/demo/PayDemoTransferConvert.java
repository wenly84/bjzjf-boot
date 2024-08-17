package site.hansi.module.pay.convert.demo;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.module.pay.controller.admin.demo.vo.transfer.PayDemoTransferCreateReqVO;
import site.hansi.module.pay.controller.admin.demo.vo.transfer.PayDemoTransferRespVO;
import site.hansi.module.pay.dal.dataobject.demo.PayDemoTransferDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jason
 */
@Mapper
public interface PayDemoTransferConvert {

    PayDemoTransferConvert INSTANCE = Mappers.getMapper(PayDemoTransferConvert.class);

    PayDemoTransferDO convert(PayDemoTransferCreateReqVO bean);

    PageResult<PayDemoTransferRespVO> convertPage(PageResult<PayDemoTransferDO> pageResult);
}
