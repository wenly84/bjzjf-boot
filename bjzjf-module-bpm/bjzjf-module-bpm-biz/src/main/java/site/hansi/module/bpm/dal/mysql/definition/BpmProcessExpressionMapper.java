package site.hansi.module.bpm.dal.mysql.definition;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.framework.mybatis.core.query.LambdaQueryWrapperX;
import site.hansi.framework.mybatis.core.mapper.BaseMapperX;
import site.hansi.module.bpm.controller.admin.definition.vo.expression.BpmProcessExpressionPageReqVO;
import site.hansi.module.bpm.dal.dataobject.definition.BpmProcessExpressionDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * BPM 流程表达式 Mapper
 *
 * @author 北京智匠坊
 */
@Mapper
public interface BpmProcessExpressionMapper extends BaseMapperX<BpmProcessExpressionDO> {

    default PageResult<BpmProcessExpressionDO> selectPage(BpmProcessExpressionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BpmProcessExpressionDO>()
                .likeIfPresent(BpmProcessExpressionDO::getName, reqVO.getName())
                .eqIfPresent(BpmProcessExpressionDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BpmProcessExpressionDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BpmProcessExpressionDO::getId));
    }

}