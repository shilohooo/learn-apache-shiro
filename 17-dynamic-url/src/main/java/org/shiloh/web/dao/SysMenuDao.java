package org.shiloh.web.dao;

import org.shiloh.web.entity.SysMenu;

import java.util.List;

/**
 * 系统菜单 DAO
 *
 * @author shiloh
 * @date 2023/4/2 23:11
 */
public interface SysMenuDao {
    /**
     * 新增菜单
     *
     * @param sysMenu 菜单信息
     * @return 新增后带主键的菜单信息
     * @author shiloh
     * @date 2023/4/2 23:13
     */
    SysMenu add(SysMenu sysMenu);

    /**
     * 根据 ID 删除菜单
     *
     * @param id ID
     * @author shiloh
     * @date 2023/4/2 23:14
     */
    void deleteById(Long id);

    /**
     * 修改菜单信息
     *
     * @param sysMenu 菜单信息
     * @return 修改后的菜单信息
     * @author shiloh
     * @date 2023/4/2 23:14
     */
    SysMenu update(SysMenu sysMenu);

    /**
     * 根据 ID 查询菜单信息
     *
     * @param id ID
     * @return 菜单信息
     * @author shiloh
     * @date 2023/4/2 23:14
     */
    SysMenu findById(Long id);

    /**
     * 查询所有菜单列表
     *
     * @return 菜单列表
     * @author shiloh
     * @date 2023/4/2 23:15
     */
    List<SysMenu> findAll();
}
