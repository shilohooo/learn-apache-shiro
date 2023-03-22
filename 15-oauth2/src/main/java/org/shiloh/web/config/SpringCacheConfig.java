package org.shiloh.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author shiloh
 * @date 2023/3/20 23:32
 */
@Configuration
@Slf4j
public class SpringCacheConfig {
    /**
     * EhCache缓存 - 缓存管理器配置
     *
     * @author shiloh
     * @date 2023/3/20 23:32
     */
    @Bean
    public CacheManager springCacheManager() {
        final EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
        try {
            final InputStream inputStream = new ClassPathResource("ehcache/ehcache.xml").getInputStream();
            final net.sf.ehcache.CacheManager cacheManager = new net.sf.ehcache.CacheManager(inputStream);
            ehCacheCacheManager.setCacheManager(cacheManager);
        } catch (IOException e) {
            log.error("Spring Cache Manager 注入出错：", e);
        }
        return ehCacheCacheManager;
    }

}
