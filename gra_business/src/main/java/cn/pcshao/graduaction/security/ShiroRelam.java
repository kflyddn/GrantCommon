package cn.pcshao.graduaction.security;

import cn.pcshao.graduaction.service.PermissionService;
import cn.pcshao.graduaction.service.RoleService;
import cn.pcshao.graduaction.service.UserService;
import cn.pcshao.grant.common.entity.GrantPermission;
import cn.pcshao.grant.common.entity.GrantRole;
import cn.pcshao.grant.common.entity.GrantUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Shiro用户注入配置
 * @author pcshao
 * @date 2018-11-28
 */
public class ShiroRelam extends AuthorizingRealm {

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    @Autowired
    @Qualifier("roleServiceImpl")
    RoleService roleService;

    @Autowired
    @Qualifier("permissionServiceImpl")
    PermissionService permissionService;

    /**
     * 权限认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<String> permissions = new ArrayList<>();
        List<String> roles = new ArrayList<>();
        //取出当前登录用户
        String currUsername = (String) principalCollection.getPrimaryPrincipal();
        GrantUser currUser = userService.listUsersByUserName(currUsername).get(0);
        //找出当前用户拥有的所有角色 找出所有角色对应的所有权限
        List<GrantRole> roleList = roleService.listRolesByUserId(currUser.getUserId());
        for(GrantRole g : roleList){
            roles.add(g.getRoleName());
            List<GrantPermission> permissionList = permissionService.listPermissionsByRoleId(g.getRoleId());
            for(GrantPermission gp : permissionList){
                permissions.add(gp.getPermissionName());
            }
        }
        //TODO 是否删除 roles.add(croUser.getCroRole().getRolename());
        info.addRoles(roles);//设置角色
        info.addStringPermissions(permissions);//设置权限
        return info;
    }

    /**
     * 用户认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        GrantUser grantUser = userService.listUsersByUserName(username).get(0);
        if(null == grantUser)
            throw new UnknownAccountException();
        if(!grantUser.getIsUse())
            throw new LockedAccountException();
        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo (
                grantUser.getUsername(), // 用户名
                grantUser.getPassword(), // 密码
                getName() // realm name
        );
        //TODO 设置盐度
        //simpleAuthorizationInfo.setCredentialsSalt(ByteSource.Util.bytes(grantUser.getUserId())); //设置盐
        return info;
    }
}
