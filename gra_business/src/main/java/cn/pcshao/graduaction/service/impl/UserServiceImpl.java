package cn.pcshao.graduaction.service.impl;

import cn.pcshao.graduaction.service.UserService;
import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseServiceImpl;
import cn.pcshao.grant.common.dao.GrantUserMapper;
import cn.pcshao.grant.common.dao.GrantUserRoleMapper;
import cn.pcshao.grant.common.entity.GrantUser;
import cn.pcshao.grant.common.entity.GrantUserExample;
import cn.pcshao.grant.common.entity.GrantUserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userServiceImpl")
public class UserServiceImpl extends BaseServiceImpl<GrantUser, Long> implements UserService {

    @Resource
    private GrantUserMapper grantUserMapper;

    @Resource
    private GrantUserRoleMapper grantUserRoleMapper;

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
    public GrantUser doAuth(String username, String password){
        GrantUserExample grantUserExample = new GrantUserExample();
        grantUserExample.createCriteria().andUsernameEqualTo(username).andPasswordEqualTo(password);
        return grantUserMapper.selectByExample(grantUserExample).get(0);
    }

    @Override
    public boolean findByUserName(String username) {
        GrantUserExample grantUserExample = new GrantUserExample();
        grantUserExample.createCriteria().andUsernameEqualTo(username);
        return grantUserMapper.countByExample(grantUserExample) > 0;
    }

    @Override
    public void saveUser(GrantUser grantUser, List<Short> roleIdList) {
        grantUser.setIsUse(true);
        grantUserMapper.insert(grantUser);
        GrantUserExample grantUserExample = new GrantUserExample();
        grantUserExample.createCriteria().andUsernameEqualTo(grantUser.getUsername());
        //插完grantUser表后查出新增用户的ID后插入grantUserRole表
        Long userId = grantUserMapper.selectByExample(grantUserExample).get(0).getUserId();
        for(Short id : roleIdList){
            GrantUserRole grantUserRole = new GrantUserRole();
            grantUserRole.setUserId(userId);
            grantUserRole.setRoleId(id);
            grantUserRoleMapper.insert(grantUserRole);
        }
    }
}
