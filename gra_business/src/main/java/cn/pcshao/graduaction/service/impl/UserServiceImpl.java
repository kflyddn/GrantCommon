package cn.pcshao.graduaction.service.impl;

import cn.pcshao.graduaction.service.UserService;
import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseServiceImpl;
import cn.pcshao.grant.common.dao.GrantUserMapper;
import cn.pcshao.grant.common.entity.GrantUserExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userServiceImpl")
public class UserServiceImpl extends BaseServiceImpl implements UserService {

    @Resource
    private GrantUserMapper grantUserMapper;

    @Override
    public BaseDao getDao() {
        return grantUserMapper;
    }

    /**
     * 验证登录
     * @param username
     * @param password
     * @return
     */
    public int doAuth(String username, String password){
        GrantUserExample grantUserExample = new GrantUserExample();
        grantUserExample.createCriteria().andUsernameEqualTo(username).andPasswordEqualTo(password);
        return (grantUserMapper.selectByExample(grantUserExample).size()>0) ? 1 : 0;
    }
}
