package cn.pcshao.graduaction.service.impl;

import cn.pcshao.graduaction.service.UserService;
import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseServiceImpl;
import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dao.GrantRoleMapper;
import cn.pcshao.grant.common.dao.GrantUserMapper;
import cn.pcshao.grant.common.dao.GrantUserRoleMapper;
import cn.pcshao.grant.common.entity.*;
import cn.pcshao.grant.common.exception.CustomException;
import cn.pcshao.grant.common.util.ListUtils;
import cn.pcshao.grant.common.util.PropertiesUtil;
import cn.pcshao.grant.common.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("userServiceImpl")
public class UserServiceImpl extends BaseServiceImpl<GrantUser, Long> implements UserService {

    @Resource
    private GrantUserMapper grantUserMapper;

    @Resource
    private GrantRoleMapper grantRoleMapper;

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
    public List<GrantUser> getUsersFromList(List<List> list) {
        List<GrantUser> users = new ArrayList<>();
        int colLength = list.get(0).size();
        int[] indexLocation = new int[colLength+1];
        try {
            for (int i = 0; i < list.size(); i++) {
                List row = list.get(i);
                GrantUser user = new GrantUser();
                for (int j = 0; j < row.size(); j++) {
                    if (indexLocation[0] != colLength) {
                        if (PropertiesUtil.getBusinessConfig("importUsersTemplate.username").equals(row.get(j).toString())) {
                            indexLocation[1] = j;
                            indexLocation[0]++;
                        } else if (PropertiesUtil.getBusinessConfig("importUsersTemplate.email").equals(row.get(j).toString())) {
                            indexLocation[2] = j;
                            indexLocation[0]++;
                        } else if (PropertiesUtil.getBusinessConfig("importUsersTemplate.nickname").equals(row.get(j).toString())) {
                            indexLocation[3] = j;
                            indexLocation[0]++;
                        } else if (PropertiesUtil.getBusinessConfig("importUsersTemplate.password").equals(row.get(j).toString())) {
                            indexLocation[4] = j;
                            indexLocation[0]++;
                        } else if (PropertiesUtil.getBusinessConfig("importUsersTemplate.sex").equals(row.get(j).toString())) {
                            indexLocation[5] = j;
                            indexLocation[0]++;
                        } else if (PropertiesUtil.getBusinessConfig("importUsersTemplate.tel").equals(row.get(j).toString())) {
                            indexLocation[6] = j;
                            indexLocation[0]++;
                        } else if (PropertiesUtil.getBusinessConfig("importUsersTemplate.is_use").equals(row.get(j).toString())) {
                            indexLocation[7] = j;
                            indexLocation[0]++;
                        }
                        //如果增加字段往后加，自动记录下标标记
                    }
                }
                user.setUsername(row.get(indexLocation[1]).toString());
                user.setEmail(row.get(indexLocation[2]).toString());
                user.setNickname(row.get(indexLocation[3]).toString());
                user.setPassword(row.get(indexLocation[4]).toString());
                user.setSex(row.get(indexLocation[5]).toString().equals("1") ? true : false);
                user.setTel(row.get(indexLocation[6]).toString());
                user.setIsUse(row.get(indexLocation[7]).toString().equals("1") ? true : false);
                users.add(user);
            }
        }catch (Exception e){
            throw new CustomException(DtoCodeConsts.EXCEL_FORMAT, DtoCodeConsts.EXCEL_FORMAT_MSG);
        }
        users.remove(0); //移除表头
        return users;
    }

    @Override
    public int insertBatch(List<GrantUser> users) {
        return grantUserMapper.insertBatch(users);
    }

    @Override
    public List<GrantUser> listUsersByUserName(String username) {
        GrantUserExample grantUserExample = new GrantUserExample();
        grantUserExample.createCriteria().andUsernameEqualTo(username);
        return grantUserMapper.selectByExample(grantUserExample);
    }

    @Override
    public List<GrantUser> listUserByRoleId(Short roleId) {
        return grantUserMapper.selectUsersByRoleId(roleId);
    }

    @Override
    public void resetDB(Long minUserId) {
        GrantUserExample example = new GrantUserExample();
        example.createCriteria().andUserIdGreaterThan(minUserId);
        grantUserMapper.deleteByExample(example);
    }

    @Override
    public Long saveUser(GrantUser grantUser, List<Short> roleIdList) {
        grantUser.setIsUse(true);
        try {
            grantUserMapper.insertSelective(grantUser);
        }catch (Exception e){
            throw new CustomException(DtoCodeConsts.DB_PRIMARY_EXIST, DtoCodeConsts.DB_PRIMARY_EXIST_MSG);
        }
        //默认角色2 normal
        if(ListUtils.isEmptyList(roleIdList)){
            roleIdList = new ArrayList<>();
            roleIdList.add((short)2);
        }
        //@TODO 查出新增记录的自增ID优化 之前是根据身份证号（username）再查一遍
        GrantUserExample grantUserExample = new GrantUserExample();
        grantUserExample.createCriteria().andUsernameEqualTo(grantUser.getUsername());
        Long userId = grantUserMapper.selectByExample(grantUserExample).get(0).getUserId();
        this.bindUserRoles(userId, roleIdList);
        return userId;
    }

    @Override
    public void bindUserRoles(Long userId, List<Short> roleIdList) {
        GrantUserRoleExample example = new GrantUserRoleExample();
        GrantUserRoleExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        grantUserRoleMapper.deleteByExample(example);
        for(Short id : roleIdList){
            GrantRole grantRole = grantRoleMapper.selectByPrimaryKey(id);
            GrantUserRole grantUserRole = new GrantUserRole();
            grantUserRole.setUserId(userId);
            grantUserRole.setRoleId(id);
            grantUserRole.setRoleName(grantRole.getRoleName());
            grantUserRoleMapper.insert(grantUserRole);
        }
    }

    @Override
    public List<GrantUser> listUsers(GrantUser grantUser, String roleName, String permissionName) {
        GrantUserExample example = new GrantUserExample();
        GrantUserExample.Criteria criteria = example.createCriteria();
        if(null != grantUser) {
            if (StringUtils.isNotEmpty(grantUser.getUsername())) {
                criteria.andUsernameLike(grantUser.getUsername());
            }
            if (StringUtils.isNotEmpty(grantUser.getNickname())) {
                criteria.andNicknameLike(grantUser.getNickname());
            }
            if (StringUtils.isNotEmpty(grantUser.getEmail())) {
                criteria.andEmailLike(grantUser.getEmail());
            }
            if (StringUtils.isNotEmpty(grantUser.getTel())) {
                criteria.andTelLike(grantUser.getTel());
            }
            if (null != grantUser.getSex()) {
                criteria.andSexEqualTo(grantUser.getSex());
            }
            if (null != grantUser.getIsUse()) {
                criteria.andIsUseEqualTo(grantUser.getIsUse());
            }
            if (null != grantUser.getUserId()) {
                criteria.andUserIdEqualTo(grantUser.getUserId());
            }
        }
        if (null != roleName) {
            //TODO 条件查询用户列表用
        }
        if (null != permissionName) {
            //TODO 条件查询用户列表用
        }
        return grantUserMapper.selectByExample(example);
    }

}
