package site.hansi.module.product.convert.brand;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.module.product.controller.admin.brand.vo.ProductBrandCreateReqVO;
import site.hansi.module.product.controller.admin.brand.vo.ProductBrandRespVO;
import site.hansi.module.product.controller.admin.brand.vo.ProductBrandSimpleRespVO;
import site.hansi.module.product.controller.admin.brand.vo.ProductBrandUpdateReqVO;
import site.hansi.module.product.dal.dataobject.brand.ProductBrandDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 品牌 Convert
 *
 * @author 北京智匠坊
 */
@Mapper
public interface ProductBrandConvert {

    ProductBrandConvert INSTANCE = Mappers.getMapper(ProductBrandConvert.class);

    ProductBrandDO convert(ProductBrandCreateReqVO bean);

    ProductBrandDO convert(ProductBrandUpdateReqVO bean);

    ProductBrandRespVO convert(ProductBrandDO bean);

    List<ProductBrandSimpleRespVO> convertList1(List<ProductBrandDO> list);

    List<ProductBrandRespVO> convertList(List<ProductBrandDO> list);

    PageResult<ProductBrandRespVO> convertPage(PageResult<ProductBrandDO> page);

}
