package site.hansi.module.trade.convert.delivery;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.framework.common.util.collection.CollectionUtils;
import site.hansi.framework.common.util.number.NumberUtils;
import site.hansi.framework.ip.core.utils.AreaUtils;
import site.hansi.module.trade.controller.admin.delivery.vo.pickup.DeliveryPickUpStoreCreateReqVO;
import site.hansi.module.trade.controller.admin.delivery.vo.pickup.DeliveryPickUpStoreRespVO;
import site.hansi.module.trade.controller.admin.delivery.vo.pickup.DeliveryPickUpStoreSimpleRespVO;
import site.hansi.module.trade.controller.admin.delivery.vo.pickup.DeliveryPickUpStoreUpdateReqVO;
import site.hansi.module.trade.controller.app.delivery.vo.pickup.AppDeliveryPickUpStoreRespVO;
import site.hansi.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DeliveryPickUpStoreConvert {

    DeliveryPickUpStoreConvert INSTANCE = Mappers.getMapper(DeliveryPickUpStoreConvert.class);

    DeliveryPickUpStoreDO convert(DeliveryPickUpStoreCreateReqVO bean);

    DeliveryPickUpStoreDO convert(DeliveryPickUpStoreUpdateReqVO bean);

    DeliveryPickUpStoreRespVO convert(DeliveryPickUpStoreDO bean);

    List<DeliveryPickUpStoreRespVO> convertList(List<DeliveryPickUpStoreDO> list);

    PageResult<DeliveryPickUpStoreRespVO> convertPage(PageResult<DeliveryPickUpStoreDO> page);

    List<DeliveryPickUpStoreSimpleRespVO> convertList1(List<DeliveryPickUpStoreDO> list);
    @Mapping(source = "areaId", target = "areaName", qualifiedByName = "convertAreaIdToAreaName")
    DeliveryPickUpStoreSimpleRespVO convert02(DeliveryPickUpStoreDO bean);

    @Named("convertAreaIdToAreaName")
    default String convertAreaIdToAreaName(Integer areaId) {
        return AreaUtils.format(areaId);
    }

    default List<AppDeliveryPickUpStoreRespVO> convertList(List<DeliveryPickUpStoreDO> list,
                                                           Double latitude, Double longitude) {
        List<AppDeliveryPickUpStoreRespVO> voList =  CollectionUtils.convertList(list, store -> {
            AppDeliveryPickUpStoreRespVO storeVO = convert03(store);
            if (latitude != null && longitude != null) {
                storeVO.setDistance(NumberUtils.getDistance(latitude, longitude, storeVO.getLatitude(), storeVO.getLongitude()));
            }
            return storeVO;
        });
        return voList;
    }
    @Mapping(source = "areaId", target = "areaName", qualifiedByName = "convertAreaIdToAreaName")
    AppDeliveryPickUpStoreRespVO convert03(DeliveryPickUpStoreDO bean);

}
