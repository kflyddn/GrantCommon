package cn.pcshao.graduaction.security;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Shiro配置类
 * @author pcshao
 * @date 2018-11-28
 */
@Configuration
public class ShiroConfig {

    /**
     * 为自定义Relam添加适当规则
     * @return
     */
    @Bean
    public ShiroRelam shiroRelam(){
        ShiroRelam shiroRelam = new ShiroRelam();
//        shiroRelam.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRelam;
    }

    /**
     * 为自定义WebSecurityManager添加Relam
     * @return
     */
    @Bean
    public WebSecurityManager securityManager(){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //将自定义的Relam注入容器
        defaultWebSecurityManager.setRealm(shiroRelam());
        //之后可以添加多个Realm
        return defaultWebSecurityManager;
    }

    /**
     * 给ShiroFilter注入自定义的WebSecurityManager
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(WebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //将自定义的WebSecurityManager注入ShiroFilter过滤器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //页面也可以在这里配置
        shiroFilterFactoryBean.setLoginUrl("login");
        shiroFilterFactoryBean.setSuccessUrl("initPage");
        shiroFilterFactoryBean.setUnauthorizedUrl("403");

        return shiroFilterFactoryBean;
    }

    /**
     * 凭证匹配器
     *  多次Hash
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

}
