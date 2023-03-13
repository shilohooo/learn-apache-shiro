package org.shiloh.web.dao;

import org.shiloh.web.entity.User;

import java.util.Set;

/**
 * 用户信息 DAO
 *
 * @author shiloh
 * @date 2023/2/28 18:21
 */
public interface UserDao {
    /**
     * 新增用户信息
     *
     * @param user 待新增的用户信息
     * @return 包含主键的用户实体
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    User add(User user);

    /**
     * 根据 ID 删除用户信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    void deleteById(Long id);

    /**
     * 根据 ID 更新用户信息
     *
     * @param user 待更新的用户信息
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    void updateById(User user);

    /**
     * 添加用户与角色的关联关系
     *
     * @param userId  用户 ID
     * @param roleIds 角色 ID 可变参数，一个或多个
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    void addRoleRefs(Long userId, Long... roleIds);

    /**
     * 移除用户与角色的关联关系
     *
     * @param userId  用户 ID
     * @param roleIds 角色 ID 可变参数，一个或多个
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    void removeRoleRefs(Long userId, Long... roleIds);

    /**
     * 根据 ID 查询用户信息
     *
     * @param id ID
     * @return 用户实体
     * @author shiloh
     * @date 2023/2/28 23:03
     */
    User findById(Long id);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息实体
     * @author shiloh
     * @date 2023/2/28 18:26
     */
    User findByUsername(String username);

    /**
     * 根据用户名查询用户所拥有的角色
     *
     * @param username 用户名
     * @return 角色标识集合
     * @author shiloh
     * @date 2023/2/28 18:26
     */
    Set<String> findRolesByUsername(String username);

    /**
     * 根据用户名查询用户所拥有的权限
     *
     * @param username 用户名
     * @return 权限标识集合
     * @author shiloh
     * @date 2023/2/28 18:26
     */
    Set<String> findPermissionsByUsername(String username);

    /**
     * 根据用户 ID 和角色 ID 查询用户与角色的关联关系是否存在
     *
     * @param userId 用户 ID
     * @param roleId 角色 ID
     * @return 如果用户与角色的关联关系存在就返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/2/28 22:53
     */
    Boolean isUserRoleRefExists(Long userId, Long roleId);
}
