package cn.pcshao.graduaction.service;

import cn.pcshao.grant.common.base.BaseService;
import cn.pcshao.grant.common.entity.GrantPermission;

import java.util.List;

public interface PermissionService extends BaseService<GrantPermission, Long> {

    /**
     * 新增权限（可选带角色）
     * @param grantPermission
     * @param roleIdList
     */
    void savePermission(GrantPermission grantPermission, List<Short> roleIdList);

    /**
     * 给批量角色授权一个权限
     * @param permissionId
     * @param roleIdList
     */
    void bindPermissionRoles(Long permissionId, List<Short> roleIdList);

    /**
     * 检查角色名是否已存在
     * @param permissionName
     * @return
     */
    boolean findPermissionByName(String permissionName);

    /**
     * 查询所有权限
     *  带条件、非必须
     * @TODO
     * 分页、多条件查询待完善
     * @return
     */
    List<GrantPermission> listPermissions(Short roleId);
}
