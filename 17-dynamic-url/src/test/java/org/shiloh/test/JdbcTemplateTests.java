package org.shiloh.test;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * JdbcTemplate 单元测试
 *
 * @author shiloh
 * @date 2023/3/31 22:55
 */
public class JdbcTemplateTests extends WebAppTests {
    /**
     * 测试查询所有用户信息
     *
     * @author shiloh
     * @date 2023/3/31 22:56
     */
    @Test
    public void testFindAllUsers() {
        final String sql = "select * from learn_shiro.sys_user";
        Assertions.assertThat(this.jdbcTemplate.queryForList(sql)).isNotEmpty();
    }
}
