package org.shiloh.web.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * JdbcTemplate 配置
 *
 * @author shiloh
 * @date 2023/3/19 14:45
 */
@Configuration
@PropertySource("classpath:jdbc.properties")
@RequiredArgsConstructor
public class JdbcTemplateConfig {

    private final Environment env;

    /**
     * 数据源配置
     *
     * @return {@link com.alibaba.druid.pool.DruidDataSource}
     * @author shiloh
     * @date 2023/3/19 14:48
     */
    @Bean
    public DataSource dataSource() {
        final DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(env.getProperty("jdbc.url"));
        druidDataSource.setDriverClassName(env.getProperty("jdbc.driver-class-name"));
        druidDataSource.setUsername(env.getProperty("jdbc.username"));
        druidDataSource.setPassword(env.getProperty("jdbc.password"));

        return druidDataSource;
    }

    /**
     * JdbcTemplate 配置
     *
     * @author shiloh
     * @date 2023/3/19 14:51
     */
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(this.dataSource());
    }
}
