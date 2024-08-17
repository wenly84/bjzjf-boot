package site.hansi.module.system.service.logger;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.module.system.api.logger.dto.OperateLogCreateReqDTO;
import site.hansi.module.system.api.logger.dto.OperateLogPageReqDTO;
import site.hansi.module.system.controller.admin.logger.vo.operatelog.OperateLogPageReqVO;
import site.hansi.module.system.dal.dataobject.logger.OperateLogDO;

/**
 * 操作日志 Service 接口
 *
 * @author 北京智匠坊
 */
public interface OperateLogService {

    /**
     * 记录操作日志
     *
     * @param createReqDTO 创建请求
     */
    void createOperateLog(OperateLogCreateReqDTO createReqDTO);

    /**
     * 获得操作日志分页列表
     *
     * @param pageReqVO 分页条件
     * @return 操作日志分页列表
     */
    PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqVO pageReqVO);

    /**
     * 获得操作日志分页列表
     *
     * @param pageReqVO 分页条件
     * @return 操作日志分页列表
     */
    PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqDTO pageReqVO);

}
