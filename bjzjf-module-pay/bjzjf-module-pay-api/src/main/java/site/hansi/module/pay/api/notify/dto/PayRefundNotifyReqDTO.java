package site.hansi.module.pay.api.notify.dto;

import site.hansi.module.pay.enums.refund.PayRefundStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 退款单的通知 Request DTO
 *
 * @author 北京智匠坊
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayRefundNotifyReqDTO {

    /**
     * 商户退款单编号
     */
    @NotEmpty(message = "商户退款单编号不能为空")
    private String merchantOrderId;

    /**
     * 支付退款编号
     */
    @NotNull(message = "支付退款编号不能为空")
    private Long payRefundId;

}
