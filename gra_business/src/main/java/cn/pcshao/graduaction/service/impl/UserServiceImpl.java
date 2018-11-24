package cn.pcshao.graduaction.service.impl;

import cn.pcshao.graduaction.service.UserService;
import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseServiceImpl;
import cn.pcshao.grant.common.dao.GrantUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl extends BaseServiceImpl implements UserService {

    @Resource
    private GrantUserMapper grantUserMapper;

    @Override
    public BaseDao getDao() {
        return grantUserMapper;
    }

    public int doAuth(String username, String password){

        return 0;
    }
}
