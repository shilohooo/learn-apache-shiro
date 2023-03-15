package org.shiloh.web.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * Shiro Redis缓存管理器
 *
 * @author shiloh
 * @date 2023/3/15 19:02
 */
public class RedisCacheManager implements CacheManager {
    /* ======================== INSTANCE METHODS ========================== */

    /**
     * 根据 key 获取对应的缓存信息
     *
     * @param key key
     * @author shiloh
     * @date 2023/3/15 19:03
     */
    @Override
    public <K, V> Cache<K, V> getCache(String key) throws CacheException {
        return null;
    }

}
