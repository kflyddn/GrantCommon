package cn.pcshao.graduaction.service;

import cn.pcshao.grant.common.base.BaseService;
import cn.pcshao.grant.common.entity.GrantRole;

import java.util.List;

public interface RoleService extends BaseService<GrantRole, Short> {

    /**
     * 新增角色（可选带用户）
     * @param grantRole
     */
    void saveRole(GrantRole grantRole, List<Long> userIdList);

    /**
     * 给批量用户授权一个角色
     * @param roleId
     * @param userIdList
     */
    void bindRoleUsers(Short roleId, List<Long> userIdList);

    /**
     * 给批量权限授权一个角色
     * @param roleId
     * @param permissionIdList
     */
    void bindRolePermissions(Short roleId, List<Long> permissionIdList);

    /**
     * 检查角色名是否已存在
     * @param roleName
     * @return
     */
    boolean findRoleByName(String roleName);

    /**
     * 查询所有角色
     *  带条件、非必须
     * 分页、多条件查询待完善
     * @param grantRole
     * @return
     */
    List<GrantRole> listRoles(GrantRole grantRole);

    /**
     * 根据用户ID查对应角色
     * @param userId
     * @return
     */
    List<GrantRole> listRolesByUserId(Long userId);

    /**
     * 根据权限ID查对应角色
     * @param permissionId
     * @return
     */
    List<GrantRole> listRolesByPermissionId(Long permissionId);

}
