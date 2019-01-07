package cn.pcshao.graduaction.service;

import cn.pcshao.grant.common.base.BaseService;
import cn.pcshao.grant.common.bo.TaskBo;
import cn.pcshao.grant.common.entity.GrantTask;

import java.util.List;

/**
 * @author pcshao.cn
 * @date 2019/1/7
 */
public interface TaskService extends BaseService<GrantTask, Integer> {

    /**
     * 条件查询任务列表
     * @param taskBo
     * @return
     */
    List<GrantTask> listTask(TaskBo taskBo);

    /**
     * 停止任务
     * @param taskId
     */
    void stopTask(List<Integer> taskId);

    /**
     * 启动任务、重启任务
     * @param taskId
     */
    void startTask(List<Integer> taskId);
}
