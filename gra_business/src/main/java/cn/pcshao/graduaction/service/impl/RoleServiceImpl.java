package cn.pcshao.graduaction.service.impl;

import cn.pcshao.graduaction.service.RoleService;
import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseServiceImpl;
import cn.pcshao.grant.common.dao.GrantPermissionMapper;
import cn.pcshao.grant.common.dao.GrantRoleMapper;
import cn.pcshao.grant.common.dao.GrantRolePermissionMapper;
import cn.pcshao.grant.common.dao.GrantUserRoleMapper;
import cn.pcshao.grant.common.entity.*;
import cn.pcshao.grant.common.util.ListUtils;
import cn.pcshao.grant.common.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("roleServiceImpl")
public class RoleServiceImpl extends BaseServiceImpl<GrantRole, Short> implements RoleService {

    @Resource
    private GrantRoleMapper grantRoleMapper;

    @Resource
    private GrantPermissionMapper grantPermissionMapper;

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
            GrantRole grantRole = grantRoleMapper.selectByPrimaryKey(roleId);
            GrantUserRole grantUserRole = new GrantUserRole();
            grantUserRole.setUserId(l);
            grantUserRole.setRoleId(roleId);
            grantUserRole.setRoleName(grantRole.getRoleName());
            grantUserRoleMapper.insertSelective(grantUserRole);
        }
    }

    @Override
    public void bindRolePermissions(Short roleId, List<Long> permissionIdList) {
        for(Long l : permissionIdList){
            GrantPermission grantPermission = grantPermissionMapper.selectByPrimaryKey(l);
            GrantRolePermission grantRolePermission = new GrantRolePermission();
            grantRolePermission.setRoleId(roleId);
            grantRolePermission.setPermissionId(l);
            grantRolePermission.setPermissionName(grantPermission.getPermissionName());
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
    public List<GrantRole> listRoles(GrantRole grantRole) {
        GrantRoleExample example = new GrantRoleExample();
        GrantRoleExample.Criteria criteria = example.createCriteria();
        if(null != grantRole) {
            if (StringUtils.isNotEmpty(grantRole.getRoleName())) {
                criteria.andRoleNameLike(grantRole.getRoleName());
            }
            if (StringUtils.isNotEmpty(grantRole.getRoleRemark())) {
                criteria.andRoleNameLike(grantRole.getRoleRemark());
            }
        }
        return grantRoleMapper.selectByExample(example);
    }

    @Override
    public List<GrantRole> listRolesByUserId(Long userId){
        return grantRoleMapper.selectRolesByUserId(userId);
    }

    @Override
    public List<GrantRole> listRolesByPermissionId(Long permissionId) {
        return grantRoleMapper.selectRolesByPermissionId(permissionId);
    }

}
