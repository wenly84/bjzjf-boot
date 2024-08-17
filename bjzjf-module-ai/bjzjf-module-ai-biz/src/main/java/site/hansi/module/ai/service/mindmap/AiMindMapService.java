package site.hansi.module.ai.service.mindmap;

import site.hansi.framework.common.pojo.CommonResult;
import site.hansi.module.ai.controller.admin.mindmap.vo.AiMindMapGenerateReqVO;
import reactor.core.publisher.Flux;

/**
 * AI 思维导图 Service 接口
 *
 * @author xiaoxin
 */
public interface AiMindMapService {

    /**
     * 生成思维导图内容
     *
     * @param generateReqVO 请求参数
     * @param userId        用户编号
     * @return 生成结果
     */
    Flux<CommonResult<String>> generateMindMap(AiMindMapGenerateReqVO generateReqVO, Long userId);

}
