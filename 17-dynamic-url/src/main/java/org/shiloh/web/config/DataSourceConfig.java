package org.shiloh.web.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * 数据源配置
 *
 * @author shiloh
 * @date 2023/3/31 22:33
 */
@Configuration
@PropertySource({"classpath:jdbc.properties"})
@RequiredArgsConstructor
public class DataSourceConfig {
    private final Environment env;

    /**
     * 注入数据源
     *
     * @author shiloh
     * @date 2023/3/31 22:35
     */
    @Bean
    public DataSource dataSource() {
        final DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setDriverClassName(env.getProperty("jdbc.driver-class-name"));
        dataSource.setUsername(env.getProperty("jdbc.username"));
        dataSource.setPassword(env.getProperty("jdbc.password"));

        return dataSource;
    }
}
