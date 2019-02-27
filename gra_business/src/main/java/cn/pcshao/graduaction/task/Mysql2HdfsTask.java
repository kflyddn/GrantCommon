package cn.pcshao.graduaction.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author pcshao.cn
 * @date 2019-02-27
 */
@Component
@PropertySource({"classpath:business.properties"})
public class Mysql2HdfsTask {

    private Logger logger = LoggerFactory.getLogger(AnalysisHUserTask.class);

    @Value("${task.AnalysisHUser.switch}")
    private String taskSwitch;

    @Scheduled(cron = "${task.Mysql2Hdfs.cron}")
    public void read(){
        if("OFF".equals(taskSwitch))
            return;
        logger.debug("正在开始Mysql2Hdfs");

    }
}
