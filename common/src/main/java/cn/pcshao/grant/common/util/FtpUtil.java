package cn.pcshao.grant.common.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;

/**
 * FTP工具类
 * @author pcshao.cn
 * @date 2018/12/5
 */
public class FtpUtil {

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
        ftpClient.login(FTP_USER, FTP_PASSWD);
        if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
            ftpClient.disconnect();
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
            /*filename = new String(filename.getBytes("GBK"),"ISO-8859-1");
            if (StringUtils.isNotEmpty(path))
            {
                path = new String(path.getBytes("GBK"), "ISO-8859-1");
            }*/
            //设置缓冲大小1024 =1M
            ftpClient.setBufferSize(409600);
            //转到指定上传目录
            ftpClient.changeWorkingDirectory(path);
            //将上传文件存储到指定目录
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            //如果缺省该句 传输txt正常 但图片和其他格式的文件传输出现乱码
            ftpClient.storeFile(filename, inputStream);
            //关闭输入流
            inputStream.close();
            //退出ftp
            ftpClient.logout();
            //表示上传成功
            target = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return target;
    }

    public static double checkEmptyDiskSize(){
        //TODO 检查FTP服务器可用磁盘空间
        return 2048000;
    }
}
