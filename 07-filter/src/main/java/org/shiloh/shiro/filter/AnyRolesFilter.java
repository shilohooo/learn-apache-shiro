package org.shiloh.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static org.shiloh.shiro.filter.FormLoginFilter.LOGIN_URL;

/**
 * 拥有任意一个符合的角色即可访问
 *
 * @author shiloh
 * @date 2023/3/4 23:34
 */
@Slf4j
public class AnyRolesFilter extends AccessControlFilter {
    /**
     * 未经授权跳转的地址
     */
    private static final String UNAUTHORIZED_URL = "/unauthorized.jsp";

    /**
     * Returns <code>true</code> if the request is allowed to proceed through the filter normally, or <code>false</code>
     * if the request should be handled by the
     * {@link #onAccessDenied(ServletRequest, ServletResponse, Object) onAccessDenied(request,response,mappedValue)}
     * method instead.
     *
     * @param request     the incoming <code>ServletRequest</code>
     * @param response    the outgoing <code>ServletResponse</code>
     * @param mappedValue the filter-specific config value mapped to this filter in the URL rules mappings.
     * @return <code>true</code> if the request should proceed through the filter normally, <code>false</code> if the
     * request should be processed by this filter's
     * {@link #onAccessDenied(ServletRequest, ServletResponse, Object)} method instead.
     * @throws Exception if an error occurs during processing.
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        log.info("AnyRolesFilter.isAccessAllowed");
        final String[] roles = (String[]) mappedValue;
        if (roles == null) {
            // 请求的路径没有角色权限限制
            return true;
        }

        for (final String role : roles) {
            if (this.getSubject(request, response).hasRole(role)) {
                return true;
            }
        }
        // 没有指定的角色，跳转到 onAccessDenied 方法处理
        return false;
    }

    /**
     * Processes requests where the subject was denied access as determined by the
     * {@link #isAccessAllowed(ServletRequest, ServletResponse, Object) isAccessAllowed}
     * method.
     *
     * @param request  the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     * @return <code>true</code> if the request should continue to be processed; false if the subclass will
     * handle/render the response directly.
     * @throws Exception if there is an error processing the request.
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        log.info("AnyRolesFilter.onAccessDenied");
        final Subject subject = this.getSubject(request, response);
        if (!subject.isAuthenticated()) {
            // 还未登录，重定向到登录页面
            this.saveRequest(request);
            WebUtils.issueRedirect(request, response, LOGIN_URL);
            return false;
        }
        // 跳转到未经授权的页面
        WebUtils.issueRedirect(request, response, UNAUTHORIZED_URL);
        return false;
    }
}
