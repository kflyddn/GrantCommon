package cn.pcshao.pic.service;

import cn.pcshao.grant.common.bo.MailSystemBo;
import cn.pcshao.grant.common.entity.AlbumPicPersonal;
import cn.pcshao.grant.common.entity.AlbumPicPublic;

/**
 * 相册系统服务接口
 * @author pcshao.cn
 * @date 2018/12/13
 */
public interface AlbumSystemService {

    /**
     * 条件计数公共图片表统计
     *  可见or不可见
     *  ...
     * @param condition
     * @return
     */
    int countPicPublic(AlbumPicPublic condition);

    /**
     * 条件计数私有图片表统计
     *  私有or不私有
     * @param condition
     * @return
     */
    int countPicPersonal(AlbumPicPersonal condition);

    /**
     * 获取邮件服务状态
     * @return
     */
    MailSystemBo getMailStatus();
}
