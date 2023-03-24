package org.shiloh.web.dao;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.shiloh.web.util.SerializableUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义 SessionDAO 实现，将会话信息存储到数据库中
 * <p>
 * 因为继承了 {@link CachingSessionDAO}，所以在读取会话信息时会先检查缓存中是否存在，如果找不到才到数据库去查
 *
 * @author shiloh
 * @date 2023/3/5 18:45
 */
@RequiredArgsConstructor
public class MySessionDAO extends CachingSessionDAO {
    /* ======================== CONSTANT ========================== */

    /**
     * SQL：新增 Session 信息
     */
    private static final String INSERT = "insert into learn_shiro.sessions(id, session) values(?, ?)";

    /**
     * SQL：根据 ID 删除 Session 信息
     */
    private static final String DELETE_BY_ID = "delete from learn_shiro.sessions where id = ?";

    /**
     * SQL：更新 Session 信息
     */
    private static final String UPDATE = "update learn_shiro.sessions set session = ? where id = ?";

    /**
     * SQL：根据 ID 获取 Session 信息
     */
    private static final String FIND_BY_ID = "select session from learn_shiro.sessions where id = ?";


    /* ======================== INSTANCE FIELD ========================== */

    private final JdbcTemplate jdbcTemplate;

    /**
     * 创建 Session
     *
     * @param session 待持久化的 Session 对象
     * @return session id
     * @author shiloh
     * @date 2023/3/5 18:47
     */
    @Override
    protected Serializable doCreate(Session session) {
        final Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.jdbcTemplate.update(INSERT, sessionId, SerializableUtils.serialize(session));
        return session.getId();
    }

    /**
     * 根据 SessionId 获取 Session 信息
     *
     * @param sessionId sessionId
     * @return {@link Session}
     * @author shiloh
     * @date 2023/3/5 19:02
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        final List<String> sessionStr = this.jdbcTemplate.queryForList(FIND_BY_ID, String.class, sessionId);
        if (CollectionUtils.isEmpty(sessionStr)) {
            return null;
        }

        return SerializableUtils.deserialize(sessionStr.get(0));
    }

    /**
     * 更新 Session 信息
     *
     * @param session 待更新的 Session 信息
     * @author shiloh
     * @date 2023/3/5 19:03
     */
    @Override
    protected void doUpdate(Session session) {
        // 判断会话是否过期 / 停止，如果是则不更新
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            return;
        }

        this.jdbcTemplate.update(UPDATE, SerializableUtils.serialize(session), session.getId());
    }

    /**
     * 根据 SessionId 删除会话信息
     *
     * @param session 待删除的会话信息
     * @author shiloh
     * @date 2023/3/5 19:04
     */
    @Override
    protected void doDelete(Session session) {
        this.jdbcTemplate.update(DELETE_BY_ID, session.getId());
    }
}
