package org.shiloh.web.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shiloh.web.dao.SysRoleDao;
import org.shiloh.web.entity.SysRole;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * @author shiloh
 * @date 2023/4/1 23:29
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class SysRoleDaoImpl implements SysRoleDao {
    private final JdbcTemplate jdbcTemplate;

    /**
     * 新增角色
     *
     * @param sysRole 角色信息
     * @return 新增后带主键的角色信息
     * @author shiloh
     * @date 2023/4/1 23:28
     */
    @Override
    public SysRole add(SysRole sysRole) {
        final String sql = "insert into learn_shiro.sys_role(name, parent_id, available) values(?, ?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(con -> {
            final PreparedStatement statement = con.prepareStatement(sql, new String[]{"id"});
            statement.setString(1, sysRole.getName());
            statement.setLong(2, sysRole.getParentId());
            statement.setBoolean(3, sysRole.getAvailable());
            return statement;
        }, keyHolder);

        final Number key = keyHolder.getKey();
        if (key != null) {
            sysRole.setId(key.longValue());
        }

        return sysRole;
    }

    /**
     * 根据 ID 删除角色信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/4/1 23:28
     */
    @Override
    public void deleteById(Long id) {
        final String sql = "delete from learn_shiro.sys_role where id = ?";
        this.jdbcTemplate.update(sql, id);
    }

    /**
     * 根据 ID 修改角色信息
     *
     * @param sysRole 角色信息
     * @return 修改后的角色信息
     * @author shiloh
     * @date 2023/4/1 23:28
     */
    @Override
    public SysRole update(SysRole sysRole) {
        final String sql = "update learn_shiro.sys_role set name = ?, parent_id = ?, available = ? where id = ?";
        this.jdbcTemplate.update(
                sql,
                sysRole.getName(),
                sysRole.getParentId(),
                sysRole.getAvailable(),
                sysRole.getId()
        );

        return sysRole;
    }

    /**
     * 根据 ID 查询角色信息
     *
     * @param id ID
     * @return 角色信息
     * @author shiloh
     * @date 2023/4/1 23:29
     */
    @Override
    public SysRole findById(Long id) {
        final String sql = "select * from learn_shiro.sys_role where id = ?";
        try {
            return this.jdbcTemplate.queryForObject(sql, new SysRole(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            log.error("根据ID：【{}】查询角色信息出错：", id, e);
            return null;
        }
    }

    /**
     * 查询所有角色信息
     *
     * @return 角色列表
     * @author shiloh
     * @date 2023/4/1 23:29
     */
    @Override
    public List<SysRole> findAll() {
        final String sql = "select * from learn_shiro.sys_role";
        return this.jdbcTemplate.query(sql, new SysRole());
    }
}
