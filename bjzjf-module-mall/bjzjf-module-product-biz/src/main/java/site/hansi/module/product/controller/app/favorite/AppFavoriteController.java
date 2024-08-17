package site.hansi.module.product.controller.app.favorite;

import cn.hutool.core.collection.CollUtil;
import site.hansi.framework.common.pojo.CommonResult;
import site.hansi.framework.common.pojo.PageResult;
import site.hansi.framework.security.core.annotations.PreAuthenticated;
import site.hansi.module.product.controller.app.favorite.vo.AppFavoriteBatchReqVO;
import site.hansi.module.product.controller.app.favorite.vo.AppFavoritePageReqVO;
import site.hansi.module.product.controller.app.favorite.vo.AppFavoriteReqVO;
import site.hansi.module.product.controller.app.favorite.vo.AppFavoriteRespVO;
import site.hansi.module.product.convert.favorite.ProductFavoriteConvert;
import site.hansi.module.product.dal.dataobject.favorite.ProductFavoriteDO;
import site.hansi.module.product.dal.dataobject.spu.ProductSpuDO;
import site.hansi.module.product.service.favorite.ProductFavoriteService;
import site.hansi.module.product.service.spu.ProductSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static site.hansi.framework.common.pojo.CommonResult.success;
import static site.hansi.framework.common.util.collection.CollectionUtils.convertList;
import static site.hansi.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 商品收藏")
@RestController
@RequestMapping("/product/favorite")
public class AppFavoriteController {

    @Resource
    private ProductFavoriteService productFavoriteService;
    @Resource
    private ProductSpuService productSpuService;

    @PostMapping(value = "/create")
    @Operation(summary = "添加商品收藏")
    @PreAuthenticated
    public CommonResult<Long> createFavorite(@RequestBody @Valid AppFavoriteReqVO reqVO) {
        return success(productFavoriteService.createFavorite(getLoginUserId(), reqVO.getSpuId()));
    }

    @DeleteMapping(value = "/delete")
    @Operation(summary = "取消单个商品收藏")
    @PreAuthenticated
    public CommonResult<Boolean> deleteFavorite(@RequestBody @Valid AppFavoriteReqVO reqVO) {
        productFavoriteService.deleteFavorite(getLoginUserId(), reqVO.getSpuId());
        return success(Boolean.TRUE);
    }

    @GetMapping(value = "/page")
    @Operation(summary = "获得商品收藏分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppFavoriteRespVO>> getFavoritePage(AppFavoritePageReqVO reqVO) {
        PageResult<ProductFavoriteDO> favoritePage = productFavoriteService.getFavoritePage(getLoginUserId(), reqVO);
        if (CollUtil.isEmpty(favoritePage.getList())) {
            return success(PageResult.empty());
        }

        // 得到商品 spu 信息
        List<ProductFavoriteDO> favorites = favoritePage.getList();
        List<Long> spuIds = convertList(favorites, ProductFavoriteDO::getSpuId);
        List<ProductSpuDO> spus = productSpuService.getSpuList(spuIds);

        // 转换 VO 结果
        PageResult<AppFavoriteRespVO> pageResult = new PageResult<>(favoritePage.getTotal());
        pageResult.setList(ProductFavoriteConvert.INSTANCE.convertList(favorites, spus));
        return success(pageResult);
    }

    @GetMapping(value = "/exits")
    @Operation(summary = "检查是否收藏过商品")
    @PreAuthenticated
    public CommonResult<Boolean> isFavoriteExists(AppFavoriteReqVO reqVO) {
        ProductFavoriteDO favorite = productFavoriteService.getFavorite(getLoginUserId(), reqVO.getSpuId());
        return success(favorite != null);
    }

    @GetMapping(value = "/get-count")
    @Operation(summary = "获得商品收藏数量")
    @PreAuthenticated
    public CommonResult<Long> getFavoriteCount() {
        return success(productFavoriteService.getFavoriteCount(getLoginUserId()));
    }

}
