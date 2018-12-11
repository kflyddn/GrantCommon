package cn.pcshao.grant.common.util;

import cn.pcshao.grant.common.entity.AlbumPicPublic;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * FTP工具类
 * @author pcshao.cn
 * @date 2018/12/5
 */
public class FtpUtil {

    Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    private FTPClient ftpClient;
    private String IP;
    private int PORT;
    private String FTP_USER;
    private String FTP_PASSWD;

    public FtpUtil(String ip, int port, String username, String password){
        this.IP = ip;
        this.PORT = port;
        this.FTP_USER = username;
        this.FTP_PASSWD = password;
    }

    /**
     * 获得FTPClient
     * @return
     * @throws IOException
     */
    public FTPClient getFtpClient() throws IOException {
        ftpClient = new FTPClient();
        ftpClient.connect(IP, PORT);
        //中文
        ftpClient.setControlEncoding("GBK");
        FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
        conf.setServerLanguageCode("zh");
        //登录
        logger.info("正在登录到" +IP);
        ftpClient.login(FTP_USER, FTP_PASSWD);
        logger.info("成功登录到" +IP);
        if(!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
            ftpClient.disconnect();
            logger.info(IP+ "断开连接");
            return null;
        }
        return ftpClient;
    }

    /**
     * 上传图片
     * @param ftpClient
     * @param path 服务器目标路径
     * @param filename 文件名
     * @param inputStream 被传文件流
     * @return
     */
    public boolean upLoadPic(FTPClient ftpClient, String path, String filename, InputStream inputStream){
        boolean target = false;
        try {
            ftpClient.changeWorkingDirectory(path);
            //编码文件目录与文件名
            filename = new String(filename.getBytes("GBK"), "ISO-8859-1");
            if (StringUtils.isNotEmpty(path))
            {
                path = new String(path.getBytes("GBK"), "ISO-8859-1");
            }
            //设置缓冲大小1024 =1M
            ftpClient.setBufferSize(409600);
            //转到指定上传目录
            ftpClient.changeWorkingDirectory(path);
            //将上传文件存储到指定目录
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            //主动模式被动模式
            //ftpClient.enterLocalPassiveMode();
            ftpClient.enterLocalActiveMode();
            target = ftpClient.storeFile(filename, inputStream);
            logger.info(ftpClient.getReplyCode() + ftpClient.getReplyString());
            //关闭输入流
            inputStream.close();
            //退出ftp
            ftpClient.logout();
            //表示上传成功
        } catch (IOException e) {
            e.printStackTrace();
        }
        return target;
    }

    public static double checkEmptyDiskSize(){
        //TODO 检查FTP服务器可用磁盘空间
        return 2048000;
    }

    /**
     * 删除资源文件
     * @param ftpClient
     * @param path
     * @param picPublicList
     */
    public void clearFtp(FTPClient ftpClient, String path, List<AlbumPicPublic> picPublicList){
        try {
            ftpClient.changeWorkingDirectory(path);
            //设置缓冲大小1024 =1M
            ftpClient.setBufferSize(409600);
            //主动模式
            ftpClient.enterLocalActiveMode();
            //删除文件
            int count = 0;
            for(AlbumPicPublic pic : picPublicList) {
                String picPath = pic.getPathLocal();
                logger.info("正在删除文件："+ picPath);
                if(ftpClient.deleteFile(picPath))
                    count++;
                logger.info("删除成功！"+ count);
            }
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
