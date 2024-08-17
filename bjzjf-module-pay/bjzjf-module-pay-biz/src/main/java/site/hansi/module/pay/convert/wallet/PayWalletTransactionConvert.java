package site.hansi.module.pay.convert.wallet;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.module.pay.controller.admin.wallet.vo.transaction.PayWalletTransactionRespVO;
import site.hansi.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionRespVO;
import site.hansi.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import site.hansi.module.pay.service.wallet.bo.WalletTransactionCreateReqBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PayWalletTransactionConvert {

    PayWalletTransactionConvert INSTANCE = Mappers.getMapper(PayWalletTransactionConvert.class);

    PageResult<PayWalletTransactionRespVO> convertPage2(PageResult<PayWalletTransactionDO> page);

    PayWalletTransactionDO convert(WalletTransactionCreateReqBO bean);

}
