package org.shiloh.web.dao;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.shiloh.test.WebAppTests;
import org.shiloh.web.entity.SysUser;
import org.shiloh.web.utils.PasswordUtils;
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
     * 测试新增用户
     *
     * @author shiloh
     * @date 2023/4/1 23:08
     */
    @Test
    public void add() {
        SysUser sysUser = new SysUser();
        sysUser.setUsername("tom");
        final String salt = PasswordUtils.generateSalt();
        sysUser.setPassword(PasswordUtils.encrypt("123456", salt));
        sysUser.setSalt(salt);

        sysUser = this.sysUserDao.add(sysUser);
        Assertions.assertThat(sysUser.getId()).isNotNull();
    }

    /**
     * 测试根据 ID 修改用户信息
     *
     * @author shiloh
     * @date 2023/4/1 23:09
     */
    @Test
    public void update() {
        SysUser sysUser = this.sysUserDao.findById(3L);
        sysUser.setLocked(true);
        this.sysUserDao.update(sysUser);

        sysUser = this.sysUserDao.findById(3L);
        Assertions.assertThat(sysUser.getLocked()).isTrue();
    }

    /**
     * 测试根据 ID 删除用户信息
     *
     * @author shiloh
     * @date 2023/4/1 23:09
     */
    @Test
    public void deleteById() {
        this.sysUserDao.deleteById(5L);
        Assertions.assertThat(this.sysUserDao.findById(5L)).isNull();
    }

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