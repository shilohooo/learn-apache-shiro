package org.shiloh.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.AbstractValidatingSessionManager;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.shiloh.factory.JdbcTemplateFactory;
import org.shiloh.util.SerializableUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 自定义 Shiro 会话验证调度器
 * <p>
 * Shiro 提供的会话验证调度器实现都是直接调用 {@link AbstractValidatingSessionManager#validateSessions()} 方法进行验证，
 * <p>
 * 其直接调用 {@link SessionDAO#getActiveSessions()} 方法获取所有会话进行验证，
 * <p>
 * 如果会话比较多，会影响性能；这里用分页获取会话并进行验证
 *
 * @author shiloh
 * @date 2023/3/5 19:23
 */
@Slf4j
public class MySessionValidationScheduler implements SessionValidationScheduler, Runnable {
    /* ======================== CONSTANT ========================== */

    /**
     * SQL：分页查询会话信息
     */
    private static final String FIND_SESSION_BY_PAGE = "select session from learn_shiro.sessions limit ?, ?";

    /* ======================== INSTANCE FIELD ========================== */

    private final JdbcTemplate jdbcTemplate;

    /**
     * 可验证会话的会话管理器
     */
    private ValidatingSessionManager sessionManager;

    /**
     * 定时调度服务
     */
    private ScheduledExecutorService scheduledExecutorService;

    /**
     * 调度时间间隔，单位：毫秒
     */
    private long interval = 60 * 60 * 60;

    /**
     * 是否开启
     */
    private boolean enabled = false;

    /* ======================== CONSTRUCTOR ========================== */

    public MySessionValidationScheduler() {
        super();
        this.jdbcTemplate = JdbcTemplateFactory.getInstance();
    }

    /* ======================== ACCESSORS & MODIFIERS ========================== */

    public ValidatingSessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(ValidatingSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    /* ======================== INSTANCE METHODS ========================== */

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        if (log.isDebugEnabled()) {
            log.debug("开始会话验证....");
        }
        final long startTime = System.currentTimeMillis();
        // 获取会话信息
        int pageNo = 0;
        int pageSize = 10;
        List<String> sessions = this.jdbcTemplate.queryForList(FIND_SESSION_BY_PAGE, String.class, pageNo, pageSize);
        while (CollectionUtils.isNotEmpty(sessions)) {
            if (log.isDebugEnabled()) {
                log.info("查询到有 {} 条会话信息待验证", sessions.size());
            }
            for (final String sessionStr : sessions) {
                try {
                    final Session session = SerializableUtils.deserialize(sessionStr);
                    final Method method = ReflectionUtils.findMethod(
                            AbstractValidatingSessionManager.class, "validateSessions"
                    );
                    if (method != null) {
                        method.setAccessible(true);
                        ReflectionUtils.invokeMethod(
                                method, this.sessionManager, session, new DefaultSessionKey(session.getId())
                        );
                    }
                } catch (Exception ignored) {
                }
            }
            pageNo += pageSize;
            sessions = this.jdbcTemplate.queryForList(FIND_SESSION_BY_PAGE, String.class, pageNo, pageSize);
        }

        final long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("会话验证成功，耗时 {} 毫秒", (endTime - startTime));
        }
    }

    /**
     * Returns <code>true</code> if this Scheduler is enabled and ready to begin validation at the appropriate time,
     * <code>false</code> otherwise.
     * <p/>
     * It does <em>not</em> indicate if the validation is actually executing at that instant - only that it is prepared
     * to do so at the appropriate time.
     *
     * @return <code>true</code> if this Scheduler is enabled and ready to begin validation at the appropriate time,
     * <code>false</code> otherwise.
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Enables the session validation job.
     */
    @Override
    public void enableSessionValidation() {
        if (this.interval <= 0L) {
            return;
        }
        log.info("interval = {}", this.interval);
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
            final Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("MySessionValidationThread");
            return thread;
        });
        this.scheduledExecutorService.scheduleAtFixedRate(this, interval, interval, TimeUnit.SECONDS);
        this.enabled = true;
    }

    /**
     * Disables the session validation job.
     */
    @Override
    public void disableSessionValidation() {
        this.scheduledExecutorService.shutdown();
        this.enabled = false;
    }
}
