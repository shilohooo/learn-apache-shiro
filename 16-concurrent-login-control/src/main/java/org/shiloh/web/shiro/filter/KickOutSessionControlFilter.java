package org.shiloh.web.shiro.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * 并发登录控制过滤器
 *
 * @author shiloh
 * @date 2023/3/24 22:27
 */
@Setter
@Getter
@Slf4j
public class KickOutSessionControlFilter extends AccessControlFilter {
    public static final String KICK_OUT_SESSION_KEY = "kickOut";
    /**
     * 强制踢出后的跳转地址
     */
    private String kickOutUrl;

    /**
     * {@code true}：踢出后面登录的，{@code false}：踢出之前登录的
     */
    private Boolean isKickOutAfter = false;

    /**
     * 同一帐号最大会话数，默认为：1
     */
    private Integer maxSession = 1;

    /**
     * 会话管理器
     */
    private SessionManager sessionManager;

    /**
     * 已登录的会话队列缓存
     */
    private Cache<String, Deque<Serializable>> loggedSessionCache;

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
        System.out.println("KickOutSessionControlFilter.isAccessAllowed");
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

        System.out.println("KickOutSessionControlFilter.onAccessDenied");
        final Subject subject = this.getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            // 还没登录
            return true;
        }

        final Session session = subject.getSession();
        final String username = (String) subject.getPrincipal();
        final Serializable sessionId = session.getId();
        Deque<Serializable> deque = this.loggedSessionCache.get(username);
        log.info("get cache, key: {}, value: {}", username, deque);
        if (deque == null) {
            deque = new LinkedList<>();
            log.info("put cache, key: {}, value: {}", username, deque);
            this.loggedSessionCache.put(username, deque);
        }
        // 如果队列里没有该会话 ID，且用户没有被踢出，则将会话ID放入队列
        if (!deque.contains(sessionId) && session.getAttribute(KICK_OUT_SESSION_KEY) == null) {
            log.info("put session: {} to cache", sessionId);
            deque.push(sessionId);
        }
        // 如果队列里的会话 ID 数量超出了限制，开始踢人
        while (deque.size() > this.maxSession) {
            log.info("start kickOut");
            final Serializable kickOutSessionId;
            // 判断是踢出之前的还是后面的
            if (this.isKickOutAfter) {
                log.info("kickOut after");
                // 踢出后面登录的
                kickOutSessionId = deque.removeFirst();
            } else {
                log.info("kickOut before");
                // 踢出之前登录的
                kickOutSessionId = deque.removeLast();
            }
            try {
                final Session kickOutSession = this.sessionManager.getSession(new DefaultSessionKey(kickOutSessionId));
                if (kickOutSession != null) {
                    // 设置 kickOut 属性到会话中，表示踢出了
                    kickOutSession.setAttribute(KICK_OUT_SESSION_KEY, true);
                }
            } catch (Exception ignored) {
            }
        }
        // 如果被踢出了，则进行注销登录操作，然后重定向到指定的地址
        if (session.getAttribute(KICK_OUT_SESSION_KEY) != null) {
            log.info("kickOut session: {}", session.getId());
            try {
                subject.logout();
            } catch (Exception ignored) {
            }
            this.saveRequest(request);
            WebUtils.issueRedirect(request, response, this.kickOutUrl);
            return false;
        }

        return true;
    }
}
