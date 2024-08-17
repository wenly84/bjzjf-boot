package site.hansi.module.product.api.spu;

import site.hansi.framework.common.util.object.BeanUtils;
import site.hansi.module.product.api.spu.dto.ProductSpuRespDTO;
import site.hansi.module.product.dal.dataobject.spu.ProductSpuDO;
import site.hansi.module.product.service.spu.ProductSpuService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 商品 SPU API 接口实现类
 *
 * @author LeeYan9
 * @since 2022-09-06
 */
@Service
@Validated
public class ProductSpuApiImpl implements ProductSpuApi {

    @Resource
    private ProductSpuService spuService;

    @Override
    public List<ProductSpuRespDTO> getSpuList(Collection<Long> ids) {
        List<ProductSpuDO> spus = spuService.getSpuList(ids);
        return BeanUtils.toBean(spus, ProductSpuRespDTO.class);
    }

    @Override
    public List<ProductSpuRespDTO> validateSpuList(Collection<Long> ids) {
        List<ProductSpuDO> spus = spuService.validateSpuList(ids);
        return BeanUtils.toBean(spus, ProductSpuRespDTO.class);
    }

    @Override
    public ProductSpuRespDTO getSpu(Long id) {
        ProductSpuDO spu = spuService.getSpu(id);
        return BeanUtils.toBean(spu, ProductSpuRespDTO.class);
    }

}
