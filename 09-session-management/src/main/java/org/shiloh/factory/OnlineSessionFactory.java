package org.shiloh.factory;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.web.session.mgt.WebSessionContext;
import org.shiloh.session.OnlineSession;
import org.shiloh.util.IpUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义 Session Factory
 *
 * @author shiloh
 * @date 2023/3/5 21:56
 */
public class OnlineSessionFactory implements SessionFactory {

    /**
     * Http 请求头 User-Agent Key
     */
    private static final String USER_AGENT = "User-Agent";

    /**
     * Creates a new {@code Session} instance based on the specified contextual initialization data.
     *
     * @param initData the initialization data to be used during {@link Session} creation.
     * @return a new {@code Session} instance.
     * @since 1.0
     */
    @Override
    public Session createSession(SessionContext initData) {
        final OnlineSession onlineSession = new OnlineSession();
        if (initData instanceof WebSessionContext) {
            final WebSessionContext webSessionContext = (WebSessionContext) initData;
            final HttpServletRequest request = (HttpServletRequest) webSessionContext.getServletRequest();
            // 设置请求相关信息
            if (request != null) {
                onlineSession.setHost(IpUtils.getIpAddress(request));
                onlineSession.setUserAgent(request.getHeader(USER_AGENT));
                onlineSession.setSysHost(request.getLocalAddr() + ":" + request.getLocalPort());
            }
        }
        return onlineSession;
    }
}
