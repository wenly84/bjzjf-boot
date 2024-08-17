package site.hansi.module.pay.service.notify;

import site.hansi.framework.common.pojo.PageResult;
import site.hansi.module.pay.controller.admin.notify.vo.PayNotifyTaskPageReqVO;
import site.hansi.module.pay.dal.dataobject.notify.PayNotifyLogDO;
import site.hansi.module.pay.dal.dataobject.notify.PayNotifyTaskDO;

import java.util.List;

/**
 * 回调通知 Service 接口
 *
 * @author 北京智匠坊
 */
public interface PayNotifyService {

    /**
     * 创建回调通知任务
     *
     * @param type 类型
     * @param dataId 数据编号
     */
    void createPayNotifyTask(Integer type, Long dataId);

    /**
     * 执行回调通知
     *
     * 注意，该方法提供给定时任务调用。目前是 bjzjf-server 进行调用
     * @return 通知数量
     */
    int executeNotify() throws InterruptedException;

    /**
     * 获得回调通知
     *
     * @param id 编号
     * @return 回调通知
     */
    PayNotifyTaskDO getNotifyTask(Long id);

    /**
     * 获得回调通知分页
     *
     * @param pageReqVO 分页查询
     * @return 回调通知分页
     */
    PageResult<PayNotifyTaskDO> getNotifyTaskPage(PayNotifyTaskPageReqVO pageReqVO);

    /**
     * 获得回调日志列表
     *
     * @param taskId 任务编号
     * @return 日志列表
     */
    List<PayNotifyLogDO> getNotifyLogList(Long taskId);

}
