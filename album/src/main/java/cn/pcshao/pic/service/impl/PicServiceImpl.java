package cn.pcshao.pic.service.impl;

import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseServiceImpl;
import cn.pcshao.grant.common.bo.AlbumSource;
import cn.pcshao.grant.common.dao.AlbumPicPersonalMapper;
import cn.pcshao.grant.common.dao.AlbumPicPublicMapper;
import cn.pcshao.grant.common.util.FtpUtil;
import cn.pcshao.grant.common.util.StringUtils;
import cn.pcshao.pic.ao.ResultFtp;
import cn.pcshao.pic.service.AlbumSourceService;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片资源上传服务
 * @author pcshao.cn
 * @date 2018/12/4
 */
@Service("picService")
@PropertySource({"classpath:album.properties"})
public class PicServiceImpl extends BaseServiceImpl<AlbumSource, Integer> implements AlbumSourceService {

    @Resource
    private AlbumPicPersonalMapper picPersonalMapper;
    @Resource
    private AlbumPicPublicMapper picPublicMapper;

    @Value("${file.service.address}")
    private String ftp_address;
    @Value("${file.service.port}")
    private String ftp_port;
    @Value("${file.service.username}")
    private String ftp_username;
    @Value("${file.service.password}")
    private String ftp_passwrod;
    @Value("${file.service.path}")
    private String ftp_path;
    @Value("${file.service.alertDiskSize}")
    private String alertDiskSize;

    @Deprecated
    @Override
    public BaseDao<AlbumSource, Integer> getDao() {
        return picPublicMapper;
    }

    @Override
    public BaseDao<AlbumSource, Integer> getPublicPicMapper(){
        return picPublicMapper;
    }

    @Override
    public BaseDao<AlbumSource, Integer> getPersonalPicMapper(){
        return picPersonalMapper;
    }

    @Override
    public ResultFtp upLoadFile(MultipartFile file, String appendPath) throws IOException {
        ResultFtp resultFtp = new ResultFtp();
        if(Double.parseDouble(alertDiskSize) > FtpUtil.checkEmptyDiskSize()) {
            resultFtp.setData("可用FTP服务器硬盘空间不足！"+FtpUtil.checkEmptyDiskSize());
            return resultFtp;
        }
        String filename = System.currentTimeMillis() + file.getOriginalFilename();
        InputStream inputstream = file.getInputStream();
        float fileSize = (float) (Math.floor(file.getSize() * 100) / 1024 / 100);
        //创建和配置FTPClient
        FtpUtil ftpUtil = new FtpUtil(ftp_address, Integer.parseInt(ftp_port), ftp_username, ftp_passwrod);
        FTPClient ftp = ftpUtil.getFtpClient();
        if(null == ftp){
            resultFtp.setData("FTP服务器连接失败");
            return resultFtp;
        }
        //开始上传，存放路径调整
        String to_ftpPath = ftp_path;
        if(StringUtils.isNotEmpty(appendPath)){
            to_ftpPath += appendPath;
        }
        boolean upSucess = ftpUtil.upLoadPic(ftp, to_ftpPath, filename, inputstream);
        //返回ftp地址
        if (upSucess) {
            resultFtp.setFlag(true);
            resultFtp.setFtpPath("ftp://" + ftp_address + ":" + ftp_port + "/" + ftp_path + "/" + filename);
            //TODO 上传文件大小
            resultFtp.setFilesize(fileSize);
            return resultFtp;
        }
        resultFtp.setFlag(false);
        return resultFtp;
    }
}
