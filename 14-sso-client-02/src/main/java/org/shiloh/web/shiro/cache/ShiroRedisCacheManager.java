package org.shiloh.web.shiro.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Shiro 基于Redis的缓存管理器
 * <p>
 * 在 Shiro 中，{@link CacheManager} 用于缓存 {@link org.apache.shiro.subject.Subject} 的身份验证信息和权限信息
 *
 * @author shiloh
 * @date 2023/3/15 19:02
 */
@Slf4j
@Component
public class ShiroRedisCacheManager implements CacheManager {
    /* ======================== INSTANCE FIELDS ========================== */

    @Autowired
    @SuppressWarnings("rawtypes")
    private Cache shiroRedisCache;

    /* ======================== INSTANCE METHODS ========================== */

    /**
     * 根据 key 获取对应的缓存信息
     *
     * @param key key
     * @author shiloh
     * @date 2023/3/15 19:03
     */
    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> getCache(String key) throws CacheException {
        return this.shiroRedisCache;
    }
}
