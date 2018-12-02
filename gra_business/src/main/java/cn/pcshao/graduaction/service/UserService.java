package cn.pcshao.graduaction.service;

import cn.pcshao.grant.common.base.BaseService;
import cn.pcshao.grant.common.entity.GrantUser;

import java.util.List;

public interface UserService extends BaseService<GrantUser, Long> {

    /**
     * 基础校验方法
     * @param username
     * @param password
     * @return
     */
    GrantUser doAuth(String username, String password);

    /**
     * 条件查询用户
     * @TODO
     *  分页、多条件查询等等待完善
     * @return
     */
    List<GrantUser> listUsers(GrantUser grantUser, String withRole);

    /**
     * 条件查询用户
     * @param username
     * @return
     */
    List<GrantUser> listUsersByUserName(String username);

    /**
     * 条件查询用户
     * @param roleId
     * @return
     */
    List<GrantUser> listUserByRoleId(Short roleId);

    /**
     * 新增用户带权限
     * @param grantUser
     * @param roleIdList
     */
    void saveUser(GrantUser grantUser, List<Short> roleIdList);

    /**
     * 给单个用户授权多个角色
     * @param userId
     * @param roleIdList
     */
    void bindUserRoles(Long userId, List<Short> roleIdList);

}
