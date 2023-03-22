package org.shiloh.web.dao;

import org.shiloh.web.entity.User;

import java.util.List;

/**
 * User DAO
 *
 * @author shiloh
 * @date 2023/3/19 14:41
 */
public interface UserDao {
    /**
     * 创建用户
     *
     * @param user 用户实体
     * @return 带主键的用户实体
     * @author shiloh
     * @date 2023/3/19 14:43
     */
    User create(User user);

    /**
     * 根据 ID 删除用户信息
     *
     * @param id 用户 ID
     * @author shiloh
     * @date 2023/3/19 14:43
     */
    void deleteById(Long id);

    /**
     * 更新用户信息
     *
     * @param user 用户实体
     * @return 用户实体
     * @author shiloh
     * @date 2023/3/19 14:43
     */
    User update(User user);

    /**
     * 根据 ID 查询用户信息
     *
     * @param id 用户 ID
     * @return 用户实体
     * @author shiloh
     * @date 2023/3/19 14:44
     */
    User findById(Long id);

    /**
     * 查询所有用户信息
     *
     * @return 用户实体列表
     * @author shiloh
     * @date 2023/3/19 14:44
     */
    List<User> findAll();

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户实体
     * @author shiloh
     * @date 2023/3/20 22:29
     */
    User findByUsername(String username);
}
