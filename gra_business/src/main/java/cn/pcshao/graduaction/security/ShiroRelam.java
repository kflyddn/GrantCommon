package cn.pcshao.graduaction.security;

import cn.pcshao.graduaction.service.UserService;
import cn.pcshao.grant.common.entity.GrantUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Shiro用户注入配置
 * @author pcshao
 * @date 2018-11-28
 */
public class ShiroRelam extends AuthorizingRealm {

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    /**
     * 权限
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 口令认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());
        GrantUser grantUser = userService.doAuth(username, password);
        if(null == grantUser)
            throw new AuthenticationException();
        if(!grantUser.getIsUse())
            throw new LockedAccountException();
        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        return new SimpleAuthenticationInfo(
                grantUser.getUsername(), // 用户名
                grantUser.getPassword(), // 密码
                getName() // realm name
        );
    }
}
