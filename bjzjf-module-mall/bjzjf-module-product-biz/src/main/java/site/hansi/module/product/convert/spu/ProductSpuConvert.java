package site.hansi.module.product.convert.spu;

import site.hansi.framework.common.util.collection.CollectionUtils;
import site.hansi.framework.common.util.object.BeanUtils;
import site.hansi.module.product.controller.admin.spu.vo.ProductSkuRespVO;
import site.hansi.module.product.controller.admin.spu.vo.ProductSpuPageReqVO;
import site.hansi.module.product.controller.admin.spu.vo.ProductSpuRespVO;
import site.hansi.module.product.controller.app.spu.vo.AppProductSpuPageReqVO;
import site.hansi.module.product.dal.dataobject.sku.ProductSkuDO;
import site.hansi.module.product.dal.dataobject.spu.ProductSpuDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static site.hansi.framework.common.util.collection.CollectionUtils.convertMultiMap;

/**
 * 商品 SPU Convert
 *
 * @author 北京智匠坊
 */
@Mapper
public interface ProductSpuConvert {

    ProductSpuConvert INSTANCE = Mappers.getMapper(ProductSpuConvert.class);

    ProductSpuPageReqVO convert(AppProductSpuPageReqVO bean);

    default ProductSpuRespVO convert(ProductSpuDO spu, List<ProductSkuDO> skus) {
        ProductSpuRespVO spuVO = BeanUtils.toBean(spu, ProductSpuRespVO.class);
        spuVO.setSkus(BeanUtils.toBean(skus, ProductSkuRespVO.class));
        return spuVO;
    }

    default List<ProductSpuRespVO> convertForSpuDetailRespListVO(List<ProductSpuDO> spus, List<ProductSkuDO> skus) {
        Map<Long, List<ProductSkuDO>> skuMultiMap = convertMultiMap(skus, ProductSkuDO::getSpuId);
        return CollectionUtils.convertList(spus, spu -> convert(spu, skuMultiMap.get(spu.getId())));
    }

}
