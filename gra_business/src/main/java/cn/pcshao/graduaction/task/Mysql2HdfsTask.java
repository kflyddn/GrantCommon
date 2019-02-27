package cn.pcshao.graduaction.task;

import cn.pcshao.grant.common.dao.GrantHuserMapper;
import cn.pcshao.grant.common.dao.GrantM2hStateMapper;
import cn.pcshao.grant.common.entity.GrantHuser;
import cn.pcshao.grant.common.entity.GrantHuserExample;
import cn.pcshao.grant.common.entity.GrantM2hStateExample;
import cn.pcshao.grant.common.exception.CustomException;
import cn.pcshao.grant.common.util.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Mysql关系型数据库同步HUser数据到HDFS分布式文件系统
 *  1.抽取结构化文本流输出
 *  2.附加非结构化大数据便于直观感受
 * @author pcshao.cn
 * @date 2019-02-27
 */
@Component
@PropertySource({"classpath:business.properties"})
public class Mysql2HdfsTask {

    private Logger logger = LoggerFactory.getLogger(AnalysisHUserTask.class);

    @Value("${task.Mysql2Hdfs.switch}")
    private String taskSwitch;

    @Resource
    private GrantHuserMapper huserMapper;
    @Resource
    private GrantM2hStateMapper hStateMapper;

    @Scheduled(cron = "${task.Mysql2Hdfs.cron}")
    public void read(){
        if("OFF".equals(taskSwitch))
            return;
        logger.debug("正在开始Mysql2Hdfs");
        GrantHuserExample huserExample = new GrantHuserExample();
        GrantM2hStateExample stateExample = new GrantM2hStateExample();
    //读取huser存到临时文本
        //TODO 分批次读取 按ID读吧 bigint够用了
        Long maxHUserId = hStateMapper.getMaxHUserId();
        if(null != maxHUserId) {
            GrantHuserExample.Criteria criteria = huserExample.createCriteria();
            criteria.andHuserIdGreaterThan(maxHUserId);
        }
        List<GrantHuser> husers = huserMapper.selectByExample(huserExample);
        //字符串拼接成字符串数组
        if(ListUtils.isEmptyList(husers)) {
            return;
        }
        List<String> dataList = obj2hdfs(husers);
        //附加非结构化数据
        //文本与非结构化数据一同写入hdfs
        
        hStateMapper.insertBatch(husers);
    }

    private List<String> obj2hdfs(List<GrantHuser> husers) {
        List<String> allData = new ArrayList<>();
        for (GrantHuser user: husers){
            StringBuilder sb = new StringBuilder();
            sb.append(user.getHuserId()+ " ");
            sb.append(user.getIdCard()+ " ");
            sb.append(user.getName()+ " ");
            sb.append(user.getSex()+ " ");
            sb.append(user.getTelephone()+ " ");
            sb.append(user.getEmail()+ " ");
            allData.add(sb.toString());
        }
        return allData;
    }
}
