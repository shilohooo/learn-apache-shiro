package org.shiloh.web.filter;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.shiloh.constant.ShiroConstants;
import org.shiloh.session.OnlineSession;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * {@link OnlineSession} 过滤器
 *
 * @author shiloh
 * @date 2023/3/5 22:15
 */
@Setter
@Getter
public class OnlineSessionFilter extends AccessControlFilter {
    /* ========================= INSTANCE FIELDS ========================== */

    /**
     * 强制退出登录后的跳转地址
     */
    private String forceLogoutUrl;

    /**
     * SessionDAO
     */
    private SessionDAO sessionDAO;

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
        final Subject subject = this.getSubject(request, response);
        if (subject == null || subject.getSession(false) == null) {
            // 还没登录
            return true;
        }

        final Session session = this.sessionDAO.readSession(subject.getSession().getId());
        if (session instanceof OnlineSession) {
            final OnlineSession onlineSession = (OnlineSession) session;
            request.setAttribute(ShiroConstants.ONLINE_SESSION_KEY, onlineSession);
            return !OnlineSession.OnlineStatus.FORCE_LOGOUT.equals(onlineSession.getStatus());
        }

        return true;
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
        final Subject subject = this.getSubject(request, response);
        if (subject != null) {
            // 退出登录
            subject.logout();
        }

        this.saveRequestAndRedirectToLogin(request, response);
        return true;
    }

    /**
     * Convenience method for subclasses that merely acquires the {@link #getLoginUrl() getLoginUrl} and redirects
     * the request to that url.
     * <p/>
     * <b>N.B.</b>  If you want to issue a redirect with the intention of allowing the user to then return to their
     * originally requested URL, don't use this method directly.  Instead you should call
     * {@link #saveRequestAndRedirectToLogin(ServletRequest, ServletResponse)
     * saveRequestAndRedirectToLogin(request,response)}, which will save the current request state so that it can
     * be reconstructed and re-used after a successful login.
     *
     * @param request  the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     * @throws IOException if an error occurs.
     */
    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        // 强制退出后，跳转到指定地址
        WebUtils.issueRedirect(request, response, this.getForceLogoutUrl());
    }
}
