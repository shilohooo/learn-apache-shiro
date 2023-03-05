package org.shiloh.web.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;

import java.io.Serializable;

/**
 * 会话监听器02
 * <p>
 * 会话监听器用于监听会话创建、过期、停止事件
 * <p>
 * 只想监听某一个事件，可以继承 {@link org.apache.shiro.session.SessionListenerAdapter}
 * <p>
 * 该适配器提供了 {@link org.apache.shiro.session.SessionListener} 的默认空实现
 *
 * @author shiloh
 * @date 2023/3/5 18:11
 */
@Slf4j
public class MySessionListener2 extends SessionListenerAdapter {
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
}
