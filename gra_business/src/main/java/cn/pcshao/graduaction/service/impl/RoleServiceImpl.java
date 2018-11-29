package cn.pcshao.graduaction.service.impl;

import cn.pcshao.graduaction.service.RoleService;
import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseServiceImpl;
import cn.pcshao.grant.common.dao.GrantRoleMapper;
import cn.pcshao.grant.common.dao.GrantRolePermissionMapper;
import cn.pcshao.grant.common.dao.GrantUserRoleMapper;
import cn.pcshao.grant.common.entity.*;
import cn.pcshao.grant.common.util.ListUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("roleServiceImpl")
public class RoleServiceImpl extends BaseServiceImpl<GrantRole, Short> implements RoleService {

    @Resource
    private GrantRoleMapper grantRoleMapper;

    @Resource
    private GrantUserRoleMapper grantUserRoleMapper;

    @Resource
    private GrantRolePermissionMapper grantRolePermissionMapper;

    @Override
    public BaseDao<GrantRole, Short> getDao() {
        return grantRoleMapper;
    }

    @Override
    public void saveRole(GrantRole grantRole, List<Long> userIdList) {
        grantRoleMapper.insertSelective(grantRole);
        if(ListUtils.isEmptyList(userIdList))
            return;
        //@TODO 查出新增记录的自增ID优化
        GrantRoleExample grantRoleExample = new GrantRoleExample();
        grantRoleExample.createCriteria().andRoleNameEqualTo(grantRole.getRoleName());
        Short roleId = grantRoleMapper.selectByExample(grantRoleExample).get(0).getRoleId();
        this.bindRoleUsers(roleId, userIdList);
    }

    @Override
    public void bindRoleUsers(Short roleId, List<Long> userIdList) {
        for(Long l : userIdList){
            GrantUserRole grantUserRole = new GrantUserRole();
            grantUserRole.setUserId(l);
            grantUserRole.setRoleId(roleId);
            grantUserRoleMapper.insertSelective(grantUserRole);
        }
    }

    @Override
    public void bindRolePermissions(Short roleId, List<Long> permissionIdList) {
        for(Long l : permissionIdList){
            GrantRolePermission grantRolePermission = new GrantRolePermission();
            grantRolePermission.setRoleId(roleId);
            grantRolePermission.setPermissionId(l);
            grantRolePermissionMapper.insertSelective(grantRolePermission);
        }
    }

    @Override
    public boolean findRoleByName(String roleName) {
        GrantRoleExample grantRoleExample = new GrantRoleExample();
        grantRoleExample.createCriteria().andRoleNameEqualTo(roleName);
        return grantRoleMapper.countByExample(grantRoleExample) > 0;
    }

    @Override
    public List<GrantRole> listRoles() {
        //@TODO
        return null;
    }
}
