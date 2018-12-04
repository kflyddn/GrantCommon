package cn.pcshao.pic.service;

import cn.pcshao.grant.common.base.BaseService;
import cn.pcshao.grant.common.bo.AlbumSource;
import cn.pcshao.pic.ao.ResultFtp;
import org.springframework.web.multipart.MultipartFile;

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
    ResultFtp upLoadFile(MultipartFile file);

}
