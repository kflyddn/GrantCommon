package cn.pcshao.graduaction.task;

import cn.pcshao.graduaction.util.HadoopUtil;
import cn.pcshao.grant.common.dao.GrantHuserMapper;
import cn.pcshao.grant.common.dao.GrantM2hStateMapper;
import cn.pcshao.grant.common.entity.GrantHuser;
import cn.pcshao.grant.common.entity.GrantHuserExample;
import cn.pcshao.grant.common.util.ListUtils;
import cn.pcshao.grant.common.util.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

/**
 * Mysql关系型数据库同步HUser数据到HDFS分布式文件系统
 *  1.抽取结构化文本流输出
 *  2.附加非结构化大数据便于直观感受
 *  阻塞式的，读完表（未分页）后写入hdfs（同步）
 * @author pcshao.cn
 * @date 2019-02-27
 */
@Component
@PropertySource({"classpath:business.properties"})
public class Mysql2HdfsTask {

    private Logger logger = LoggerFactory.getLogger(AnalysisHUserTask.class);

    @Value("${task.Mysql2Hdfs.switch}")
    private String taskSwitch;
    @Value("${task.Mysql2Hdfs.tempFilePath}")
    private String tempFilePath;
    @Value("${task.Mysql2Hdfs.hadoopURI}")
    private String hadoopURI;
    @Value("${task.Mysql2Hdfs.hdfsLocate}")
    private String hdfsLocatePath;
    @Value("${task.Mysql2Hdfs.perSynCount}")
    private String perSynCount;

    @Resource
    private GrantHuserMapper huserMapper;
    @Resource
    private GrantM2hStateMapper hStateMapper;

    private HadoopUtil hadoopUtil = null;

    @Scheduled(cron = "${task.Mysql2Hdfs.cron}")
    public void read(){
        if("OFF".equals(taskSwitch))
            return;
        logger.debug("正在开始Mysql2Hdfs");
        GrantHuserExample huserExample = new GrantHuserExample();
        Long maxHUserId = hStateMapper.getMaxHUserId();
        if(null != maxHUserId) {
            GrantHuserExample.Criteria criteria = huserExample.createCriteria();
            criteria.andHuserIdGreaterThan(maxHUserId);
        }
        List<GrantHuser> husers = huserMapper.selectByExample(huserExample);
        if(ListUtils.isEmptyList(husers)) {
            return;
        }
        int sub = Integer.parseInt(perSynCount);
        int size = husers.size();
        logger.info("正在写入HDFS");
        for (int i = 0; i < size;) {
            //TODO 附加非结构化数据
            List<GrantHuser> list;
            if((i+sub) > size){
                list = husers.subList(i, size);
            }else {
                list = husers.subList(i, i + sub);
            }
            File file = obj2file(list, tempFilePath);
            if(write2hdfs(file, hdfsLocatePath)) {
                hStateMapper.insertBatch(list);
            }
            i += sub;
            file.deleteOnExit();
        }
    }

    /**
     * 持有的hadoopUtil在第一次执行write2hdfs时实例化
     *  直到手动设为null，不过一般应用生命周期内都不会改变hadoop地址吧
     * @param file 文件对象
     * @param dstPath dfs目的地地址
     * @return
     */
    private boolean write2hdfs(File file, String dstPath) {
        if(null == hadoopUtil) {
            Configuration conf = new Configuration();
            hadoopUtil = new HadoopUtil(hadoopURI, conf);
        }
        String fileName = System.currentTimeMillis()+ StringUtils.getRandomString(5);
        hadoopUtil.copyFromLocalFile(file.getAbsolutePath(), dstPath+ fileName);
        return true;
    }

    /**
     * 对象手动文件化
     * @param husers 对象集合
     * @param path 输出文件路径
     * @return 文件对象
     */
    public File obj2file(List<GrantHuser> husers, String path) {
        File file = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fos);
        for (GrantHuser user: husers){
            StringBuilder sb = new StringBuilder();
            sb.append(user.getHuserId()+ " ");
            sb.append(user.getIdCard()+ " ");
            sb.append(user.getName()+ " ");
            sb.append(user.getSex()+ " ");
            sb.append(user.getTelephone()+ " ");
            sb.append(user.getEmail()+ " ");
            sb.append(user.getAddress()+ " ");
            pw.println(sb.toString());
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pw.close();
        if(fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
