package cn.pcshao.pic.service.impl;

import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseServiceImpl;
import cn.pcshao.grant.common.bo.AlbumSource;
import cn.pcshao.grant.common.dao.AlbumPicPersonalMapper;
import cn.pcshao.grant.common.dao.AlbumPicPublicMapper;
import cn.pcshao.grant.common.entity.AlbumPicPersonal;
import cn.pcshao.grant.common.entity.AlbumPicPersonalExample;
import cn.pcshao.grant.common.entity.AlbumPicPublic;
import cn.pcshao.grant.common.entity.AlbumPicPublicExample;
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
import java.util.List;

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
    private String ftp_password;
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
    public List<AlbumPicPublic> getPicPublic(AlbumPicPublic albumPicPublic) {
        AlbumPicPublicExample example = new AlbumPicPublicExample();
        AlbumPicPublicExample.Criteria criteria = example.createCriteria();
        if(null != albumPicPublic){
            String username,userNickname;
            String name,describe;
            if(StringUtils.isNotEmpty(username = albumPicPublic.getUserName())){
                criteria.andUserNameEqualTo(username);
            }
            if(StringUtils.isNotEmpty(userNickname = albumPicPublic.getUserNickname())){
                criteria.andUserNicknameLike(userNickname);
            }
            if(StringUtils.isNotEmpty(name = albumPicPublic.getName())){
                criteria.andNameLike(name);
            }
            if(StringUtils.isNotEmpty(describe = albumPicPublic.getDescrib())){
                criteria.andDescribLike(describe);
            }
        }
        return picPublicMapper.selectByExample(example);
    }

    @Override
    public List<AlbumPicPersonal> getPicPersonal(String username) {
        AlbumPicPersonalExample example = new AlbumPicPersonalExample();
        example.createCriteria().andUserNameEqualTo(username);
        return picPersonalMapper.selectByExample(example);
    }

    @Override
    public ResultFtp upLoadFile(MultipartFile file, String appendPath) throws IOException {
        ResultFtp resultFtp = new ResultFtp();
        if(Double.parseDouble(alertDiskSize) > FtpUtil.checkEmptyDiskSize()) {
            resultFtp.setData("可用FTP服务器硬盘空间不足！"+FtpUtil.checkEmptyDiskSize());
            return resultFtp;
        }
        //存储文件大小
        float fileSize = (float) (Math.floor(file.getSize() * 100) / 1024 / 100);
        resultFtp.setFilesize(fileSize);
        //创建和配置FTPClient
        FtpUtil ftpUtil = new FtpUtil(ftp_address, Integer.parseInt(ftp_port), ftp_username, ftp_password);
        FTPClient ftp = ftpUtil.getFtpClient();
        if(null == ftp){
            resultFtp.setData("FTP服务器连接失败");
            return resultFtp;
        }
        //开始上传，整理参数
        String to_ftpPath = ftp_path;
        if(StringUtils.isNotEmpty(appendPath)){
            to_ftpPath += appendPath;
        }
        String filename = System.currentTimeMillis() + file.getOriginalFilename();
        InputStream inputstream = file.getInputStream();
        boolean upSucess = ftpUtil.upLoadPic(ftp, to_ftpPath, filename, inputstream);
        //返回ftp地址
        if (upSucess) {
            resultFtp.setFlag(true);
            resultFtp.setLocalPath(to_ftpPath +"/"+ filename);
            resultFtp.setFtpPath("ftp://" + ftp_address + ":" + ftp_port + "/" + ftp_path + "/" + filename);
            return resultFtp;
        }
        resultFtp.setFlag(false);
        return resultFtp;
    }
}
