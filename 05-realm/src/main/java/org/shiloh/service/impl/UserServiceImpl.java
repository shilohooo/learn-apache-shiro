package org.shiloh.service.impl;

import org.shiloh.dao.UserDao;
import org.shiloh.dao.impl.UserDaoImpl;
import org.shiloh.entity.User;
import org.shiloh.service.UserService;
import org.shiloh.util.PasswordEncryptUtils;

import java.util.Set;

/**
 * 用户信息 Service Impl
 *
 * @author shiloh
 * @date 2023/3/1 16:40
 */
public class UserServiceImpl implements UserService {
    /* ============================= INSTANCE FIELDS ============================== */

    private final UserDao userDao;

    /* ============================= CONSTRUCTORS ============================== */

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    /**
     * 新增用户信息
     *
     * @param user 待新增的用户信息
     * @return 包含主键的用户实体
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    @Override
    public User add(User user) {
        // 加密密码
        PasswordEncryptUtils.encryptPassword(user);
        return this.userDao.add(user);
    }

    /**
     * 根据 ID 删除用户信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    @Override
    public void deleteById(Long id) {
        this.userDao.deleteById(id);
    }

    /**
     * 根据 ID 更新用户信息
     *
     * @param user 待更新的用户信息
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    @Override
    public void updateById(User user) {
        this.userDao.updateById(user);
    }

    /**
     * 添加用户与角色的关联关系
     *
     * @param userId  用户 ID
     * @param roleIds 角色 ID 可变参数，一个或多个
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    @Override
    public void addRoleRefs(Long userId, Long... roleIds) {
        this.userDao.addRoleRefs(userId, roleIds);
    }

    /**
     * 移除用户与角色的关联关系
     *
     * @param userId  用户 ID
     * @param roleIds 角色 ID 可变参数，一个或多个
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    @Override
    public void removeRoleRefs(Long userId, Long... roleIds) {
        this.userDao.removeRoleRefs(userId, roleIds);
    }

    /**
     * 根据 ID 查询用户信息
     *
     * @param id ID
     * @return 用户实体
     * @author shiloh
     * @date 2023/2/28 23:03
     */
    @Override
    public User findById(Long id) {
        return this.userDao.findById(id);
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息实体
     * @author shiloh
     * @date 2023/2/28 18:26
     */
    @Override
    public User findByUsername(String username) {
        return this.userDao.findByUsername(username);
    }

    /**
     * 根据用户名查询用户所拥有的角色
     *
     * @param username 用户名
     * @return 角色标识集合
     * @author shiloh
     * @date 2023/2/28 18:26
     */
    @Override
    public Set<String> findRolesByUsername(String username) {
        return this.userDao.findRolesByUsername(username);
    }

    /**
     * 根据用户名查询用户所拥有的权限
     *
     * @param username 用户名
     * @return 权限标识集合
     * @author shiloh
     * @date 2023/2/28 18:26
     */
    @Override
    public Set<String> findPermissionsByUsername(String username) {
        return this.userDao.findPermissionsByUsername(username);
    }

    /**
     * 修改密码
     *
     * @param id          用户 ID
     * @param newPassword 新密码
     * @author shiloh
     * @date 2023/3/1 16:42
     */
    @Override
    public void changePassword(Long id, String newPassword) {
        final User user = this.userDao.findById(id);
        user.setPassword(newPassword);
        // 加密密码
        PasswordEncryptUtils.encryptPassword(user);
        this.userDao.updateById(user);
    }
}
