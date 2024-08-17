package site.hansi.module.trade.dal.mysql.config;

import site.hansi.framework.mybatis.core.mapper.BaseMapperX;
import site.hansi.module.trade.dal.dataobject.config.TradeConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 交易中心配置 Mapper
 *
 * @author owen
 */
@Mapper
public interface TradeConfigMapper extends BaseMapperX<TradeConfigDO> {

}
