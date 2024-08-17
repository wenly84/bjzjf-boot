package site.hansi.module.promotion.api.coupon;


import site.hansi.module.promotion.api.coupon.dto.CouponRespDTO;
import site.hansi.module.promotion.api.coupon.dto.CouponUseReqDTO;
import site.hansi.module.promotion.api.coupon.dto.CouponValidReqDTO;
import site.hansi.module.promotion.convert.coupon.CouponConvert;
import site.hansi.module.promotion.dal.dataobject.coupon.CouponDO;
import site.hansi.module.promotion.service.coupon.CouponService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 优惠劵 API 实现类
 *
 * @author 北京智匠坊
 */
@Service
@Validated
public class CouponApiImpl implements CouponApi {

    @Resource
    private CouponService couponService;

    @Override
    public void useCoupon(CouponUseReqDTO useReqDTO) {
        couponService.useCoupon(useReqDTO.getId(), useReqDTO.getUserId(),
                useReqDTO.getOrderId());
    }

    @Override
    public void returnUsedCoupon(Long id) {
        couponService.returnUsedCoupon(id);
    }

    @Override
    public CouponRespDTO validateCoupon(CouponValidReqDTO validReqDTO) {
        CouponDO coupon = couponService.validCoupon(validReqDTO.getId(), validReqDTO.getUserId());
        return CouponConvert.INSTANCE.convert(coupon);
    }

}
