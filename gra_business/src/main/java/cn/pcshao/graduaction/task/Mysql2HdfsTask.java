package cn.pcshao.graduaction.task;

import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dao.GrantHuserMapper;
import cn.pcshao.grant.common.dao.GrantM2hStateMapper;
import cn.pcshao.grant.common.entity.GrantHuser;
import cn.pcshao.grant.common.entity.GrantHuserExample;
import cn.pcshao.grant.common.exception.CustomException;
import cn.pcshao.grant.common.util.ListUtils;
import cn.pcshao.grant.common.util.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
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
        for (int i = 0; i < size;) {
            File file = obj2file(husers, tempFilePath);
            //TODO 附加非结构化数据
            logger.debug("开始写入HDFS");
            if(write2hdfs(file, hdfsLocatePath)) {
                logger.debug("更新同步记录");
                List<GrantHuser> list;
                if((i+sub) > size){
                    list = husers.subList(i, size);
                }else {
                    list = husers.subList(i, i + sub);
                }
                hStateMapper.insertBatch(list);
                i += sub;
            }
            file.deleteOnExit();
        }
    }

    private boolean write2hdfs(File file, String dstPath) {
        URI uri = null;
        try {
            uri = new URI(hadoopURI);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Configuration conf = new Configuration();
        FileSystem fs = null;
        String fileName = System.currentTimeMillis()+ StringUtils.getRandomString(5);
        try {
            fs = FileSystem.get(uri, conf);
            fs.copyFromLocalFile(new Path(file.getAbsolutePath()), new Path(dstPath+ fileName));
            return true;
        } catch (IOException e) {
            throw new CustomException(DtoCodeConsts.HADOOP_HDFS_CONNECT_FAIL, DtoCodeConsts.HADOOP_CONNECT_FAIL_MSG);
        }finally {
            try {
                fs.close();
            } catch (IOException e) {
            }
        }
    }

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
