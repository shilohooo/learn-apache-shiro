package org.shiloh.web.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shiloh.web.dao.SysUserDao;
import org.shiloh.web.entity.SysUser;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * @author shiloh
 * @date 2023/3/31 23:09
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class SysUserDaoImpl implements SysUserDao {
    private final JdbcTemplate jdbcTemplate;

    /**
     * 新增用户
     *
     * @param sysUser 用户信息
     * @return 新增后带主键的用户信息
     * @author shiloh
     * @date 2023/4/1 22:58
     */
    @Override
    public SysUser add(SysUser sysUser) {
        final String sql = "insert into learn_shiro.sys_user(username, password, salt, locked) values(?, ?, ?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(con -> {
            final PreparedStatement statement = con.prepareStatement(sql, new String[]{"id"});
            statement.setString(1, sysUser.getUsername());
            statement.setString(2, sysUser.getPassword());
            statement.setString(3, sysUser.getSalt());
            statement.setBoolean(4, sysUser.getLocked());

            return statement;
        }, keyHolder);

        final Number key = keyHolder.getKey();
        if (key != null) {
            sysUser.setId(key.longValue());
        }

        return sysUser;
    }

    /**
     * 根据 ID 删除用户信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/4/1 22:59
     */
    @Override
    public void deleteById(Long id) {
        final String sql = "delete from learn_shiro.sys_user where id = ?";
        this.jdbcTemplate.update(sql, id);
    }

    /**
     * 根据 ID 更新用户信息
     *
     * @param sysUser 用户信息
     * @return 更新后的用户信息
     * @author shiloh
     * @date 2023/4/1 22:59
     */
    @Override
    public SysUser update(SysUser sysUser) {
        final String sql = "update learn_shiro.sys_user set username = ?, password = ?, salt = ?, locked = ? where id = ?";
        this.jdbcTemplate.update(
                sql,
                sysUser.getUsername(),
                sysUser.getPassword(),
                sysUser.getSalt(),
                sysUser.getLocked(),
                sysUser.getId()
        );

        return sysUser;
    }

    /**
     * 查询所有用户信息
     *
     * @return 用户列表
     * @author shiloh
     * @date 2023/3/31 23:08
     */
    @Override
    public List<SysUser> findAll() {
        final String sql = "select * from learn_shiro.sys_user";
        return this.jdbcTemplate.query(sql, new SysUser());
    }

    /**
     * 根据 ID 查询用户信息
     *
     * @param id ID
     * @return 用户信息
     * @author shiloh
     * @date 2023/3/31 23:08
     */
    @Override
    public SysUser findById(Long id) {
        final String sql = "select * from learn_shiro.sys_user where id = ?";
        try {
            return this.jdbcTemplate.queryForObject(sql, new SysUser(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            log.error("根据 ID：【{}】 查询用户信息出错：", id, e);
            return null;
        }
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     * @author shiloh
     * @date 2023/3/31 23:08
     */
    @Override
    public SysUser findByUsername(String username) {
        final String sql = "select * from learn_shiro.sys_user where username = ?";
        try {
            return this.jdbcTemplate.queryForObject(sql, new SysUser(), username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            log.error("根据用户名：【{}】 查询用户信息出错：", username, e);
            return null;
        }
    }
}
