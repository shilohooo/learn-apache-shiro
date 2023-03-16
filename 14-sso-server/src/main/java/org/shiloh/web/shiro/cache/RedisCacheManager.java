package org.shiloh.web.shiro.cache;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Shiro 基于Redis的缓存管理器
 * <p>
 * 在 Shiro 中，{@link CacheManager} 用于缓存 {@link org.apache.shiro.subject.Subject} 的身份验证信息和权限信息
 *
 * @author shiloh
 * @date 2023/3/15 19:02
 */
@Component
public class RedisCacheManager implements CacheManager {
    /* ======================== INSTANCE FIELDS ========================== */

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
        final RedisCache redisCache = (RedisCache) this.redisTemplate.opsForValue()
                .get(key);
        return (Cache<K, V>) redisCache;
    }

    /**
     * 缓存实现，key 为 {@link String}，value 为任意对象
     *
     * @author shiloh
     * @date 2023/3/15 20:42
     */
    public class RedisCache implements Cache<String, Object> {
        /**
         * key 前缀
         */
        static final String KEY_PREFIX = "shiro:subject:cache:";

        @Override
        public Object get(String key) throws CacheException {
            return redisTemplate.opsForValue().get(KEY_PREFIX + key);
        }

        @Override
        public Object put(String key, Object val) throws CacheException {
            redisTemplate.opsForValue().set(KEY_PREFIX + key, val);
            return val;
        }

        @Override
        public Object remove(String key) throws CacheException {
            final String fullKey = KEY_PREFIX + key;
            final Object prev = redisTemplate.opsForValue().get(fullKey);
            redisTemplate.delete(fullKey);
            return prev;
        }

        @Override
        public void clear() throws CacheException {
            final Set<String> keys = this.keys();
            if (CollectionUtils.isEmpty(keys)) {
                return;
            }

            keys.forEach(key -> redisTemplate.delete(key));
        }

        @Override
        public int size() {
            final Set<String> keys = this.keys();
            if (CollectionUtils.isEmpty(keys)) {
                return 0;
            }

            return keys.size();
        }

        @Override
        public Set<String> keys() {
            final Set<String> keys = redisTemplate.keys(KEY_PREFIX + "*");
            if (CollectionUtils.isEmpty(keys)) {
                return Collections.emptySet();
            }

            return keys;
        }

        @Override
        public Collection<Object> values() {
            final Set<String> keys = this.keys();
            if (CollectionUtils.isEmpty(keys)) {
                return Collections.emptySet();
            }

            final Set<Object> values = new HashSet<>();
            keys.forEach(key -> values.add(redisTemplate.opsForValue().get(key)));

            return values;
        }
    }

}
