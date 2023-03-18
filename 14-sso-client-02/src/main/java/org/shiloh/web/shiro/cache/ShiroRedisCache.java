package org.shiloh.web.shiro.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

/**
 * 缓存实现，key 为 {@link String}，value 为任意对象
 *
 * @author shiloh
 * @date 2023/3/15 20:42
 */
@Slf4j
@Component
public class ShiroRedisCache<K, V> implements Cache<K, V> {

    @Autowired
    private RedisTemplate<K, V> redisTemplate;

    public static final String KEY_PREFIX = "shiro:cache:";

    @Override
    public V get(K key) throws CacheException {
        log.info("根据 key: {} 获取 shiro 缓存的数据", key);
        return this.redisTemplate.opsForValue().get(KEY_PREFIX + key);
    }

    @Override
    public V put(K key, V val) throws CacheException {
        if (val == null) {
            log.warn("无法将 null 值添加到缓存中");
            return null;
        }

        log.info("根据 key：{} 设置 shiro 缓存数据：{}", key, val);
        this.redisTemplate.opsForValue().set(key, val);
        return val;
    }

    @Override
    public V remove(K key) throws CacheException {
        final V prev = this.redisTemplate.opsForValue().get(key);
        this.redisTemplate.delete(key);
        return prev;
    }

    @Override
    public void clear() throws CacheException {
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
