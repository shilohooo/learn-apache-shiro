package org.shiloh.factory;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * JdbcTemplate 工厂类
 *
 * @author shiloh
 * @date 2023/2/28 18:39
 */
public class JdbcTemplateFactory {
    /**
     * 全局 {@link JdbcTemplate} 实例
     */
    private static JdbcTemplate jdbcTemplate;

    /**
     * 获取 {@link JdbcTemplate} 实例
     *
     * @return {@link JdbcTemplate} 实例
     * @author shiloh
     * @date 2023/2/28 22:16
     */
    public static JdbcTemplate getInstance() {
        if (jdbcTemplate == null) {
            jdbcTemplate = createJdbcTemplate();
        }

        return jdbcTemplate;
    }

    /**
     * 创建 {@link JdbcTemplate} 实例，设置数据源
     *
     * @return {@link JdbcTemplate} 实例
     * @author shiloh
     * @date 2023/2/28 22:16
     */
    private static JdbcTemplate createJdbcTemplate() {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        try (
                final InputStream inputStream = JdbcTemplateFactory.class
                        .getClassLoader()
                        .getResourceAsStream("jdbc.properties")
        ) {
            final Properties properties = new Properties();
            properties.load(inputStream);

            final DruidDataSource dataSource = new DruidDataSource();
            dataSource.setDriverClassName(properties.getProperty("jdbc.driver-class-name"));
            dataSource.setUrl("jdbc.url");
            dataSource.setUsername("jdbc.username");
            dataSource.setPassword("jdbc.password");

            jdbcTemplate.setDataSource(dataSource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jdbcTemplate;
    }
}
