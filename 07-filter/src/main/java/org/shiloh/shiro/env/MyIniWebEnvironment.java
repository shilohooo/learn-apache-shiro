package org.shiloh.shiro.env;

import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

import javax.servlet.Filter;

/**
 * 自定义 IniWebEnvironment
 *
 * @author shiloh
 * @date 2023/3/4 16:23
 */
public class MyIniWebEnvironment extends IniWebEnvironment {
    /**
     * 自定义过滤器链解析
     *
     * @author shiloh
     * @date 2023/3/4 16:24
     */
    @Override
    protected FilterChainResolver createFilterChainResolver() {
        final PathMatchingFilterChainResolver filterChainResolver = new PathMatchingFilterChainResolver();
        final DefaultFilterChainManager filterChainManager = new DefaultFilterChainManager();
        // 注册默认的过滤器
        for (final DefaultFilter defaultFilter : DefaultFilter.values()) {
            final Filter filter = (Filter) ClassUtils.newInstance(defaultFilter.getFilterClass());
            filterChainManager.addFilter(defaultFilter.name(), filter);
        }
        // 注册 url - filter 映射关系
        filterChainManager.addToChain("/login.jsp", DefaultFilter.authc.name());
        filterChainManager.addToChain("/unauthorized.jsp", DefaultFilter.anon.name());
        filterChainManager.addToChain("/**", DefaultFilter.authc.name());
        filterChainManager.addToChain("/**", DefaultFilter.roles.name(), "admin");
        // 设置过滤器的属性
        final FormAuthenticationFilter formAuthenticationFilter = (FormAuthenticationFilter) filterChainManager
                .getFilter(DefaultFilter.authc.name());
        // 设置登录地址
        formAuthenticationFilter.setLoginUrl("/login.jsp");
        final RolesAuthorizationFilter rolesAuthorizationFilter = (RolesAuthorizationFilter) filterChainManager
                .getFilter(DefaultFilter.roles.name());
        // 设置未经授权跳转的地址
        rolesAuthorizationFilter.setUnauthorizedUrl("/unauthorized.jsp");
        filterChainResolver.setFilterChainManager(filterChainManager);
        return filterChainResolver;
    }
}
