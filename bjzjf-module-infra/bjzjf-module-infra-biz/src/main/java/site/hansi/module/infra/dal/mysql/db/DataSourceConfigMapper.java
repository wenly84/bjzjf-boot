package site.hansi.module.infra.dal.mysql.db;

import site.hansi.framework.mybatis.core.mapper.BaseMapperX;
import site.hansi.module.infra.dal.dataobject.db.DataSourceConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源配置 Mapper
 *
 * @author 北京智匠坊
 */
@Mapper
public interface DataSourceConfigMapper extends BaseMapperX<DataSourceConfigDO> {
}
