package cn.pcshao.graduaction.task;

import cn.pcshao.grant.common.dao.GrantHuserMapper;
import cn.pcshao.grant.common.dao.GrantM2hStateMapper;
import cn.pcshao.grant.common.entity.GrantHuser;
import cn.pcshao.grant.common.entity.GrantHuserExample;
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
        File file = obj2file(husers, tempFilePath);
        //附加非结构化数据
        //文本与非结构化数据一同写入hdfs
        write2hdfs(file, hdfsLocatePath);
        //更新mysql
//        hStateMapper.insertBatch(husers);
    }

    private void write2hdfs(File file, String dstPath) {
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
            //TODO 检测是否连接上
            fs.copyFromLocalFile(new Path(file.getAbsolutePath()), new Path(dstPath+ fileName));
        } catch (IOException e) {
            e.printStackTrace();
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
