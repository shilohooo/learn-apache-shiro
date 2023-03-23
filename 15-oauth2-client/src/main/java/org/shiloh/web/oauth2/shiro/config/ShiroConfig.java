package org.shiloh.web.oauth2.shiro.config;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.shiloh.web.oauth2.shiro.filter.OAuth2AuthenticationFilter;
import org.shiloh.web.oauth2.shiro.realm.OAuth2Realm;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shiloh
 * @date 2023/3/23 23:10
 */
@Configuration
public class ShiroConfig {
    /**
     * clientId
     */
    private static final String CLIENT_ID = "44f01332-ba92-4e99-9abc-f1bf33ee2cce";

    /**
     * clientSecret
     */
    private static final String CLIENT_SECRET = "7134d6e3-d5cc-4dce-9bc5-68e95b5dad48";

    /**
     * accessToken 获取地址
     */
    private static final String ACCESS_TOKEN_URL = "http://localhost:8080/access-token";

    /**
     * 用户信息获取地址
     */
    private static final String USER_INFO_URL = "http://localhost:8080/user-info";

    /**
     * 客户端登录重定向地址
     */
    private static final String REDIRECT_URL = "http://localhost:9080/oauth2-login";

    /**
     * 登录地址
     */
    private static final String LOGIN_URL = String.format("http://localhost:8080/authorize?client_id=%s&response_type=code&redirect_uri=%s", CLIENT_ID, REDIRECT_URL);

    /**
     * 全局会话有效时间：30 分钟，单位：毫秒
     */
    public static final int GLOBAL_SESSION_TIMEOUT = 1000 * 60 * 30;

    /**
     * Ehcache 缓存管理器配置
     * <p>
     * 防止缓存名称冲突
     *
     * @return {@link net.sf.ehcache.CacheManager}
     * @author shiloh
     * @date 2023/3/8 22:06
     */
    @Bean
    public net.sf.ehcache.CacheManager ehCacheManager() {
        net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.getCacheManager("shiro-cache");
        if (cacheManager == null) {
            cacheManager = net.sf.ehcache.CacheManager.create(
                    ShiroConfig.class.getClassLoader()
                            .getResourceAsStream("ehcache/ehcache.xml")
            );
        }

        return cacheManager;
    }

    /**
     * Ehcache 缓存管理器配置
     *
     * @return {@link org.apache.shiro.cache.ehcache.EhCacheManager}
     * @author shiloh
     * @date 2023/3/8 15:49
     */
    @Bean
    public org.apache.shiro.cache.CacheManager cacheManager() {
        final EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(this.ehCacheManager());
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache/ehcache.xml");
        return ehCacheManager;
    }

    /**
     * OAuth2 Realm 配置
     *
     * @author shiloh
     * @date 2023/3/23 23:11
     */
    @Bean
    public OAuth2Realm oAuth2Realm() {
        final OAuth2Realm realm = new OAuth2Realm();
        // 开启缓存
        realm.setCachingEnabled(true);
        realm.setAuthenticationCachingEnabled(true);
        realm.setAuthenticationCacheName("authenticationCache");
        realm.setAuthorizationCachingEnabled(true);
        realm.setAuthorizationCacheName("authorizationCache");
        // clientId、clientSecret 配置
        realm.setClientId(CLIENT_ID);
        realm.setClientSecret(CLIENT_SECRET);
        // access token 获取地址配置
        realm.setAccessTokenUrl(ACCESS_TOKEN_URL);
        // 用户信息获取地址配置
        realm.setUserInfoUrl(USER_INFO_URL);
        // 登录重定向地址配置
        realm.setRedirectUrl(REDIRECT_URL);
        return realm;
    }

    /**
     * 会话ID生成器配置
     *
     * @author shiloh
     * @date 2023/3/23 23:19
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * 会话 cookie 模板配置
     *
     * @author shiloh
     * @date 2023/3/23 23:19
     */
    @Bean
    public Cookie sessionIdCookie() {
        final SimpleCookie simpleCookie = new SimpleCookie("sid");
        simpleCookie.setHttpOnly(true);
        // maxAge = 1，表示浏览器关闭时清除 cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    /**
     * 会话管理 DAO 配置
     *
     * @author shiloh
     * @date 2023/3/23 23:21
     */
    @Bean
    public SessionDAO sessionDAO() {
        final EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        // 开启会话缓存
        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        sessionDAO.setSessionIdGenerator(this.sessionIdGenerator());
        return sessionDAO;
    }

    /**
     * 会话管理器配置
     *
     * @author shiloh
     * @date 2023/3/23 23:21
     */
    @Bean
    public SessionManager sessionManager() {
        final DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 设置全局会话有效时间：30分钟
        sessionManager.setGlobalSessionTimeout(GLOBAL_SESSION_TIMEOUT);
        // 清理无效的会话
        sessionManager.setDeleteInvalidSessions(true);
        // 开启会话验证调度器
        sessionManager.setSessionValidationSchedulerEnabled(true);
        final QuartzSessionValidationScheduler scheduler = new QuartzSessionValidationScheduler();
        // 设置会话验证调度时间间隔 = 30分钟
        scheduler.setSessionValidationInterval(GLOBAL_SESSION_TIMEOUT);
        scheduler.setSessionManager(sessionManager);
        sessionManager.setSessionValidationScheduler(scheduler);
        // 设置 sessionDAO
        sessionManager.setSessionDAO(this.sessionDAO());
        // 开启 session id cookie
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(this.sessionIdCookie());
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * 核心组件 - 安全管理器配置
     *
     * @author shiloh
     * @date 2023/3/23 23:26
     */
    @Bean
    public SecurityManager securityManager() {
        final DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(this.sessionManager());
        securityManager.setCacheManager(this.cacheManager());
        securityManager.setRealm(this.oAuth2Realm());
        return securityManager;
    }

    /**
     * 将核心组件 - 安全管理器设置到 SecurityUtils 中
     *
     * @author shiloh
     * @date 2023/3/23 23:27
     */
    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        final MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(this.securityManager());
        return factoryBean;
    }

    /**
     * 自定义 OAuth2 身份验证过滤器配置
     *
     * @author shiloh
     * @date 2023/3/23 23:28
     */
    @Bean
    public AuthenticationFilter authenticationFilter() {
        final OAuth2AuthenticationFilter filter = new OAuth2AuthenticationFilter();
        filter.setAuthCodeParam("code");
        filter.setFailureUrl("/oauth2-failure.jsp");
        return filter;
    }

    /**
     * Shiro 过滤器配置
     *
     * @author shiloh
     * @date 2023/3/23 23:29
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        final ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(this.securityManager());
        factoryBean.setSuccessUrl("/");
        // 此处的 loginUrl 会自动设置到所有的 AccessControlFilter，如：OAuth2AuthenticationFilter
        factoryBean.setLoginUrl(LOGIN_URL);
        // 过滤器配置
        factoryBean.setFilters(Map.of("oauth2Authc", this.authenticationFilter()));
        // url 映射配置
        final Map<String, String> urlMappings = new LinkedHashMap<>();
        urlMappings.put("/", DefaultFilter.anon.name());
        urlMappings.put("/oauth2-failure.jsp", DefaultFilter.anon.name());
        urlMappings.put("/oauth2-login", "oauth2Authc");
        urlMappings.put("/logout", DefaultFilter.logout.name());
        urlMappings.put("/**", DefaultFilter.user.name());
        factoryBean.setFilterChainDefinitionMap(urlMappings);

        return factoryBean;
    }
}
