package org.shiloh.web.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shiloh.web.dao.SysUserDao;
import org.shiloh.web.entity.SysUser;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
     * 查询所有用户信息
     *
     * @return 用户列表
     * @author shiloh
     * @date 2023/3/31 23:08
     */
    @Override
    public List<SysUser> findAll() {
        final String sql = "select * from learn_shiro.sys_user";
        return this.jdbcTemplate.queryForList(sql, SysUser.class);
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
        } catch (DataAccessException e) {
            log.error("根据用户名：【{}】 查询用户信息出错：", username, e);
            return null;
        }
    }
}
