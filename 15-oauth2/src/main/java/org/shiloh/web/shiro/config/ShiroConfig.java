package org.shiloh.web.shiro.config;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.shiloh.web.service.PasswordHelper;
import org.shiloh.web.service.UserService;
import org.shiloh.web.shiro.credentials.MyHashCredentialsMatcher;
import org.shiloh.web.shiro.realm.MyUserRealm;
import org.shiloh.web.spring.SpringCacheManagerWrapper;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 配置
 *
 * @author shiloh
 * @date 2023/3/21 23:03
 */
@Configuration
public class ShiroConfig {
    /**
     * 缓存管理器配置
     *
     * @param springCacheManager Spring 缓存管理器
     * @author shiloh
     * @date 2023/3/21 23:41
     */
    @Bean
    public SpringCacheManagerWrapper cacheManager(CacheManager springCacheManager) {
        final SpringCacheManagerWrapper springCacheManagerWrapper = new SpringCacheManagerWrapper();
        springCacheManagerWrapper.setCacheManager(springCacheManager);
        return springCacheManagerWrapper;
    }

    /**
     * 密码匹配器配置
     *
     * @author shiloh
     * @date 2023/3/21 23:41
     */
    @Bean
    public CredentialsMatcher credentialsMatcher(UserService userService, PasswordHelper passwordHelper) {
        final MyHashCredentialsMatcher myHashCredentialsMatcher = new MyHashCredentialsMatcher(
                userService, passwordHelper
        );
        myHashCredentialsMatcher.setHashAlgorithmName("md5");
        myHashCredentialsMatcher.setHashIterations(2);
        myHashCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return myHashCredentialsMatcher;
    }

    /**
     * Realm 配置
     *
     * @author shiloh
     * @date 2023/3/21 23:47
     */
    @Bean
    public MyUserRealm myUserRealm(UserService userService, PasswordHelper passwordHelper) {
        final MyUserRealm myUserRealm = new MyUserRealm(userService);
        myUserRealm.setCredentialsMatcher(this.credentialsMatcher(userService, passwordHelper));
        myUserRealm.setCachingEnabled(false);
        return myUserRealm;
    }

    /**
     * 会话 ID 生成器配置
     *
     * @author shiloh
     * @date 2023/3/22 21:28
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * 会话 ID cookie 配置
     *
     * @author shiloh
     * @date 2023/3/22 21:29
     */
    @Bean
    public Cookie sessionIdCookie() {
        final SimpleCookie simpleCookie = new SimpleCookie("sid");
        simpleCookie.setHttpOnly(true);
        // -1 表示浏览器关闭时删除 cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    /**
     * 会话 DAO 配置
     *
     * @author shiloh
     * @date 2023/3/22 21:30
     */
    @Bean
    public SessionDAO sessionDAO() {
        final EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setSessionIdGenerator(this.sessionIdGenerator());
        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        return sessionDAO;
    }

    /**
     * 会话管理器配置
     *
     * @author shiloh
     * @date 2023/3/22 21:30
     */
    @Bean
    public SessionManager sessionManager() {
        final DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 全局会话过期时间：30分钟
        sessionManager.setGlobalSessionTimeout(1000 * 60 * 30);
        // 删除无效的会话
        sessionManager.setDeleteInvalidSessions(true);
        // 定时验证会话的间隔：30秒
        sessionManager.setSessionValidationInterval(1000 * 30);
        // 开启会话验证调度器
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionDAO(this.sessionDAO());
        // 开启 session id cookie
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(this.sessionIdCookie());
        // 禁止将 session id 追加到 url 后面
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * 安全管理器配置 - 核心组件
     *
     * @author shiloh
     * @date 2023/3/22 21:34
     */
    @Bean
    public SecurityManager securityManager(UserService userService, PasswordHelper passwordHelper, CacheManager springCacheManager) {
        final DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(this.myUserRealm(userService, passwordHelper));
        securityManager.setSessionManager(this.sessionManager());
        securityManager.setCacheManager(this.cacheManager(springCacheManager));
        return securityManager;
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(UserService userService, PasswordHelper passwordHelperm,
                                                               CacheManager springCacheManager) {
        final MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        // 相当于调用 SecurityUtils.setSecurityManager(securityManager)
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(this.securityManager(userService, passwordHelperm, springCacheManager));
        return factoryBean;
    }

    /**
     * 身份验证过滤器配置
     *
     * @author shiloh
     * @date 2023/3/22 21:39
     */
    @Bean
    public AuthenticationFilter authenticationFilter() {
        final FormAuthenticationFilter filter = new FormAuthenticationFilter();
        filter.setLoginUrl("/login");
        return filter;
    }

    /**
     * Shiro过滤器配置
     *
     * @author shiloh
     * @date 2023/3/22 21:39
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(UserService userService, PasswordHelper passwordHelper,
                                                         CacheManager springCacheManager) {
        final ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(this.securityManager(userService, passwordHelper, springCacheManager));
        factoryBean.setLoginUrl("/login");
        factoryBean.setSuccessUrl("/");
        // 添加过滤器
        factoryBean.setFilters(Map.of(DefaultFilter.authc.name(), this.authenticationFilter()));
        // 添加过滤器路径映射
        final Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>(16);
        filterChainDefinitionMap.put("/", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/login", DefaultFilter.authc.name());
        filterChainDefinitionMap.put("/logout", DefaultFilter.logout.name());

        filterChainDefinitionMap.put("/authorize", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/access-token", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/user-info", DefaultFilter.anon.name());

        filterChainDefinitionMap.put("/**", DefaultFilter.user.name());
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return factoryBean;
    }

    /**
     * 生命周期处理器
     *
     * @author shiloh
     * @date 2023/3/22 21:43
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * Shiro 权限校验注解支持
     *
     * @author shiloh
     * @date 2023/3/22 21:44
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(UserService userService,
                                                                                   PasswordHelper passwordHelper,
                                                                                   CacheManager springCacheManager) {
        final AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(this.securityManager(userService, passwordHelper, springCacheManager));
        return advisor;
    }
}
