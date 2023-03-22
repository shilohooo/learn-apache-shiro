package org.shiloh.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.shiloh.web.dao.UserDao;
import org.shiloh.web.entity.User;
import org.shiloh.web.service.PasswordHelper;
import org.shiloh.web.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User Service Impl
 *
 * @author shiloh
 * @date 2023/3/20 22:27
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordHelper passwordHelper;

    /**
     * 创建用户
     *
     * @param user 用户实体
     * @return 带主键的用户实体
     * @author shiloh
     * @date 2023/3/19 14:43
     */
    @Override
    public User create(User user) {
        this.passwordHelper.encryptPassword(user);
        return this.userDao.create(user);
    }

    /**
     * 根据 ID 删除用户信息
     *
     * @param id 用户 ID
     * @author shiloh
     * @date 2023/3/19 14:43
     */
    @Override
    public void deleteById(Long id) {
        this.userDao.deleteById(id);
    }

    /**
     * 更新用户信息
     *
     * @param user 用户实体
     * @return 用户实体
     * @author shiloh
     * @date 2023/3/19 14:43
     */
    @Override
    public User update(User user) {
        return this.userDao.update(user);
    }

    /**
     * 根据 ID 查询用户信息
     *
     * @param id 用户 ID
     * @return 用户实体
     * @author shiloh
     * @date 2023/3/19 14:44
     */
    @Override
    public User findById(Long id) {
        return this.userDao.findById(id);
    }

    /**
     * 查询所有用户信息
     *
     * @return 用户实体列表
     * @author shiloh
     * @date 2023/3/19 14:44
     */
    @Override
    public List<User> findAll() {
        return this.userDao.findAll();
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户实体
     * @author shiloh
     * @date 2023/3/20 22:29
     */
    @Override
    public User findByUsername(String username) {
        return this.userDao.findByUsername(username);
    }

    /**
     * 根据 ID 修改用户密码
     *
     * @param id          ID
     * @param newPassword 新密码
     * @author shiloh
     * @date 2023/3/20 22:30
     */
    @Override
    public void changePassword(Long id, String newPassword) {
        final User user = this.userDao.findById(id);
        user.setPassword(newPassword);
        this.passwordHelper.encryptPassword(user);
        this.userDao.update(user);
    }
}
