package site.hansi.module.ai.dal.mysql.mindmap;

import site.hansi.framework.mybatis.core.mapper.BaseMapperX;
import site.hansi.module.ai.dal.dataobject.mindmap.AiMindMapDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI 思维导图 Mapper
 *
 * @author xiaoxin
 */
@Mapper
public interface AiMindMapMapper extends BaseMapperX<AiMindMapDO> {
}
