package org.shiloh.web.shiro.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * 重写 SessionDAO，使用 Redis 缓存 Session 实现会话共享
 *
 * @author shiloh
 * @date 2023/3/15 18:44
 */
@Component
@Slf4j
public class MyRedisSessionDAO extends CachingSessionDAO {
    /* ======================== CONSTANTS ========================== */

    /**
     * SessionId 缓存名称前缀
     */
    public static final String KEY_PREFIX = "shiro:session:";

    /* ======================== INSTANCE FIELD ========================== */

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /* ======================== INSTANCE METHODS ========================== */

    /**
     * Subclass implementation hook to actually persist the {@code Session}'s state to the underlying EIS.
     *
     * @param session the session object whose state will be propagated to the EIS.
     */
    @Override
    protected void doUpdate(Session session) {
        log.info("更新会话信息：{}", session);
        final Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.redisTemplate.opsForValue()
                .set(KEY_PREFIX + sessionId, session);
    }

    /**
     * Subclass implementation hook to permanently delete the given Session from the underlying EIS.
     *
     * @param session the session instance to permanently delete from the EIS.
     */
    @Override
    protected void doDelete(Session session) {
        if (session == null) {
            return;
        }

        log.info("删除会话信息：{}", session);
        this.redisTemplate.delete(KEY_PREFIX + session.getId());
    }

    /**
     * Subclass hook to actually persist the given <tt>Session</tt> instance to the underlying EIS.
     *
     * @param session the Session instance to persist to the EIS.
     * @return the id of the session created in the EIS (i.e. this is almost always a primary key and should be the
     * value returned from {@link Session#getId() Session.getId()}.
     */
    @Override
    protected Serializable doCreate(Session session) {
        if (session == null) {
            throw new RuntimeException("无法创建空的会话信息");
        }

        log.info("创建会话信息：{}", session);
        final Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.redisTemplate.opsForValue()
                .set(KEY_PREFIX + sessionId, session);
        return session.getId();
    }

    /**
     * Subclass implementation hook that retrieves the Session object from the underlying EIS or {@code null} if a
     * session with that ID could not be found.
     *
     * @param sessionId the id of the <tt>Session</tt> to retrieve.
     * @return the Session in the EIS identified by <tt>sessionId</tt> or {@code null} if a
     * session with that ID could not be found.
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        log.info("根据会话 ID：{} 获取会话信息", sessionId);
        return (Session) this.redisTemplate.opsForValue()
                .get(KEY_PREFIX + sessionId);
    }

    /**
     * 获取当前在线的会话列表
     *
     * @return 在线会话列表
     * @author shiloh
     * @date 2023/3/15 20:05
     */
    @Override
    public Collection<Session> getActiveSessions() {
        final Set<String> keys = this.redisTemplate.keys(KEY_PREFIX + "*");
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }

        final List<Session> sessions = new ArrayList<>();
        for (final String key : keys) {
            final Session session = (Session) this.redisTemplate.opsForValue()
                    .get(key);
            if (session == null) {
                continue;
            }

            sessions.add(session);
        }

        return sessions;
    }


}
