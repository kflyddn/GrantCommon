package cn.pcshao.graduaction.task;

import cn.pcshao.graduaction.util.HadoopUtil;
import cn.pcshao.grant.common.consts.DBConsts;
import cn.pcshao.grant.common.dao.GrantHuserMapper;
import cn.pcshao.grant.common.dao.GrantM2hStateMapper;
import cn.pcshao.grant.common.entity.GrantHuser;
import cn.pcshao.grant.common.entity.GrantHuserExample;
import cn.pcshao.grant.common.entity.GrantM2hState;
import cn.pcshao.grant.common.util.FileUtils;
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
import java.util.ArrayList;
import java.util.Date;
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
    @Value("${task.Mysql2Hdfs.recoverFilePath}")
    private String recoverFilePath;
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
    private boolean recoverDone = false;

    @Scheduled(cron = "${task.Mysql2Hdfs.cron}")
    public void read(){
        if("OFF".equals(taskSwitch))
            return;
        if("RECOVER".equals(taskSwitch))
            recover();
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
                hStateMapper.insertBatch(list, DBConsts.M2H_STATE_DONE_1);
            }
            i += sub;
            file.deleteOnExit();
        }
    }

    /**
     * 恢复模式
     *  将HDFS中的数据逆向到DB并在m2h_state表中置为2
     */
    public void recover() {
        logger.debug("正在开始Mysql2Hdfs: RECOVER模式");
        if(recoverDone) {
            logger.info("RECOVER 已完成，请关闭系统并检查数据库与本地文件目录：" + recoverFilePath);
            return;
        }
        FileUtils.deleteDir(recoverFilePath);
        List<Path> pathList = read4hdfs();
        for (int i = 0; i < pathList.size(); i++) {
            Path path = pathList.get(i);
            String localPath = recoverFilePath+ path.getName();
            pull4hdfs(path, localPath);
            List<GrantHuser> husers = file2obj(localPath);
            GrantM2hState stateRecord;
            for(GrantHuser huser : husers) {
                stateRecord = new GrantM2hState(huser.getHuserId(), DBConsts.M2H_STATE_REC_2, new Date());
                try {
                    huserMapper.insert(huser);
                    hStateMapper.insert(stateRecord);
                } catch (Exception e) {
                    stateRecord.setState(DBConsts.M2H_STATE_ALREADY_3);
                    hStateMapper.insert(stateRecord);
                }
            }
        }
        recoverDone = true;
    }

    /**
     * 根据远程path拉到本地path
     * @param path DFS path
     * @param localPath 本地 path
     */
    private void pull4hdfs(Path path, String localPath) {
        if(null == hadoopUtil) {
            Configuration conf = new Configuration();
            hadoopUtil = new HadoopUtil(hadoopURI, conf);
        }
        hadoopUtil.copyToLocalFile(path, localPath);
    }

    private List<Path> read4hdfs() {
        if(null == hadoopUtil) {
            Configuration conf = new Configuration();
            hadoopUtil = new HadoopUtil(hadoopURI, conf);
        }
        return hadoopUtil.getPathFromDFS(hdfsLocatePath, false);
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
     * DFS文件结构化元素读成对象
     * @param filePath 输入文件
     * @return 对象数组
     */
    public List<GrantHuser> file2obj(String filePath) {
        List<GrantHuser> list = new ArrayList<>();
        GrantHuser huser;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        try {
            String ret;
            while ((ret=br.readLine()) != null){
                huser = new GrantHuser();
                String[] items = ret.split(" ");
                huser.setHuserId(Long.parseLong(items[0]));
                huser.setUserId(Long.parseLong(items[1]));
                huser.setIdCard(items[2]);
                huser.setName(items[3]);
                huser.setSex(items[4].equals("true")? true : false);
                huser.setTelephone(items[5]);
                huser.setEmail(items[6]);
                huser.setAddress(items[7]);
                list.add(huser);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
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
        for (GrantHuser huser: husers){
            StringBuilder sb = new StringBuilder();
            sb.append(huser.getHuserId()+ " ");
            sb.append(huser.getUserId()+ " ");
            sb.append(huser.getIdCard()+ " ");
            sb.append(huser.getName()+ " ");
            sb.append(huser.getSex()+ " ");
            sb.append(huser.getTelephone()+ " ");
            sb.append(huser.getEmail()+ " ");
            sb.append(huser.getAddress()+ " ");
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
