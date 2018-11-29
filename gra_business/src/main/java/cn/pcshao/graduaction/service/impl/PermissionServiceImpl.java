package cn.pcshao.graduaction.service.impl;

import cn.pcshao.graduaction.service.PermissionService;
import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseServiceImpl;
import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dao.GrantPermissionMapper;
import cn.pcshao.grant.common.dao.GrantRolePermissionMapper;
import cn.pcshao.grant.common.entity.GrantPermission;
import cn.pcshao.grant.common.entity.GrantPermissionExample;
import cn.pcshao.grant.common.entity.GrantRolePermission;
import cn.pcshao.grant.common.exception.CustomException;
import cn.pcshao.grant.common.util.ListUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("permissionServiceImpl")
public class PermissionServiceImpl extends BaseServiceImpl<GrantPermission, Long> implements PermissionService {

    @Resource
    private GrantPermissionMapper grantPermissionMapper;

    @Resource
    private GrantRolePermissionMapper grantRolePermissionMapper;

    @Override
    public void savePermission(GrantPermission grantPermission, List<Short> roleIdList) {
        try{
            grantPermissionMapper.insert(grantPermission);
        }catch (Exception e){
            throw new CustomException(DtoCodeConsts.DB_PRIMARY_EXIST, DtoCodeConsts.DB_PRIMARY_EXIST_MSG);
        }
        if(ListUtils.isEmptyList(roleIdList))
            return;
        //@TODO 查出新增记录的自增ID优化
        GrantPermissionExample grantPermissionExample = new GrantPermissionExample();
        grantPermissionExample.createCriteria().andPermissionNameEqualTo(grantPermission.getPermissionName());
        Long permissionId = grantPermissionMapper.selectByExample(grantPermissionExample).get(0).getPermissionId();
        this.bindPermissionRoles(permissionId, roleIdList);
    }

    @Override
    public void bindPermissionRoles(Long permissionId, List<Short> roleIdList) {
        for(Short s : roleIdList){
            GrantRolePermission grantRolePermission = new GrantRolePermission();
            grantRolePermission.setPermissionId(permissionId);
            grantRolePermission.setRoleId(s);
            grantRolePermissionMapper.insert(grantRolePermission);
        }
    }

    @Override
    public boolean findPermissionByName(String permissionName) {
        GrantPermissionExample grantPermissionExample = new GrantPermissionExample();
        grantPermissionExample.createCriteria().andPermissionNameEqualTo(permissionName);
        return grantPermissionMapper.countByExample(grantPermissionExample) > 0;
    }

    @Override
    public List<GrantPermission> listPermissions() {
        return null;
    }

    @Override
    public BaseDao<GrantPermission, Long> getDao() {
        return grantPermissionMapper;
    }
}
