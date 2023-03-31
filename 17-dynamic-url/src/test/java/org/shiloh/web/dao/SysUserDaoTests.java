package org.shiloh.web.dao;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.shiloh.test.WebAppTests;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 系统用户 DAO 单元测试
 *
 * @author shiloh
 * @date 2023/3/31 23:19
 */
public class SysUserDaoTests extends WebAppTests {
    @Autowired
    private SysUserDao sysUserDao;

    /**
     * 测试查询所有用户信息
     *
     * @author shiloh
     * @date 2023/3/31 23:20
     */
    @Test
    public void findAll() {
        Assertions.assertThat(this.sysUserDao.findAll()).isNotEmpty();
    }

    /**
     * 测试根据 ID 查询用户信息
     *
     * @author shiloh
     * @date 2023/3/31 23:20
     */
    @Test
    public void findById() {
        Assertions.assertThat(this.sysUserDao.findById(1L)).isNotNull();
    }

    /**
     * 测试根据用户名查询用户信息
     *
     * @author shiloh
     * @date 2023/3/31 23:20
     */
    @Test
    public void findByUsername() {
        Assertions.assertThat(this.sysUserDao.findByUsername("shiloh")).isNotNull();
    }
}