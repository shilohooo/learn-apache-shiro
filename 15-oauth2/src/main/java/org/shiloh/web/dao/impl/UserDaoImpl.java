package org.shiloh.web.dao.impl;

import lombok.RequiredArgsConstructor;
import org.shiloh.web.dao.UserDao;
import org.shiloh.web.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

/**
 * UserDao Impl
 *
 * @author shiloh
 * @date 2023/3/19 14:44
 */
@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

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
        final String sql = "insert into learn_shiro.oauth2_user(username, password, salt) values (?, ?, ?)";
        // 保存新增成功后的主键
        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            final PreparedStatement statement = connection.prepareStatement(sql, new String[]{"id"});
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getSalt());
            return statement;
        }, generatedKeyHolder);

        user.setId(Objects.requireNonNull(generatedKeyHolder.getKey()).longValue());
        return user;
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
        final String sql = "delete from learn_shiro.oauth2_user where id = ?";
        this.jdbcTemplate.update(sql, id);
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
        final String sql = "update learn_shiro.oauth2_user set username = ?, password = ?, salt = ? where id = ?";
        this.jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getSalt(), user.getId());
        return user;
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
        try {
            final String sql = "select * from learn_shiro.oauth2_user where id = ?";
            return this.jdbcTemplate.queryForObject(sql, new User(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
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
        final String sql = "select * from learn_shiro.oauth2_user";
        return this.jdbcTemplate.query(sql, new User());
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
        try {
            final String sql = "select * from learn_shiro.oauth2_user where username = ?";
            return this.jdbcTemplate.queryForObject(sql, new User(), username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
