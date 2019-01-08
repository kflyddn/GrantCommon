package cn.pcshao.graduaction.task;

import cn.pcshao.graduaction.service.HUserService;
import cn.pcshao.grant.common.dao.GrantHuserMapper;
import cn.pcshao.grant.common.dao.GrantTaskMapper;
import cn.pcshao.grant.common.entity.GrantHuserExample;
import cn.pcshao.grant.common.entity.GrantTask;
import cn.pcshao.grant.common.entity.GrantTaskExample;
import cn.pcshao.grant.common.util.JSONUtils;
import cn.pcshao.grant.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author pcshao.cn
 * @date 2019/1/8
 */
@Component
@PropertySource({"classpath:business.properties"})
public class AnalysisHUserTask {

    private Logger logger = LoggerFactory.getLogger(AnalysisHUserTask.class);

    //结果集
    public Map resultMap;
    //任务队列
    private Queue<GrantTask> taskQueue = new LinkedList<>();

    @Autowired
    @Qualifier("hUserServiceImpl")
    private HUserService hUserService;

    @Resource
    private GrantHuserMapper huserMapper;
    @Resource
    private GrantTaskMapper taskMapper;

    @Value("${task.AnalysisHUser.switch}")
    private String taskSwitch;

    @Resource
    @Qualifier("taskExecutor")
    private TaskExecutor taskExecutor;

    @Scheduled(cron = "${task.AnalysisHUser.readCron}")
    public void read(){
        if("OFF".equals(taskSwitch))
            return;
        GrantTaskExample example = new GrantTaskExample();
        GrantTaskExample.Criteria criteria = example.createCriteria();
        //将状态为3的读进待处理的任务列表
        criteria.andStateEqualTo((byte) 3);
        List<GrantTask> tasks = taskMapper.selectByExample(example);
        //遍历Set
        for(GrantTask task : tasks){
            if(!taskQueue.contains(task)){
                taskQueue.add(task);
            }
        }
    }

    @Scheduled(cron = "${task.AnalysisHUser.consumeCron}")
    public void consume(){
        GrantTask currTask = taskQueue.poll();
        if(null == currTask)
            return;
        String param = currTask.getParam();
        if(StringUtils.isNotEmpty(param)) {
            Map<String, Object> fromJson = JSONUtils.getMapFromJson(param);
            if(fromJson.get("countName") != null){
                //Map中可以嵌套map
                addCountNameTask(currTask, (String) fromJson.get("countName"));
            }
            if(fromJson.get("test") != null){
                //GO ON
            }
        }
    }

    private void addCountNameTask(GrantTask currTask, String name){
        logger.debug("开启新线程运算countName任务中...");
        taskExecutor.execute(() -> countName(currTask, name));
    }

    //APP 需要做成多线程的，执行完毕返回后再去使任务记录标识变化
    private void countName(GrantTask currTask, String name){
        //任务进行
        currTask.setState((byte) 2);
        taskMapper.updateByPrimaryKeySelective(currTask);
        logger.info("开始统计姓名任务！");
        Map map = new HashMap<String, Object>(); //后期多线程下需要考虑线程安全问题
        GrantHuserExample example1 = new GrantHuserExample();
          example1.createCriteria().andNameLike(name + "%");
        GrantHuserExample example2 = new GrantHuserExample();
          example2.createCriteria().andNameLike("%"+ name + "%");
        GrantHuserExample example3 = new GrantHuserExample();
          example3.createCriteria().andNameLike("%"+ name);
        int firstName = huserMapper.countByExample(example1);
        map.put("firstName", firstName);
        int containName = huserMapper.countByExample(example2);
        map.put("containName", containName);
        int lastName = huserMapper.countByExample(example3);
        map.put("lastName", lastName);
        logger.info(JSONUtils.getJsonFromMap(map));
        //经过线程休眠模拟任务执行时间不同，此处线程池可以达到多个任务同步进行并实时更新任务状态
        //TODO 持久化或者直接写入hadoop
        //任务完成
        currTask.setState((byte) 1);
        currTask.setProcess((short) 100);
        taskMapper.updateByPrimaryKeySelective(currTask);
    }

}
