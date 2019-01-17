package cn.pcshao.pic.service.impl;

import cn.pcshao.grant.common.aop.MailAspect;
import cn.pcshao.grant.common.bo.MailSystemBo;
import cn.pcshao.grant.common.dao.AlbumPicPersonalMapper;
import cn.pcshao.grant.common.dao.AlbumPicPublicMapper;
import cn.pcshao.grant.common.entity.AlbumPicPersonal;
import cn.pcshao.grant.common.entity.AlbumPicPersonalExample;
import cn.pcshao.grant.common.entity.AlbumPicPublic;
import cn.pcshao.grant.common.entity.AlbumPicPublicExample;
import cn.pcshao.grant.common.util.PropertiesUtil;
import cn.pcshao.pic.service.AlbumSystemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author pcshao.cn
 * @date 2018/12/13
 */
@Service("albumSystemService")
public class AlbumSystemServiceImpl implements AlbumSystemService {

    @Resource
    private AlbumPicPublicMapper picPublicMapper;
    @Resource
    private AlbumPicPersonalMapper picPersonalMapper;

    @Override
    public int countPicPublic(AlbumPicPublic condition) {
        AlbumPicPublicExample example = new AlbumPicPublicExample();
        if(null != condition) {
            if(null != condition.getDisplay()) {
                example.createCriteria().andDisplayEqualTo(condition.getDisplay());
            }
        }
        return picPublicMapper.countByExample(example);
    }

    @Override
    public int countPicPersonal(AlbumPicPersonal condition) {
        AlbumPicPersonalExample example = new AlbumPicPersonalExample();
        if(null != condition){
            if(null != condition.getIsPrivate()){
                example.createCriteria().andIsPrivateEqualTo(condition.getIsPrivate());
            }
        }
        return picPersonalMapper.countByExample(example);
    }

    @Override
    public MailSystemBo getMailStatus() {
        MailSystemBo bo = new MailSystemBo();
        bo.setToMailAddressRecordMap(MailAspect.getToMailAddressRecordMap());
        bo.setFrom_address(PropertiesUtil.getMailConfig("mail.fromAddress"));
        bo.setSubject(PropertiesUtil.getMailConfig("mail.subject"));
        bo.setText(PropertiesUtil.getMailConfig("mail.text"));
        return bo;
    }
}
