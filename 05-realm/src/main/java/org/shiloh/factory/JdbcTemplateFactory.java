package org.shiloh.factory;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * JdbcTemplate 工厂类
 *
 * @author shiloh
 * @date 2023/2/28 18:39
 */
public class JdbcTemplateFactory {
    private static JdbcTemplate jdbcTemplate;

    public static JdbcTemplate getInstance() {
        if (jdbcTemplate == null) {
            jdbcTemplate = createJdbcTemplate();
        }

        return jdbcTemplate;
    }

    private static JdbcTemplate createJdbcTemplate() {
        return null;
    }
}
