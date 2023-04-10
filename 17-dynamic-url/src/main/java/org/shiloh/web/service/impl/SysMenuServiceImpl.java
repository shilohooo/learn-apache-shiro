package org.shiloh.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.shiloh.web.dao.SysMenuDao;
import org.shiloh.web.entity.SysMenu;
import org.shiloh.web.service.SysMenuService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统菜单 Service Impl
 *
 * @author shiloh
 * @date 2023/4/10 22:26
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {
    private final SysMenuDao sysMenuDao;
    private final JdbcTemplate jdbcTemplate;

    /**
     * 新增菜单
     *
     * @param sysMenu 菜单信息
     * @return 新增后带主键的菜单信息
     * @author shiloh
     * @date 2023/4/10 22:23
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysMenu add(SysMenu sysMenu) {
        return this.sysMenuDao.add(sysMenu);
    }

    /**
     * 根据 ID 删除菜单
     *
     * @param id ID
     * @author shiloh
     * @date 2023/4/10 22:23
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        this.sysMenuDao.deleteById(id);
    }

    /**
     * 修改菜单信息
     *
     * @param sysMenu 菜单信息
     * @return 修改后的菜单信息
     * @author shiloh
     * @date 2023/4/10 22:23
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysMenu update(SysMenu sysMenu) {
        return this.sysMenuDao.update(sysMenu);
    }

    /**
     * 根据 ID 查询菜单信息
     *
     * @param id ID
     * @return 菜单信息
     * @author shiloh
     * @date 2023/4/10 22:23
     */
    @Override
    public SysMenu getById(Long id) {
        return this.sysMenuDao.findById(id);
    }

    /**
     * 查询所有菜单列表
     *
     * @return 菜单列表
     * @author shiloh
     * @date 2023/4/10 22:23
     */
    @Override
    public List<SysMenu> getAll() {
        return this.sysMenuDao.findAll();
    }

    /**
     * 获取用户可见菜单列表
     *
     * @param userId 用户 ID
     * @return 用户可见菜单列表
     * @author shiloh
     * @date 2023/4/10 22:25
     */
    @Override
    public List<SysMenu> getUserMenus(Long userId) {
        final String sql = "select m.id, m.name, m.type, m.order_no, m.url,m.parent_id, m.permission, m.available" +
                " from learn_shiro.sys_user u" +
                " inner join learn_shiro.sys_user_role ur on u.id = ur.user_id" +
                " inner join learn_shiro.sys_role r on ur.role_id = r.id" +
                " inner join learn_shiro.sys_role_menu rm on r.id = rm.role_id" +
                " inner join learn_shiro.sys_menu m on rm.menu_id = m.id" +
                " where u.id = ?";

        return this.jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SysMenu.class), userId);
    }
}
