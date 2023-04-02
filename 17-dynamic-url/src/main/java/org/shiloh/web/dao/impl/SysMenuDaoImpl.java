package org.shiloh.web.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shiloh.web.dao.SysMenuDao;
import org.shiloh.web.entity.SysMenu;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * 系统菜单 DAO Impl
 *
 * @author shiloh
 * @date 2023/4/2 23:15
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class SysMenuDaoImpl implements SysMenuDao {
    private final JdbcTemplate jdbcTemplate;

    /**
     * 新增菜单
     *
     * @param sysMenu 菜单信息
     * @return 新增后带主键的菜单信息
     * @author shiloh
     * @date 2023/4/2 23:13
     */
    @Override
    public SysMenu add(SysMenu sysMenu) {
        final String sql = "insert into learn_shiro.sys_menu(name, type, order_no, url, parent_id, permission, available) values(?, ?, ?, ?, ?, ?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(con -> {
            final PreparedStatement statement = con.prepareStatement(sql, new String[]{"id"});
            statement.setString(1, sysMenu.getName());
            statement.setInt(2, sysMenu.getType());
            statement.setInt(3, sysMenu.getOrderNo());
            statement.setString(4, sysMenu.getUrl());
            statement.setLong(5, sysMenu.getParentId());
            statement.setString(6, sysMenu.getPermission());
            statement.setBoolean(7, sysMenu.getAvailable());
            return statement;
        }, keyHolder);

        final Number key = keyHolder.getKey();
        if (key != null) {
            sysMenu.setId(key.longValue());
        }

        return sysMenu;
    }

    /**
     * 根据 ID 删除菜单
     *
     * @param id ID
     * @author shiloh
     * @date 2023/4/2 23:14
     */
    @Override
    public void deleteById(Long id) {
        final String sql = "delete from learn_shiro.sys_menu where id = ?";
        this.jdbcTemplate.update(sql, id);
    }

    /**
     * 修改菜单信息
     *
     * @param sysMenu 菜单信息
     * @return 修改后的菜单信息
     * @author shiloh
     * @date 2023/4/2 23:14
     */
    @Override
    public SysMenu update(SysMenu sysMenu) {
        final String sql = "update learn_shiro.sys_menu set name = ?, type = ?, order_no = ?, url = ?, parent_id = ?, permission = ?, available = ? where id = ?";
        this.jdbcTemplate.update(
                sql,
                sysMenu.getName(),
                sysMenu.getType(),
                sysMenu.getOrderNo(),
                sysMenu.getUrl(),
                sysMenu.getParentId(),
                sysMenu.getPermission(),
                sysMenu.getAvailable(),
                sysMenu.getId()
        );

        return sysMenu;
    }

    /**
     * 根据 ID 查询菜单信息
     *
     * @param id ID
     * @return 菜单信息
     * @author shiloh
     * @date 2023/4/2 23:14
     */
    @Override
    public SysMenu findById(Long id) {
        final String sql = "select * from learn_shiro.sys_menu where id = ?";
        try {
            return this.jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(SysMenu.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            log.error("根据 ID：【{}】查询系统菜单出错：", id, e);
            return null;
        }
    }

    /**
     * 查询所有菜单列表
     *
     * @return 菜单列表
     * @author shiloh
     * @date 2023/4/2 23:15
     */
    @Override
    public List<SysMenu> findAll() {
        final String sql = "select * from learn_shiro.sys_menu";
        return this.jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SysMenu.class));
    }
}
