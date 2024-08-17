package site.hansi.module.erp.dal.mysql.sale;


import site.hansi.framework.common.pojo.PageResult;
import site.hansi.framework.mybatis.core.mapper.BaseMapperX;
import site.hansi.framework.mybatis.core.query.MPJLambdaWrapperX;
import site.hansi.module.erp.controller.admin.sale.vo.returns.ErpSaleReturnPageReqVO;
import site.hansi.module.erp.dal.dataobject.sale.ErpSaleOutDO;
import site.hansi.module.erp.dal.dataobject.sale.ErpSaleReturnDO;
import site.hansi.module.erp.dal.dataobject.sale.ErpSaleReturnItemDO;
import site.hansi.module.erp.enums.ErpAuditStatus;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Objects;

/**
 * ERP 销售退货 Mapper
 *
 * @author 北京智匠坊
 */
@Mapper
public interface ErpSaleReturnMapper extends BaseMapperX<ErpSaleReturnDO> {

    default PageResult<ErpSaleReturnDO> selectPage(ErpSaleReturnPageReqVO reqVO) {
        MPJLambdaWrapperX<ErpSaleReturnDO> query = new MPJLambdaWrapperX<ErpSaleReturnDO>()
                .likeIfPresent(ErpSaleReturnDO::getNo, reqVO.getNo())
                .eqIfPresent(ErpSaleReturnDO::getCustomerId, reqVO.getCustomerId())
                .betweenIfPresent(ErpSaleReturnDO::getReturnTime, reqVO.getReturnTime())
                .eqIfPresent(ErpSaleReturnDO::getStatus, reqVO.getStatus())
                .likeIfPresent(ErpSaleReturnDO::getRemark, reqVO.getRemark())
                .eqIfPresent(ErpSaleReturnDO::getCreator, reqVO.getCreator())
                .eqIfPresent(ErpSaleReturnDO::getAccountId, reqVO.getAccountId())
                .likeIfPresent(ErpSaleReturnDO::getOrderNo, reqVO.getOrderNo())
                .orderByDesc(ErpSaleReturnDO::getId);
        // 退款状态。为什么需要 t. 的原因，是因为联表查询时，需要指定表名，不然会报字段不存在的错误
        if (Objects.equals(reqVO.getRefundStatus(), ErpSaleReturnPageReqVO.REFUND_STATUS_NONE)) {
            query.eq(ErpSaleReturnDO::getRefundPrice, 0);
        } else if (Objects.equals(reqVO.getRefundStatus(), ErpSaleReturnPageReqVO.REFUND_STATUS_PART)) {
            query.gt(ErpSaleReturnDO::getRefundPrice, 0).apply("t.refund_price < t.total_price");
        } else if (Objects.equals(reqVO.getRefundStatus(), ErpSaleReturnPageReqVO.REFUND_STATUS_ALL)) {
            query.apply("t.refund_price = t.total_price");
        }
        if (Boolean.TRUE.equals(reqVO.getRefundEnable())) {
            query.eq(ErpSaleOutDO::getStatus, ErpAuditStatus.APPROVE.getStatus())
                    .apply("t.refund_price < t.total_price");
        }
        if (reqVO.getWarehouseId() != null || reqVO.getProductId() != null) {
            query.leftJoin(ErpSaleReturnItemDO.class, ErpSaleReturnItemDO::getReturnId, ErpSaleReturnDO::getId)
                    .eq(reqVO.getWarehouseId() != null, ErpSaleReturnItemDO::getWarehouseId, reqVO.getWarehouseId())
                    .eq(reqVO.getProductId() != null, ErpSaleReturnItemDO::getProductId, reqVO.getProductId())
                    .groupBy(ErpSaleReturnDO::getId); // 避免 1 对多查询，产生相同的 1
        }
        return selectJoinPage(reqVO, ErpSaleReturnDO.class, query);
    }

    default int updateByIdAndStatus(Long id, Integer status, ErpSaleReturnDO updateObj) {
        return update(updateObj, new LambdaUpdateWrapper<ErpSaleReturnDO>()
                .eq(ErpSaleReturnDO::getId, id).eq(ErpSaleReturnDO::getStatus, status));
    }

    default ErpSaleReturnDO selectByNo(String no) {
        return selectOne(ErpSaleReturnDO::getNo, no);
    }

    default List<ErpSaleReturnDO> selectListByOrderId(Long orderId) {
        return selectList(ErpSaleReturnDO::getOrderId, orderId);
    }

}