package org.shiloh.web.dao;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.shiloh.test.WebAppTests;
import org.shiloh.web.entity.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 系统菜单管理 DAO 单元测试
 *
 * @author shiloh
 * @date 2023/4/2 23:25
 */
public class SysMenuDaoTests extends WebAppTests {
    @Autowired
    private SysMenuDao sysMenuDao;

    /**
     * 测试新增菜单
     *
     * @author shiloh
     * @date 2023/4/2 23:25
     */
    @Test
    public void add() {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setName("测试菜单");
        sysMenu.setType(1);
        sysMenu.setOrderNo(1);
        sysMenu.setParentId(1L);
        sysMenu.setUrl("/test");
        sysMenu.setPermission("sys:test:view");
        sysMenu = this.sysMenuDao.add(sysMenu);
        Assertions.assertThat(sysMenu.getId()).isNotNull();
    }

    /**
     * 测试根据 ID 删除菜单信息
     *
     * @author shiloh
     * @date 2023/4/2 23:25
     */
    @Test
    public void deleteById() {
        this.sysMenuDao.deleteById(7L);
        Assertions.assertThat(this.sysMenuDao.findById(7L)).isNull();
    }

    /**
     * 测试修改菜单信息
     *
     * @author shiloh
     * @date 2023/4/2 23:25
     */
    @Test
    public void update() {
        final SysMenu sysMenu = this.sysMenuDao.findById(5L);
        sysMenu.setName(sysMenu.getName() + "-按钮");
        this.sysMenuDao.update(sysMenu);
    }

    /**
     * 测试根据 ID 查询菜单信息
     *
     * @author shiloh
     * @date 2023/4/2 23:25
     */
    @Test
    public void findById() {
        Assertions.assertThat(this.sysMenuDao.findById(1L)).isNotNull();
    }

    /**
     * 测试查询所有菜单信息
     *
     * @author shiloh
     * @date 2023/4/2 23:25
     */
    @Test
    public void findAll() {
        Assertions.assertThat(this.sysMenuDao.findAll()).isNotEmpty();
    }
}