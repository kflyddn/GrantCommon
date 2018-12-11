package cn.pcshao.pic.task;

import cn.pcshao.grant.common.entity.AlbumPicPublic;
import cn.pcshao.grant.common.util.FtpUtil;
import cn.pcshao.pic.service.AlbumSourceService;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author pcshao.cn
 * @date 2018/12/11
 */
@Configuration
@EnableScheduling
@PropertySource({"classpath:album.properties"})
public class AutoClearFtpTask {

    private Logger logger = LoggerFactory.getLogger(AutoClearFtpTask.class);

    @Resource
    @Qualifier("picService")
    private AlbumSourceService picService;

    @Value("${task.autoclearftp.path}")
    private String path;
    @Value("${file.service.address}")
    private String ftp_address;
    @Value("${file.service.port}")
    private String ftp_port;
    @Value("${file.service.username}")
    private String ftp_username;
    @Value("${file.service.password}")
    private String ftp_password;
    @Value("${task.autoclearftp.pic}")
    private String taskSwitch;

    @Scheduled(cron = "${task.autoclearftp.cron}")
    public void execute(){
        if("FALSE".equals(taskSwitch))
            return;
        logger.info("AutoClearFtpTask定时任务开始执行！");
        logger.info("开始同步删除FTP中未被物理删除的文件...");
        //清理public目录下的文件
        AlbumPicPublic condition = new AlbumPicPublic();
        condition.setName("TO_DELETE");
        List<AlbumPicPublic> picPublicList = picService.getPicPublic(condition);
        logger.info("要删除的记录数量为："+ picPublicList.size());
        //初始化FTPUtil
        FtpUtil ftpUtil = new FtpUtil(ftp_address, Integer.parseInt(ftp_port), ftp_username, ftp_password);
        FTPClient ftpClient = null;
        try {
            ftpClient = ftpUtil.getFtpClient();
            //删除FTP上name被标识为TO_DELETE的图片
            ftpUtil.clearFtp(ftpClient, path, picPublicList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //删除DB上的
        logger.info("开始同步删除DB中未被真实删除的记录...");
        int count = 0;
        for(AlbumPicPublic pic : picPublicList) {
            count += picService.getPublicPicMapper().deleteByPrimaryKey(pic.getId());
        }
        logger.info("删除记录数量为："+ count);
        logger.info("AutoClearFtpTask定时任务执行完成！");
    }
}
