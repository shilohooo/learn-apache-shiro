package org.shiloh.test;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shiloh.WebApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;

/**
 * @author shiloh
 * @date 2023/3/31 22:38
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebApp.class})
@WebAppConfiguration
public class WebAppTests {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 应用上下文加载测试
     *
     * @author shiloh
     * @date 2023/3/31 22:38
     */
    @Test
    public void contextLoad() {
        Assertions.assertThat(this.dataSource).isNotNull();
        Assertions.assertThat(this.jdbcTemplate).isNotNull();
    }
}
