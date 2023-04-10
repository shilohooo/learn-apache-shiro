package org.shiloh.web.service;

import org.shiloh.web.entity.SysMenu;

import java.util.List;

/**
 * 系统菜单 Service
 *
 * @author shiloh
 * @date 2023/4/10 22:23
 */
public interface SysMenuService {
    /**
     * 新增菜单
     *
     * @param sysMenu 菜单信息
     * @return 新增后带主键的菜单信息
     * @author shiloh
     * @date 2023/4/10 22:23
     */
    SysMenu add(SysMenu sysMenu);

    /**
     * 根据 ID 删除菜单
     *
     * @param id ID
     * @author shiloh
     * @date 2023/4/10 22:23
     */
    void deleteById(Long id);

    /**
     * 修改菜单信息
     *
     * @param sysMenu 菜单信息
     * @return 修改后的菜单信息
     * @author shiloh
     * @date 2023/4/10 22:23
     */
    SysMenu update(SysMenu sysMenu);

    /**
     * 根据 ID 查询菜单信息
     *
     * @param id ID
     * @return 菜单信息
     * @author shiloh
     * @date 2023/4/10 22:23
     */
    SysMenu getById(Long id);

    /**
     * 查询所有菜单列表
     *
     * @return 菜单列表
     * @author shiloh
     * @date 2023/4/10 22:23
     */
    List<SysMenu> getAll();

    /**
     * 获取用户可见菜单列表
     *
     * @param userId 用户 ID
     * @return 用户可见菜单列表
     * @author shiloh
     * @date 2023/4/10 22:25
     */
    List<SysMenu> getUserMenus(Long userId);
}
