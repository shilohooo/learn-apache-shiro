package org.shiloh.web.dao;

import org.shiloh.web.entity.SysUser;

import java.util.List;

/**
 * 系统用户信息 DAO
 *
 * @author shiloh
 * @date 2023/3/31 23:07
 */
public interface SysUserDao {
    /**
     * 查询所有用户信息
     *
     * @return 用户列表
     * @author shiloh
     * @date 2023/3/31 23:08
     */
    List<SysUser> findAll();

    /**
     * 根据 ID 查询用户信息
     *
     * @param id ID
     * @return 用户信息
     * @author shiloh
     * @date 2023/3/31 23:08
     */
    SysUser findById(Long id);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     * @author shiloh
     * @date 2023/3/31 23:08
     */
    SysUser findByUsername(String username);

}
