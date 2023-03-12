package org.shiloh.web.shiro.config;

import com.alibaba.druid.pool.DruidDataSource;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.shiloh.web.shiro.credentials.RetryLimitHashCredentialsMatcher;
import org.shiloh.web.shiro.filter.MyFormAuthenticationFilter;
import org.shiloh.web.shiro.realm.MyShiroRealm;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Shiro 配置
 *
 * @author shiloh
 * @date 2023/3/8 15:48
 */
@Configuration
public class ShiroConfig {

    /**
     * 全局会话超时时间：30分钟
     */
    public static final int GLOBAL_SESSION_TIMEOUT_MS = 1000 * 60 * 30;

    /**
     * 会话验证调度时间间隔：30秒
     */
    public static final int SESSION_VALIDATION_INTERVAL_MS = 1000 * 30;

    /**
     * Cookie 有效期，单位：秒
     */
    public static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 30;

    /**
     * 数据源配置
     *
     * @return {@link DruidDataSource}
     * @author shiloh
     * @date 2023/3/8 1au4
     */
    @Bean
    public DataSource dataSource() {
        final DruidDataSource dataSource = new DruidDataSource();
        try (
                final InputStream inputStream = ShiroConfig.class
                        .getClassLoader()
                        .getResourceAsStream("jdbc.properties")
        ) {
            final Properties properties = new Properties();
            properties.load(inputStream);
            dataSource.setDriverClassName(properties.getProperty("jdbc.driver-class-name"));
            dataSource.setUrl(properties.getProperty("jdbc.url"));
            dataSource.setUsername(properties.getProperty("jdbc.username"));
            dataSource.setPassword(properties.getProperty("jdbc.password"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataSource;
    }

    /**
     * {@link JdbcTemplate} 配置
     *
     * @return {@link JdbcTemplate}
     * @author shiloh
     * @date 2023/3/8 16:08
     */
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(this.dataSource());
    }

    /**
     * Ehcache 缓存管理器配置
     * <p>
     * 防止缓存名称冲突
     *
     * @return {@link CacheManager}
     * @author shiloh
     * @date 2023/3/8 22:06
     */
    @Bean
    public CacheManager ehCacheManager() {
        CacheManager cacheManager = CacheManager.getCacheManager("shiro-cache");
        if (cacheManager == null) {
            cacheManager = CacheManager.create(
                    ShiroConfig.class.getClassLoader()
                            .getResourceAsStream("shiro-ehcache.xml")
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
        ehCacheManager.setCacheManagerConfigFile("classpath:shiro-ehcache.xml");
        return ehCacheManager;
    }

    /**
     * 密码验证匹配器配置
     *
     * @return {@link org.shiloh.web.shiro.credentials.RetryLimitHashCredentialsMatcher}
     * @author shiloh
     * @date 2023/3/8 15:59
     */
    @Bean
    public CredentialsMatcher credentialsMatcher() {
        final RetryLimitHashCredentialsMatcher credentialsMatcher = new RetryLimitHashCredentialsMatcher(
                this.cacheManager()
        );
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        credentialsMatcher.setHashIterations(2);
        return credentialsMatcher;
    }

    /**
     * 自定义 Shiro Realm 配置
     *
     * @return {@link MyShiroRealm}
     * @author shiloh
     * @date 2023/3/8 16:29
     */
    @Bean
    public MyShiroRealm myShiroRealm() {
        final MyShiroRealm myShiroRealm = new MyShiroRealm();
        // 设置密码校验匹配器，支持重试次数限制
        myShiroRealm.setCredentialsMatcher(this.credentialsMatcher());
        // 开启缓存
        myShiroRealm.setCachingEnabled(true);
        // 开启身份验证缓存
        myShiroRealm.setAuthenticationCachingEnabled(true);
        // 设置身份验证缓存的缓存名称
        myShiroRealm.setAuthenticationCacheName("authenticationCache");
        // 开始授权缓存
        myShiroRealm.setAuthorizationCachingEnabled(true);
        // 设置授权缓存的缓存名称
        myShiroRealm.setAuthorizationCacheName("authorizationCache");
        return myShiroRealm;
    }

    /**
     * 配置会话 ID 生成器，使用 {@link java.util.UUID} 生成会话 ID
     *
     * @return {@link JavaUuidSessionIdGenerator}
     * @author shiloh
     * @date 2023/3/8 16:37
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * {@link SessionDAO} 配置
     *
     * @return {@link org.shiloh.web.dao.MySessionDAO}
     * @author shiloh
     * @date 2023/3/8 16:39
     */
    @Bean
    public SessionDAO sessionDAO() {
        // final MySessionDAO mySessionDAO = new MySessionDAO(this.jdbcTemplate());

        // web集成
        final EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        // 注入 sessionId 生成组件
        sessionDAO.setSessionIdGenerator(this.sessionIdGenerator());
        // 设置缓存名称 - 对应 ehcache 配置文件中的 name 属性
        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        return sessionDAO;
    }

    /**
     * SessionId Cookie 配置
     *
     * @return {@link SimpleCookie}
     * @author shiloh
     * @date 2023/3/12 10:52
     */
    @Bean
    public Cookie sessionIdCookie() {
        // 实例化时指定 cookie 的名称
        final SimpleCookie simpleCookie = new SimpleCookie("myShiroSessionCookie");
        // 开启 httpOnly
        simpleCookie.setHttpOnly(true);
        // 设置有效期，单位：秒，这里设置为一天
        simpleCookie.setMaxAge(COOKIE_MAX_AGE);

        return simpleCookie;
    }

    /**
     * 会话管理器配置
     *
     * @author shiloh
     * @date 2023/3/8 16:43
     */
    @Bean
    public ValidatingSessionManager sessionManager() {
        // final DefaultSessionManager sessionManager = new DefaultSessionManager();

        // web 集成
        final DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 设置全局会话超时时间，默认为：半个小时
        sessionManager.setGlobalSessionTimeout(GLOBAL_SESSION_TIMEOUT_MS);
        sessionManager.setSessionDAO(this.sessionDAO());
        // 删除已失效的会话
        sessionManager.setDeleteInvalidSessions(true);
        // 开启会话验证调度，使用 Shiro 提供的 Quartz 调度器
        sessionManager.setSessionValidationSchedulerEnabled(true);
        final QuartzSessionValidationScheduler scheduler = new QuartzSessionValidationScheduler();
        // 设置会话验证调度时间间隔
        scheduler.setSessionValidationInterval(SESSION_VALIDATION_INTERVAL_MS);
        scheduler.setSessionManager(sessionManager);
        sessionManager.setSessionValidationScheduler(scheduler);
        // 开启 sessionIdCookie
        sessionManager.setSessionIdCookieEnabled(true);
        // 注入 sessionIdCookie 组件
        sessionManager.setSessionIdCookie(this.sessionIdCookie());

        return sessionManager;
    }

    /**
     * Shiro 核心组件安全管理器配置
     *
     * @author shiloh
     * @date 2023/3/8 16:49
     */
    @Bean
    public SecurityManager securityManager() {
        // final DefaultSecurityManager securityManager = new DefaultSecurityManager();

        // web 集成
        final DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealms(Collections.singletonList(this.myShiroRealm()));
        securityManager.setCacheManager(this.cacheManager());
        securityManager.setSessionManager(this.sessionManager());
        return securityManager;
    }

    /**
     * 将安全管理器设置到 {@link org.apache.shiro.SecurityUtils} 中
     *
     * @return {@link MethodInvokingFactoryBean}
     * @author shiloh
     * @date 2023/3/8 16:53
     */
    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        final MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(this.securityManager());
        return factoryBean;
    }

    /**
     * Shiro 生命周期处理器配置
     *
     * @return {@link LifecycleBeanPostProcessor}
     * @author shiloh
     * @date 2023/3/8 21:50
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 配置基于 form 表单的身份验证过滤器
     *
     * @return {@link org.apache.shiro.web.filter.authc.FormAuthenticationFilter}
     * @author shiloh
     * @date 2023/3/12 11:02
     */
    @Bean
    public AuthenticationFilter formAuthenticationFilter() {
        final FormAuthenticationFilter filter = new MyFormAuthenticationFilter();
        // 设置接收表单用户名、密码参数的名称
        filter.setUsernameParam("username");
        filter.setPasswordParam("password");
        // 设置登录页面跳转地址
        filter.setLoginUrl("/login.jsp");

        return filter;
    }

    /**
     * Shiro Web 过滤器配置
     *
     * @author shiloh
     * @date 2023/3/12 11:05
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        final ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 注入安全管理器
        shiroFilterFactoryBean.setSecurityManager(this.securityManager());
        // 设置登录页面跳转地址
        shiroFilterFactoryBean.setLoginUrl("/login.jsp");
        // 设置未授权访问跳转地址
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized.jsp");
        // 设置登录成功的跳转地址
        shiroFilterFactoryBean.setSuccessUrl("/index.jsp");
        // 添加过滤器
        final Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put(DefaultFilter.authc.name(), this.formAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        // 添加 url 拦截与过滤器的映射关系
        // 注意：这里需要使用有序的 map，保证执行顺序
        final Map<String, String> urlMappings = new LinkedHashMap<>();
        urlMappings.put("/index.jsp", DefaultFilter.anon.name());
        urlMappings.put("/unauthorized.jsp", DefaultFilter.anon.name());
        urlMappings.put("/login.jsp", DefaultFilter.authc.name());
        urlMappings.put("/logout", DefaultFilter.logout.name());
        urlMappings.put("/**", DefaultFilter.user.name());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(urlMappings);
        return shiroFilterFactoryBean;
    }
}
