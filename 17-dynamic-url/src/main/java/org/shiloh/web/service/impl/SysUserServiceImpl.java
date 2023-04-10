package org.shiloh.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.shiloh.web.dao.SysUserDao;
import org.shiloh.web.entity.SysUser;
import org.shiloh.web.service.SysUserService;
import org.shiloh.web.utils.PasswordUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 系统用户信息 Service Impl
 *
 * @author shiloh
 * @date 2023/4/5 22:14
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {
    private final SysUserDao sysUserDao;
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
        // 密码加密
        sysUser.setPassword(PasswordUtils.encrypt(sysUser.getPassword(), sysUser.getSalt()));
        return this.sysUserDao.add(sysUser);
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
        this.sysUserDao.deleteById(id);
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
        return this.sysUserDao.update(sysUser);
    }

    /**
     * 查询所有用户信息
     *
     * @return 用户列表
     * @author shiloh
     * @date 2023/3/31 23:08
     */
    @Override
    public List<SysUser> getAll() {
        return this.sysUserDao.findAll();
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
    public SysUser getById(Long id) {
        return this.sysUserDao.findById(id);
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
    public SysUser getByUsername(String username) {
        return this.sysUserDao.findByUsername(username);
    }

    /**
     * 修改用户密码
     *
     * @param id          用户 ID
     * @param newPassword 新密码
     * @author shiloh
     * @date 2023/4/5 22:13
     */
    @Override
    public void changePassword(Long id, String newPassword) {
        final SysUser sysUser = this.sysUserDao.findById(id);
        if (sysUser == null) {
            return;
        }
        sysUser.setPassword(PasswordUtils.encrypt(newPassword, sysUser.getSalt()));
        this.sysUserDao.update(sysUser);
    }

    /**
     * 根据用户 ID 获取该用户的所有角色
     *
     * @param userId 用户 ID
     * @return 角色列表
     * @author shiloh
     * @date 2023/4/5 22:14
     */
    @Override
    public Set<String> getRoles(Long userId) {
        final String sql = "select r.name from learn_shiro.sys_user u inner join learn_shiro.sys_user_role ur" +
                " on u.id = ur.user_id inner join learn_shiro.sys_role r on ur.role_id = r.id where u.id = ?";
        final List<String> roles = this.jdbcTemplate.queryForList(sql, String.class, userId);
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptySet();
        }

        return new HashSet<>(roles);
    }

    /**
     * 根据用户 ID 获取该用户的所有权限
     *
     * @param userId 用户 ID
     * @return 权限列表
     * @author shiloh
     * @date 2023/4/5 22:14
     */
    @Override
    public Set<String> getPermissions(Long userId) {
        final String sql = "select m.permission from learn_shiro.sys_user u" +
                " inner join learn_shiro.sys_user_role ur on u.id = ur.user_id" +
                " inner join learn_shiro.sys_role r on ur.role_id = r.id" +
                " inner join learn_shiro.sys_role_menu rm on r.id = rm.role_id" +
                " inner join learn_shiro.sys_menu m on rm.menu_id = m.id" +
                " where u.id = ?";
        final List<String> permissions = this.jdbcTemplate.queryForList(sql, String.class, userId);
        if (CollectionUtils.isEmpty(permissions)) {
            return Collections.emptySet();
        }

        return new HashSet<>(permissions);
    }
}
