package cn.pcshao.pic.service;

import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseService;
import cn.pcshao.grant.common.bo.AlbumSource;
import cn.pcshao.grant.common.entity.AlbumPicPersonal;
import cn.pcshao.grant.common.entity.AlbumPicPublic;
import cn.pcshao.pic.ao.ResultFtp;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author pcshao.cn
 * @date 2018/12/4
 */
public interface AlbumSourceService extends BaseService<AlbumSource, Integer> {

    /**
     * 上传文件接口
     * @param file
     * @return
     */
    ResultFtp upLoadFile(MultipartFile file, String appendPath) throws IOException;

    /**
     * 获得公共图片mapper
     * @return
     */
    BaseDao getPublicPicMapper();

    /**
     * 获得私有图片mapper
     * @return
     */
    BaseDao getPersonalPicMapper();

    /**
     * 获得公共图片列表
     * @return
     */
    List<AlbumPicPublic> getPicPublic(AlbumPicPublic albumPicPublic);

    /**
     * 获得私有图片列表
     * @return
     */
    List<AlbumPicPersonal> getPicPersonal(String username);

}
