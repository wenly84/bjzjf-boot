package site.hansi.module.product.controller.admin.history;

import cn.hutool.core.collection.CollUtil;
import site.hansi.framework.common.pojo.CommonResult;
import site.hansi.framework.common.pojo.PageResult;
import site.hansi.framework.common.util.object.BeanUtils;
import site.hansi.module.product.controller.admin.history.vo.ProductBrowseHistoryPageReqVO;
import site.hansi.module.product.controller.admin.history.vo.ProductBrowseHistoryRespVO;
import site.hansi.module.product.dal.dataobject.history.ProductBrowseHistoryDO;
import site.hansi.module.product.dal.dataobject.spu.ProductSpuDO;
import site.hansi.module.product.service.history.ProductBrowseHistoryService;
import site.hansi.module.product.service.spu.ProductSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

import static site.hansi.framework.common.pojo.CommonResult.success;
import static site.hansi.framework.common.util.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - 商品浏览记录")
@RestController
@RequestMapping("/product/browse-history")
@Validated
public class ProductBrowseHistoryController {

    @Resource
    private ProductBrowseHistoryService browseHistoryService;
    @Resource
    private ProductSpuService productSpuService;

    @GetMapping("/page")
    @Operation(summary = "获得商品浏览记录分页")
    @PreAuthorize("@ss.hasPermission('product:browse-history:query')")
    public CommonResult<PageResult<ProductBrowseHistoryRespVO>> getBrowseHistoryPage(@Valid ProductBrowseHistoryPageReqVO pageReqVO) {
        PageResult<ProductBrowseHistoryDO> pageResult = browseHistoryService.getBrowseHistoryPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty());
        }

        // 得到商品 spu 信息
        Map<Long, ProductSpuDO> spuMap = productSpuService.getSpuMap(
                convertSet(pageResult.getList(), ProductBrowseHistoryDO::getSpuId));
        return success(BeanUtils.toBean(pageResult, ProductBrowseHistoryRespVO.class,
                vo -> Optional.ofNullable(spuMap.get(vo.getSpuId()))
                        .ifPresent(spu -> vo.setSpuName(spu.getName()).setPicUrl(spu.getPicUrl()).setPrice(spu.getPrice()))));
    }

}