package cn.pcshao.graduaction.service.impl;

import cn.pcshao.graduaction.service.TaskService;
import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseServiceImpl;
import cn.pcshao.grant.common.bo.TaskBo;
import cn.pcshao.grant.common.dao.GrantTaskMapper;
import cn.pcshao.grant.common.entity.GrantTask;
import cn.pcshao.grant.common.entity.GrantTaskExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pcshao.cn
 * @date 2019/1/7
 */
@Service("taskServiceImpl")
public class TaskServiceImpl extends BaseServiceImpl<GrantTask, Integer> implements TaskService {

    @Resource
    private GrantTaskMapper grantTaskMapper;

    @Override
    public BaseDao<GrantTask, Integer> getDao() {
        return grantTaskMapper;
    }

    @Override
    public List<GrantTask> listTask(TaskBo taskBo) {
        GrantTaskExample example = new GrantTaskExample();
        if(null != taskBo) {
            GrantTaskExample.Criteria criteria = example.createCriteria();
            if(null != taskBo.getState()){
                criteria.andStateEqualTo(taskBo.getState());
            }
            if(null != taskBo.getType()){
                criteria.andTypeEqualTo(taskBo.getType());
            }
        }
        List<GrantTask> grantTasks = grantTaskMapper.selectByExample(example);
        return grantTasks;
    }

    @Override
    public void stopTask(List<Integer> taskId) {
        GrantTaskExample example = new GrantTaskExample();
        GrantTask task = new GrantTask();
        if(null != taskId){
            GrantTaskExample.Criteria criteria = example.createCriteria();
            criteria.andTaskIdIn(taskId);
            task.setState((byte) 0);
            task.setProcess((short) -1);
        }
        grantTaskMapper.updateByExampleSelective(task, example);
    }

    @Override
    public void startTask(List<Integer> taskId) {
        GrantTaskExample example = new GrantTaskExample();
        GrantTask task = new GrantTask();
        if(null != taskId){
            GrantTaskExample.Criteria criteria = example.createCriteria();
            criteria.andTaskIdIn(taskId);
            task.setState((byte) 3);
        }
        grantTaskMapper.updateByExampleSelective(task, example);
    }
}
