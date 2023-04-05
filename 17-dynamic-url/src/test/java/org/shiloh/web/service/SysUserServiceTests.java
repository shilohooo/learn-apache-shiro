package org.shiloh.web.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.shiloh.test.WebAppTests;
import org.shiloh.web.entity.SysUser;
import org.shiloh.web.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class SysUserServiceTests extends WebAppTests {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 测试新增用户
     *
     * @author shiloh
     * @date 2023/4/1 22:58
     */
    @Test
    public void add() {
        SysUser sysUser = new SysUser();
        sysUser.setUsername("admin");
        sysUser.setPassword("admin");
        sysUser.setSalt(PasswordUtils.generateSalt());

        sysUser = this.sysUserService.add(sysUser);
        Assertions.assertThat(sysUser.getId()).isNotNull();
    }

    /**
     * 测试根据 ID 删除用户信息
     *
     * @author shiloh
     * @date 2023/4/1 22:59
     */
    @Test
    public void deleteById() {
        this.sysUserService.deleteById(3L);
        Assertions.assertThat(this.sysUserService.getById(3L)).isNull();
    }

    /**
     * 测试根据 ID 更新用户信息
     *
     * @author shiloh
     * @date 2023/4/1 22:59
     */
    @Test
    public void update() {
        final SysUser sysUser = this.sysUserService.getById(2L);
        Assertions.assertThat(sysUser).isNotNull();
        sysUser.setUsername("jack");
        this.sysUserService.update(sysUser);
    }

    /**
     * 测试查询所有用户信息
     *
     * @author shiloh
     * @date 2023/3/31 23:08
     */
    @Test
    public void getAll() {
        Assertions.assertThat(this.sysUserService.getAll()).isNotEmpty();
    }

    /**
     * 测试根据 ID 查询用户信息
     *
     * @author shiloh
     * @date 2023/3/31 23:08
     */
    @Test
    public void getById() {
        Assertions.assertThat(this.sysUserService.getById(1L)).isNotNull();
    }

    /**
     * 测试根据用户名查询用户信息
     *
     * @author shiloh
     * @date 2023/3/31 23:08
     */
    @Test
    public void getByUsername() {
        Assertions.assertThat(this.sysUserService.getByUsername("shiloh")).isNotNull();
    }

    /**
     * 测试修改用户密码
     *
     * @author shiloh
     * @date 2023/4/5 22:13
     */
    @Test
    public void changePassword() {
        final SysUser sysUser = this.sysUserService.getById(1L);
        Assertions.assertThat(sysUser).isNotNull();
        this.sysUserService.changePassword(sysUser.getId(), "654321");
    }

    /**
     * 测试根据用户 ID 获取该用户的所有角色
     *
     * @author shiloh
     * @date 2023/4/5 22:14
     */
    @Test
    public void getRoles() {
        Assertions.assertThat(this.sysUserService.getRoles(1L)).isNotEmpty();
    }

    /**
     * 测试根据用户 ID 获取该用户的所有权限
     *
     * @author shiloh
     * @date 2023/4/5 22:14
     */
    @Test
    public void getPermissions() {
        Assertions.assertThat(this.sysUserService.getPermissions(1L)).isNotEmpty();
    }
}