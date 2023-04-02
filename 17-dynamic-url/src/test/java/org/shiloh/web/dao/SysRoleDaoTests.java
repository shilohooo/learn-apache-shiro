package org.shiloh.web.dao;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.shiloh.test.WebAppTests;
import org.shiloh.web.entity.SysRole;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 系统角色管理 DAO 单元测试
 *
 * @author shiloh
 * @date 2023/4/2 22:57
 */
public class SysRoleDaoTests extends WebAppTests {
    @Autowired
    private SysRoleDao sysRoleDao;

    /**
     * 测试新增角色
     *
     * @author shiloh
     * @date 2023/4/2 22:57
     */
    @Test
    public void add() {
        SysRole sysRole = new SysRole();
        sysRole.setName("测试角色");
        sysRole.setParentId(3L);

        sysRole = this.sysRoleDao.add(sysRole);
        Assertions.assertThat(sysRole.getId()).isNotNull();
        Assertions.assertThat(this.sysRoleDao.findById(sysRole.getId())).isNotNull();
    }

    /**
     * 测试根据 ID 删除角色信息
     *
     * @author shiloh
     * @date 2023/4/2 22:57
     */
    @Test
    public void deleteById() {
        this.sysRoleDao.deleteById(5L);
        Assertions.assertThat(this.sysRoleDao.findById(5L)).isNull();
    }

    /**
     * 测试修改角色信息
     *
     * @author shiloh
     * @date 2023/4/2 22:57
     */
    @Test
    public void update() {
        final SysRole sysRole = this.sysRoleDao.findById(3L);
        sysRole.setName("管理员");
        this.sysRoleDao.update(sysRole);
    }

    /**
     * 测试根据 ID 查询角色信息
     *
     * @author shiloh
     * @date 2023/4/2 22:57
     */
    @Test
    public void findById() {
        Assertions.assertThat(this.sysRoleDao.findById(3L)).isNotNull();
    }

    /**
     * 测试查询所有角色信息
     *
     * @author shiloh
     * @date 2023/4/2 22:57
     */
    @Test
    public void findAll() {
        Assertions.assertThat(this.sysRoleDao.findAll()).isNotEmpty();
    }
}