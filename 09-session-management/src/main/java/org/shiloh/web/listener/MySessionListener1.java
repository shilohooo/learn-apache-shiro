package org.shiloh.web.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.io.Serializable;

/**
 * 会话监听器01
 * <p>
 * 会话监听器用于监听会话创建、过期、停止事件
 *
 * @author shiloh
 * @date 2023/3/5 18:11
 */
@Slf4j
public class MySessionListener1 implements SessionListener {
    /**
     * Notification callback that occurs when the corresponding Session has started.
     *
     * @param session the session that has started.
     */
    @Override
    public void onStart(Session session) {
        final Serializable sessionId = session.getId();
        log.info("会话创建，会话 ID = {}", sessionId);
    }

    /**
     * Notification callback that occurs when the corresponding Session has stopped, either programmatically via
     * {@link Session#stop} or automatically upon a subject logging out.
     *
     * @param session the session that has stopped.
     */
    @Override
    public void onStop(Session session) {
        final Serializable sessionId = session.getId();
        log.info("会话停止，会话 ID = {}", sessionId);
    }

    /**
     * Notification callback that occurs when the corresponding Session has expired.
     * <p/>
     * <b>Note</b>: this method is almost never called at the exact instant that the {@code Session} expires.  Almost all
     * session management systems, including Shiro's implementations, lazily validate sessions - either when they
     * are accessed or during a regular validation interval.  It would be too resource intensive to monitor every
     * single session instance to know the exact instant it expires.
     * <p/>
     * If you need to perform time-based logic when a session expires, it is best to write it based on the
     * session's {@link Session#getLastAccessTime() lastAccessTime} and <em>not</em> the time
     * when this method is called.
     *
     * @param session the session that has expired.
     */
    @Override
    public void onExpiration(Session session) {
        final Serializable sessionId = session.getId();
        log.info("会话过期，会话 ID = {}", sessionId);
    }
}
