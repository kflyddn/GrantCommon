package cn.pcshao.pic.service.impl;

import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseServiceImpl;
import cn.pcshao.grant.common.bo.AlbumSource;
import cn.pcshao.grant.common.dao.AlbumPicPersonalMapper;
import cn.pcshao.grant.common.dao.AlbumPicPublicMapper;
import cn.pcshao.pic.ao.ResultFtp;
import cn.pcshao.pic.service.AlbumSourceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 图片资源上传服务
 * @author pcshao.cn
 * @date 2018/12/4
 */
@Service("picService")
public class PicServiceImpl extends BaseServiceImpl<AlbumSource, Integer> implements AlbumSourceService {

    @Resource
    private AlbumPicPersonalMapper picPersonalMapper;
    @Resource
    private AlbumPicPublicMapper picPublicMapper;

    @Override
    public BaseDao<AlbumSource, Integer> getDao() {
        return picPublicMapper;
    }

    @Override
    public ResultFtp upLoadFile(MultipartFile file) {
        //TODO 检查FTP磁盘空间 搭建FTP服务器

        return null;
    }
}
