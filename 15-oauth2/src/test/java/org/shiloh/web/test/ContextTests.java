package org.shiloh.web.test;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shiloh.web.WebApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

/**
 * @author shiloh
 * @date 2023/3/19 14:53
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebApp.class)
public class ContextTests {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test() {
        Assertions.assertThat(this.dataSource)
                .isNotNull();
        Assertions.assertThat(this.jdbcTemplate)
                .isNotNull();
    }
}
