package site.hansi.module.pay.convert.wallet;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.module.pay.controller.admin.wallet.vo.wallet.PayWalletRespVO;
import site.hansi.module.pay.controller.app.wallet.vo.wallet.AppPayWalletRespVO;
import site.hansi.module.pay.dal.dataobject.wallet.PayWalletDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PayWalletConvert {

    PayWalletConvert INSTANCE = Mappers.getMapper(PayWalletConvert.class);

    AppPayWalletRespVO convert(PayWalletDO bean);

    PayWalletRespVO convert02(PayWalletDO bean);

    PageResult<PayWalletRespVO> convertPage(PageResult<PayWalletDO> page);

}
