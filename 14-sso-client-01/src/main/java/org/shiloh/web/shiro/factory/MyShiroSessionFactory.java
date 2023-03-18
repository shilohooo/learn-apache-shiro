package org.shiloh.web.shiro.factory;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.web.session.mgt.WebSessionContext;
import org.shiloh.web.shiro.session.MyShiroSession;

/**
 * 重写 Shiro 创建会话的方式，加入用户名
 *
 * @author shiloh
 * @date 2023/3/17 17:23
 */
@Slf4j
public class MyShiroSessionFactory implements SessionFactory {
    /**
     * Creates a new {@code Session} instance based on the specified contextual initialization data.
     *
     * @param initData the initialization data to be used during {@link Session} creation.
     * @return a new {@code Session} instance.
     * @since 1.0
     */
    @Override
    public Session createSession(SessionContext initData) {
        if (initData == null) {
            return new MyShiroSession();
        }

        log.info("create session");
        final String host = initData.getHost();
        // 获取用户名
        final String username = ((WebSessionContext) initData).getServletRequest()
                .getParameter("username");
        return new MyShiroSession(host, username);
    }
}
