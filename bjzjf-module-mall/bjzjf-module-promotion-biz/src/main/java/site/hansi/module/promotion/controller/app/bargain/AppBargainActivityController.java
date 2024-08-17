package site.hansi.module.promotion.controller.app.bargain;

import cn.hutool.core.collection.CollUtil;
import site.hansi.framework.common.pojo.CommonResult;
import site.hansi.framework.common.pojo.PageParam;
import site.hansi.framework.common.pojo.PageResult;
import site.hansi.module.product.api.spu.ProductSpuApi;
import site.hansi.module.product.api.spu.dto.ProductSpuRespDTO;
import site.hansi.module.promotion.controller.app.bargain.vo.activity.AppBargainActivityDetailRespVO;
import site.hansi.module.promotion.controller.app.bargain.vo.activity.AppBargainActivityRespVO;
import site.hansi.module.promotion.convert.bargain.BargainActivityConvert;
import site.hansi.module.promotion.dal.dataobject.bargain.BargainActivityDO;
import site.hansi.module.promotion.enums.bargain.BargainRecordStatusEnum;
import site.hansi.module.promotion.service.bargain.BargainActivityService;
import site.hansi.module.promotion.service.bargain.BargainRecordService;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static site.hansi.framework.common.pojo.CommonResult.success;
import static site.hansi.framework.common.util.cache.CacheUtils.buildAsyncReloadingCache;
import static site.hansi.framework.common.util.collection.CollectionUtils.convertList;

@Tag(name = "用户 App - 砍价活动")
@RestController
@RequestMapping("/promotion/bargain-activity")
@Validated
public class AppBargainActivityController {

    /**
     * {@link AppBargainActivityRespVO} 缓存，通过它异步刷新 {@link #getBargainActivityList0(Integer)} 所要的首页数据
     */
    private final LoadingCache<Integer, List<AppBargainActivityRespVO>> bargainActivityListCache = buildAsyncReloadingCache(Duration.ofSeconds(10L),
            new CacheLoader<Integer, List<AppBargainActivityRespVO>>() {

                @Override
                public List<AppBargainActivityRespVO> load(Integer count) {
                    return getBargainActivityList0(count);
                }

            });

    @Resource
    private BargainActivityService bargainActivityService;
    @Resource
    private BargainRecordService bargainRecordService;

    @Resource
    private ProductSpuApi spuApi;

    @GetMapping("/list")
    @Operation(summary = "获得砍价活动列表", description = "用于小程序首页")
    @Parameter(name = "count", description = "需要展示的数量", example = "6")
    public CommonResult<List<AppBargainActivityRespVO>> getBargainActivityList(
            @RequestParam(name = "count", defaultValue = "6") Integer count) {
        return success(bargainActivityListCache.getUnchecked(count));
    }

    private List<AppBargainActivityRespVO>getBargainActivityList0(Integer count) {
        List<BargainActivityDO> list = bargainActivityService.getBargainActivityListByCount(count);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        // 拼接数据
        List<ProductSpuRespDTO> spuList = spuApi.getSpuList(convertList(list, BargainActivityDO::getSpuId));
        return BargainActivityConvert.INSTANCE.convertAppList(list, spuList);
    }

    @GetMapping("/page")
    @Operation(summary = "获得砍价活动分页")
    public CommonResult<PageResult<AppBargainActivityRespVO>> getBargainActivityPage(PageParam pageReqVO) {
        PageResult<BargainActivityDO> result = bargainActivityService.getBargainActivityPage(pageReqVO);
        if (CollUtil.isEmpty(result.getList())) {
            return success(PageResult.empty(result.getTotal()));
        }
        // 拼接数据
        List<ProductSpuRespDTO> spuList = spuApi.getSpuList(convertList(result.getList(), BargainActivityDO::getSpuId));
        return success(BargainActivityConvert.INSTANCE.convertAppPage(result, spuList));
    }

    @GetMapping("/get-detail")
    @Operation(summary = "获得砍价活动详情")
    @Parameter(name = "id", description = "活动编号", example = "1")
    public CommonResult<AppBargainActivityDetailRespVO> getBargainActivityDetail(@RequestParam("id") Long id) {
        BargainActivityDO activity = bargainActivityService.getBargainActivity(id);
        if (activity == null) {
            return success(null);
        }
        // 拼接数据
        Integer successUserCount = bargainRecordService.getBargainRecordUserCount(id, BargainRecordStatusEnum.SUCCESS.getStatus());
        ProductSpuRespDTO spu = spuApi.getSpu(activity.getSpuId());
        return success(BargainActivityConvert.INSTANCE.convert(activity, successUserCount, spu));
    }

}
