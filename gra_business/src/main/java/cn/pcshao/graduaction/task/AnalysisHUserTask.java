package cn.pcshao.graduaction.task;

import cn.pcshao.graduaction.task.hadoop.HTaskTypeFactory;
import cn.pcshao.graduaction.task.hadoop.WordCount;
import cn.pcshao.grant.common.consts.DBConsts;
import cn.pcshao.grant.common.dao.GrantHuserMapper;
import cn.pcshao.grant.common.dao.GrantTaskMapper;
import cn.pcshao.grant.common.dao.GrantTaskResultMapper;
import cn.pcshao.grant.common.entity.GrantHuserExample;
import cn.pcshao.grant.common.entity.GrantTask;
import cn.pcshao.grant.common.entity.GrantTaskExample;
import cn.pcshao.grant.common.entity.GrantTaskResult;
import cn.pcshao.grant.common.util.JSONUtils;
import cn.pcshao.grant.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @author pcshao.cn
 * @date 2019/1/8
 */
@Component
@PropertySource({"classpath:business.properties"})
public class AnalysisHUserTask {

    private Logger logger = LoggerFactory.getLogger(AnalysisHUserTask.class);

    //任务队列
    private Queue<GrantTask> taskQueue = new LinkedList<>();

    @Resource
    private GrantHuserMapper huserMapper;
    @Resource
    private GrantTaskMapper taskMapper;
    @Resource
    private GrantTaskResultMapper taskResultMapper;

    @Value("${task.AnalysisHUser.switch}")
    private String taskSwitch;
    @Value("${task.AnalysisHUser.useHadoop}")
    private String useHadoop;
    @Value("${task.Mysql2Hdfs.hdfsLocate}")
    private String hdfsLocatePath;
    @Value("${task.Mysql2Hdfs.hadoopURI}")
    private String hadoopURI;
    @Value("${task.Mysql2Hdfs.defaultOutputPath}")
    private String defaultOutputPath;

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
                //任务进行操作DB更改状态，越早越好，消费一条立马改一条防止重复生成
                currTask.setState(DBConsts.TASK_STATE_ING_2);
                taskMapper.updateByPrimaryKeySelective(currTask);
                if("OFF".equals(useHadoop)) {
                    addCountNameTask(currTask, (String) fromJson.get("countName"));
                }else{
                    //计算名称 hadoop方式 未实现
                    addCountNameTask(currTask, (String) fromJson.get("countName"));
                }
            }
            if(fromJson.get("wordCount") != null){
                currTask.setState(DBConsts.TASK_STATE_ING_2);
                taskMapper.updateByPrimaryKeySelective(currTask);
                String outputName = defaultOutputPath+ System.currentTimeMillis();
                if(null != fromJson.get("outputPath")){
                    outputName = (String) fromJson.get("outputPath");
                }
                addWordCountHTask(currTask, outputName);
            }
            if(fromJson.get("test") != null){
                //TODO 不同的任务处理方式 GO ON
            }
        }
    }

    private void addWordCountHTask(GrantTask currTask, String outputPath){
        logger.debug("开启新线程运算基于Haadoop的任务中...");
        taskExecutor.execute(() -> {
            try {
                HTaskTypeFactory.getFacJob(WordCount.Map.class, WordCount.Reduce.class,
                        hadoopURI+ hdfsLocatePath, outputPath)  //HDFS 默认读同步到hdfs目录下的所有输入文件，需要增加一个初始化hdfs状态的接口
                        .waitForCompletion(true);
                GrantTaskResult taskResult = new GrantTaskResult();
                taskResult.setF1("任务结果路径："+ outputPath);
                taskResult.setTaskId(currTask.getTaskId());
                taskResult.setCreateTime(new Date());
                taskResultMapper.insert(taskResult);
                currTask.setState(DBConsts.M2H_STATE_DONE_1);
                currTask.setProcess((short) 100);
                taskMapper.updateByPrimaryKeySelective(currTask);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void addCountNameTask(GrantTask currTask, String name){
        logger.debug("开启新线程运算countName任务中...");
        taskExecutor.execute(() -> countName(currTask, name));
    }

    //APP 需要做成多线程的，执行完毕返回后再去使任务记录标识变化
    private void countName(GrantTask currTask, String name){
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
        //经过线程休眠模拟任务执行时间不同，此处线程池可以达到多个任务同步进行并实时更新任务状态
        //TODO 持久化或者直接写入hadoop 多线程问题待考究
        GrantTaskResult taskResult = new GrantTaskResult();
        taskResult.setTaskId(currTask.getTaskId());
        taskResult.setCreateTime(new Date());
        taskResult.setF1(JSONUtils.getJsonFromMap(map));
        taskResultMapper.insert(taskResult);
        //任务完成状态写入DB
        currTask.setState(DBConsts.M2H_STATE_DONE_1);
        currTask.setProcess((short) 100);
        taskMapper.updateByPrimaryKeySelective(currTask);
    }

}
