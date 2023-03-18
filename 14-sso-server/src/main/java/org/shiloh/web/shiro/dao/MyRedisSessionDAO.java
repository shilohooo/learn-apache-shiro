package org.shiloh.web.shiro.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.shiloh.web.shiro.session.MyShiroSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 重写 SessionDAO，使用 Redis 缓存 Session 实现会话共享
 * <p>
 * {@link org.apache.shiro.session.mgt.eis.SessionDAO} 用于管理 Shiro 中的会话信息，
 * 定义了对 {@link Session} 的 CRUD 操作
 *
 * @author shiloh
 * @date 2023/3/15 18:44
 */
@Component
@Slf4j
public class MyRedisSessionDAO extends CachingSessionDAO {
    /* ======================== CONSTANTS ========================== */

    /**
     * SessionId 缓存 key 前缀
     */
    public static final String SESSION_ID_KEY_PREFIX = "SHIRO:SESSION_ID:";

    /**
     * 用户名与 SessionId 关联缓存 key 前缀
     */
    public static final String USERNAME_SESSION_ID_KEY_PREFIX = "SHIRO:USERNAME:SESSION_ID:";

    /**
     * 默认会话超时时间：30 分钟，单位: ms
     */
    public static final int DEFAULT_SESSION_TIMEOUT = 1000 * 60 * 30;

    /* ======================== INSTANCE FIELDS ========================== */

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /* ======================== INSTANCE METHODS ========================== */

    /**
     * Subclass hook to actually persist the given <tt>Session</tt> instance to the underlying EIS.
     *
     * @param session the Session instance to persist to the EIS.
     * @return the id of the session created in the EIS (i.e. this is almost always a primary key and should be the
     * value returned from {@link Session#getId() Session.getId()}).
     */
    @Override
    protected Serializable doCreate(Session session) {
        final MyShiroSession myShiroSession = (MyShiroSession) session;
        final Serializable sessionId = this.generateSessionId(session);
        log.info("创建会话信息：{}，id = {}", myShiroSession, sessionId);
        this.assignSessionId(session, sessionId);
        this.redisTemplate.opsForValue()
                .set(SESSION_ID_KEY_PREFIX + sessionId, session, DEFAULT_SESSION_TIMEOUT, TimeUnit.MILLISECONDS);
        // 如果当前用户不是匿名登录，则将用户名与 SessionId 关联起来保存到缓存中
        if (StringUtils.isNotBlank(myShiroSession.getUsername())) {
            this.redisTemplate.opsForValue()
                    .set(
                            // key = prefix + 用户名
                            USERNAME_SESSION_ID_KEY_PREFIX + myShiroSession.getUsername(),
                            // value = session id
                            session.getId(),
                            DEFAULT_SESSION_TIMEOUT,
                            TimeUnit.MILLISECONDS
                    );
        }
        return session.getId();
    }

    /**
     * Subclass implementation hook to permanently delete the given Session from the underlying EIS.
     *
     * @param session the session instance to permanently delete from the EIS.
     */
    @Override
    protected void doDelete(Session session) {
        if (session.getId() == null) {
            return;
        }

        log.info("根据 id: {} 删除会话信息：{}", session.getId(), session);
        this.redisTemplate.delete(SESSION_ID_KEY_PREFIX + session.getId());
        final MyShiroSession myShiroSession = (MyShiroSession) session;
        // 如果用户不是匿名登录，则删除用户名与 session id 的关联缓存
        if (StringUtils.isNotBlank(myShiroSession.getUsername())) {
            this.redisTemplate.delete(USERNAME_SESSION_ID_KEY_PREFIX + myShiroSession.getUsername());
        }
    }

    /**
     * Subclass implementation hook to actually persist the {@code Session}'s state to the underlying EIS.
     *
     * @param session the session object whose state will be propagated to the EIS.
     */
    @Override
    protected void doUpdate(Session session) {
        final Serializable sessionId = session.getId();
        if (sessionId == null) {
            return;
        }
        log.info("更新 id为：{}的会话信息：{}", sessionId, session);
        // 重置过期时间
        session.setTimeout(DEFAULT_SESSION_TIMEOUT);
        this.redisTemplate.opsForValue()
                .set(SESSION_ID_KEY_PREFIX + sessionId, session, DEFAULT_SESSION_TIMEOUT, TimeUnit.MILLISECONDS);
        final MyShiroSession myShiroSession = (MyShiroSession) session;
        myShiroSession.setUsername((String) SecurityUtils.getSubject().getPrincipal());
        // 如果用户不是匿名登录，则更新用户名与 session id 的关联缓存
        if (StringUtils.isNotBlank(myShiroSession.getUsername())) {
            this.redisTemplate.opsForValue()
                    .set(
                            // key = prefix + 用户名
                            USERNAME_SESSION_ID_KEY_PREFIX + myShiroSession.getUsername(),
                            // value = session id
                            session.getId(),
                            DEFAULT_SESSION_TIMEOUT,
                            TimeUnit.MILLISECONDS
                    );
        }
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
        if (sessionId == null) {
            return null;
        }
        log.info("根据会话 ID：{} 获取会话信息", sessionId);
        return (Session) this.redisTemplate.opsForValue()
                .get(SESSION_ID_KEY_PREFIX + sessionId);
    }

    /**
     * 根据用户名获取会话信息
     *
     * @param username 用户名
     * @return 会话信息实例
     * @author shiloh
     * @date 2023/3/17 17:53
     */
    public Session getSessionByUsername(String username) {
        final String sessionId = this.getSessionIdByUsername(username);
        return this.doReadSession(sessionId);
    }

    /**
     * 根据用户名获取 session id
     *
     * @param username 用户名
     * @return session id
     * @author shiloh
     * @date 2023/3/17 17:53
     */
    public String getSessionIdByUsername(String username) {
        return (String) this.redisTemplate.opsForValue()
                .get(USERNAME_SESSION_ID_KEY_PREFIX + username);
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
        final Set<String> keys = this.redisTemplate.keys(SESSION_ID_KEY_PREFIX + "*");
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
