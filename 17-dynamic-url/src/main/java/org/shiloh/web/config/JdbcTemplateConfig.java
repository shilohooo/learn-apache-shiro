package org.shiloh.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * JdbcTemplate 配置
 *
 * @author shiloh
 * @date 2023/3/31 22:36
 */
@Configuration
public class JdbcTemplateConfig {
    /**
     * 注入 JdbcTemplate
     *
     * @param dataSource 数据源
     * @author shiloh
     * @date 2023/3/31 22:37
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
