package org.shiloh.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis相关配置
 *
 * @author shiloh
 * @date 2023/3/15 18:45
 */
@Configuration
public class RedisConfig {
    /**
     * 连接工厂配置
     *
     * @return {@link org.springframework.data.redis.connection.jedis.JedisConnectionFactory}
     * @author shiloh
     * @date 2023/3/15 18:45
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        final RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(
                "127.0.0.1", 16379
        );
        return new JedisConnectionFactory(configuration);
    }

    /**
     * RedisTemplate 配置
     *
     * @return {@link RedisTemplate}
     * @author shiloh
     * @date 2023/3/15 18:47
     */
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(this.jedisConnectionFactory());
        return redisTemplate;
    }
}
