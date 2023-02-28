package org.shiloh.dao.impl;

import org.shiloh.dao.UserDao;
import org.shiloh.entity.User;
import org.shiloh.factory.JdbcTemplateFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 用户信息 DAO Impl
 *
 * @author shiloh
 * @date 2023/2/28 22:51
 */
public class UserDaoImpl implements UserDao {
    /* =================== SQL ===================== */
    /**
     * SQL：新增用户信息
     */
    private static final String INSERT = "insert into learn_shiro.sys_users(username, password, salt, locked)" +
            " values(?, ? ,?, ?)";

    /**
     * SQL：根据 ID 删除用户信息
     */
    private static final String DELETE_BY_ID = "delete from learn_shiro.sys_users where id = ?";


    /**
     * SQL：新增用户与角色关联数据
     */
    private static final String INSERT_USER_ROLE_REF = "insert into learn_shiro.sys_users_roles(USER_ID, ROLE_ID)" +
            " values(?, ?)";

    /**
     * SQL：删除用户与角色关联数据
     */
    private static final String DELETE_USER_ROLE_REF = "delete from learn_shiro.sys_users_roles where user_id = ?" +
            " and role_id = ?";

    /**
     * SQL：根据 ID 修改用户信息
     */
    private static final String UPDATE = "update learn_shiro.sys_users set username = ?, password = ?, salt = ?," +
            " locked = ? where id = ?";

    /**
     * SQL：根据用户 ID 和角色 ID 查询用户与角色关联关系是否存在
     */
    private static final String QUERY_USER_ROLE_REF_EXISTS = "select count(1) from learn_shiro.sys_users_roles" +
            " where user_id = ? and role_id = ?";

    /**
     * SQL：根据 ID 查询用户信息
     */
    private static final String QUERY_BY_ID = "select id, username, password, salt, locked from" +
            " learn_shiro.sys_users where id = ?";

    /**
     * SQL：根据用户名查询用户信息
     */
    private static final String QUERY_BY_USERNAME = "select id, username, password, salt, locked from" +
            " learn_shiro.sys_users where username = ?";

    /**
     * SQL：根据用户名查询用户关联的角色名称
     */
    private static final String QUERY_USER_REF_ROLE_BY_USERNAME = "select r.role from learn_shiro.sys_users u" +
            " inner join learn_shiro.sys_users_roles ur on u.id = ur.user_id" +
            " inner join learn_shiro.sys_roles r on ur.role_id = r.id where u.username = ?";

    /**
     * SQL：根据用户名获取用户关联的权限名称
     */
    private static final String QUERY_USER_REF_PERMISSION_BY_USERNAME = "select p.permission from" +
            " learn_shiro.sys_users u" +
            " inner join learn_shiro.sys_users_roles ur on u.id = ur.user_id" +
            " inner join learn_shiro.sys_roles_permissions rp on ur.role_id = rp.role_id" +
            " inner join learn_shiro.sys_permissions p on rp.permission_id = p.id" +
            " where u.username = ?";


    /* =================== INSTANCE FIELDS ===================== */

    /**
     * JdbcTemplate
     */
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl() {
        this.jdbcTemplate = JdbcTemplateFactory.getInstance();
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
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            final PreparedStatement statement = connection.prepareStatement(INSERT, new String[]{"id"});
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getSalt());
            statement.setBoolean(4, user.getLocked());
            return statement;
        }, keyHolder);
        // 新增成功后，设置自增主键
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return user;
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
        this.jdbcTemplate.update(DELETE_BY_ID, id);
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
        this.jdbcTemplate.update(
                UPDATE,
                user.getUsername(),
                user.getPassword(),
                user.getSalt(),
                user.getLocked(),
                user.getId()
        );
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
        if (roleIds == null || roleIds.length == 0) {
            return;
        }

        for (final Long roleId : roleIds) {
            // 已存在的关联关系不用添加
            if (this.isUserRoleRefExists(userId, roleId)) {
                continue;
            }

            this.jdbcTemplate.update(INSERT_USER_ROLE_REF, userId, roleId);
        }
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
        if (roleIds == null || roleIds.length == 0) {
            return;
        }

        for (final Long roleId : roleIds) {
            // 不存在的关联关系不用删除
            if (!this.isUserRoleRefExists(userId, roleId)) {
                continue;
            }

            this.jdbcTemplate.update(DELETE_USER_ROLE_REF, userId, roleId);
        }
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
        return this.jdbcTemplate.queryForObject(QUERY_BY_ID, new User(), id);
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
        return this.jdbcTemplate.queryForObject(QUERY_BY_USERNAME, new User(), username);
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
        return new HashSet<>(
                this.jdbcTemplate.queryForList(QUERY_USER_REF_ROLE_BY_USERNAME, String.class, username)
        );
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
        return new HashSet<>(
                this.jdbcTemplate.queryForList(QUERY_USER_REF_PERMISSION_BY_USERNAME, String.class, username)
        );
    }

    /**
     * 根据用户 ID 和角色 ID 查询用户与角色的关联关系是否存在
     *
     * @param userId 用户 ID
     * @param roleId 角色 ID
     * @return 如果用户与角色的关联关系存在就返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/2/28 22:53
     */
    @Override
    public Boolean isUserRoleRefExists(Long userId, Long roleId) {
        final Integer count = this.jdbcTemplate.queryForObject(
                QUERY_USER_ROLE_REF_EXISTS,
                Integer.class,
                userId,
                roleId
        );

        return count != null && count > 0;
    }
}
