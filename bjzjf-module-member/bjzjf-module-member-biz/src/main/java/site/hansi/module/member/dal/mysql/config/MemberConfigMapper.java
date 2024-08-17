package site.hansi.module.member.dal.mysql.config;

import site.hansi.framework.mybatis.core.mapper.BaseMapperX;
import site.hansi.module.member.dal.dataobject.config.MemberConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 积分设置 Mapper
 *
 * @author QingX
 */
@Mapper
public interface MemberConfigMapper extends BaseMapperX<MemberConfigDO> {
}
