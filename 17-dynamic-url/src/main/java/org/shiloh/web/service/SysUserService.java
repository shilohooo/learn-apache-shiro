package org.shiloh.web.service;

import org.shiloh.web.entity.SysUser;

import java.util.Collection;
import java.util.List;

/**
 * 系统用户信息 Service
 *
 * @author shiloh
 * @date 2023/4/5 22:11
 */
public interface SysUserService {
    /**
     * 新增用户
     *
     * @param sysUser 用户信息
     * @return 新增后带主键的用户信息
     * @author shiloh
     * @date 2023/4/1 22:58
     */
    SysUser add(SysUser sysUser);

    /**
     * 根据 ID 删除用户信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/4/1 22:59
     */
    void deleteById(Long id);

    /**
     * 根据 ID 更新用户信息
     *
     * @param sysUser 用户信息
     * @return 更新后的用户信息
     * @author shiloh
     * @date 2023/4/1 22:59
     */
    SysUser update(SysUser sysUser);

    /**
     * 查询所有用户信息
     *
     * @return 用户列表
     * @author shiloh
     * @date 2023/3/31 23:08
     */
    List<SysUser> getAll();

    /**
     * 根据 ID 查询用户信息
     *
     * @param id ID
     * @return 用户信息
     * @author shiloh
     * @date 2023/3/31 23:08
     */
    SysUser getById(Long id);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     * @author shiloh
     * @date 2023/3/31 23:08
     */
    SysUser getByUsername(String username);

    /**
     * 修改用户密码
     *
     * @param id          用户 ID
     * @param newPassword 新密码
     * @author shiloh
     * @date 2023/4/5 22:13
     */
    void changePassword(Long id, String newPassword);

    /**
     * 根据用户 ID 获取该用户的所有角色
     *
     * @param userId 用户 ID
     * @return 角色列表
     * @author shiloh
     * @date 2023/4/5 22:14
     */
    Collection<String> getRoles(Long userId);

    /**
     * 根据用户 ID 获取该用户的所有权限
     *
     * @param userId 用户 ID
     * @return 权限列表
     * @author shiloh
     * @date 2023/4/5 22:14
     */
    Collection<String> getPermissions(Long userId);
}
