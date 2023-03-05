package org.shiloh.constant;

/**
 * 常量
 *
 * @author shiloh
 * @date 2023/3/5 21:59
 */
public final class ShiroConstants {
    private ShiroConstants() {
    }

    /**
     * Request 属性：在线会话 key
     */
    public static final String ONLINE_SESSION_KEY = "online_session";

    /**
     * 仅清空本地缓存，不清空数据库的
     */
    public static final String ONLY_CLEAR_CACHE = "online_session_only_clear_cache";

}
