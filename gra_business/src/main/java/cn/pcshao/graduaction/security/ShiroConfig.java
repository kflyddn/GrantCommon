package cn.pcshao.graduaction.security;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置类
 * @author pcshao
 * @date 2018-11-28
 */
@Configuration
public class ShiroConfig {

    private Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    /**
     * 为自定义Relam添加适当规则
     * @return
     */
    @Bean
    public ShiroRelam shiroRelam(){
        ShiroRelam shiroRelam = new ShiroRelam();
        //TODO 现在是控制层MD5加解密,密文存在token中，用credentialsMatcher加密效果一样但是是明文
        //shiroRelam.setCredentialsMatcher(hashedCredentialsMatcher());
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
        //转发到URL
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/initPage");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        // filterChainDefinitions拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        // 配置不会被拦截的链接 从上向下顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/templates/**", "anon");

        // 配置退出过滤器,具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");
        //add操作，该用户必须有【addOperation】权限
//        filterChainDefinitionMap.put("/add", "perms[addOperation]");

        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问【放行】-->
        filterChainDefinitionMap.put("/user/register", "anon");
        filterChainDefinitionMap.put("/user/checkUserName", "anon");
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/user/**", "authc");

        // 相册模块添加过滤规则
        filterChainDefinitionMap.put("/album/**", "authc");

        shiroFilterFactoryBean
                .setFilterChainDefinitionMap(filterChainDefinitionMap);
        logger.debug("Shiro拦截器工厂类注入成功");
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
        hashedCredentialsMatcher.setHashIterations(1);//散列的次数，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

}
