package org.shiloh.web.dao.impl;

import lombok.RequiredArgsConstructor;
import org.shiloh.web.dao.RoleDao;
import org.shiloh.web.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;

/**
 * 角色信息 DAO Impl
 *
 * @author shiloh
 * @date 2023/2/28 22:31
 */
@Repository
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class RoleDaoImpl implements RoleDao {
    /* =================== SQL ===================== */

    /**
     * SQL：新增角色信息
     */
    private static final String INSERT = "insert into learn_shiro.sys_roles(role, description, available)" +
            " values(?, ? ,?)";

    /**
     * SQL：根据 ID 删除角色信息
     */
    private static final String DELETE_BY_ID = "delete from learn_shiro.sys_roles where id = ?";

    /**
     * SQL：根据 ID 删除角色与用户关联数据
     */
    private static final String DELETE_USER_ROLE_REF = "delete from learn_shiro.sys_users_roles where role_id = ?";

    /**
     * SQL：新增角色与权限关联数据
     */
    private static final String INSERT_ROLE_PERMISSION_REF = "insert into" +
            " learn_shiro.sys_roles_permissions(role_id, permission_id) values(?, ?)";

    /**
     * SQL：删除角色与权限关联数据
     */
    private static final String DELETE_ROLE_PERMISSION_REF = "delete from learn_shiro.sys_roles_permissions" +
            " where role_id = ? and permission_id = ?";

    /**
     * SQL：根据角色 ID 和权限 ID 查询角色与权限关联数据是否存在
     */
    private static final String QUERY_ROLE_PERMISSION_REF_EXISTS = "select count(1) from" +
            " learn_shiro.sys_roles_permissions where role_id = ? and permission_id = ?";


    /* =================== INSTANCE FIELDS ===================== */

    private final JdbcTemplate jdbcTemplate;

    /**
     * 新增角色信息
     *
     * @param role 待新增的角色信息
     * @return 包含主键的角色实体
     * @author shiloh
     * @date 2023/2/28 18:29
     */
    @Override
    public Role add(Role role) {
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            final PreparedStatement statement = connection.prepareStatement(INSERT, new String[]{"id"});
            statement.setString(1, role.getRole());
            statement.setString(2, role.getDescription());
            statement.setBoolean(3, role.getAvailable());
            return statement;
        }, keyHolder);
        // 新增成功后，设置新增主键
        role.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return role;
    }

    /**
     * 根据 ID 删除角色信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    @Override
    public void deleteById(Long id) {
        // 先删除用户与角色关联数据
        this.jdbcTemplate.update(DELETE_USER_ROLE_REF, id);
        // 再删除角色信息
        this.jdbcTemplate.update(DELETE_BY_ID, id);
    }

    /**
     * 添加角色与权限的关联关系
     *
     * @param roleId        角色 ID
     * @param permissionIds 权限 ID 可变参数，一个或多个
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    @Override
    public void addPermissionRefs(Long roleId, Long... permissionIds) {
        if (permissionIds == null || permissionIds.length == 0) {
            return;
        }

        for (final Long permissionId : permissionIds) {
            // 已存在的不用再次添加
            if (this.isRolePermissionRefExists(roleId, permissionId)) {
                continue;
            }

            this.jdbcTemplate.update(INSERT_ROLE_PERMISSION_REF, roleId, permissionId);
        }
    }

    /**
     * 移除角色与权限的关联关系
     *
     * @param roleId        角色 ID
     * @param permissionIds 权限 ID 可变参数，一个或多个
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    @Override
    public void removePermissionRefs(Long roleId, Long... permissionIds) {
        if (permissionIds == null || permissionIds.length == 0) {
            return;
        }

        for (final Long permissionId : permissionIds) {
            // 不存在的不用删除
            if (!this.isRolePermissionRefExists(roleId, permissionId)) {
                continue;
            }

            this.jdbcTemplate.update(DELETE_ROLE_PERMISSION_REF, roleId, permissionId);
        }
    }

    /**
     * 根据角色 ID 和权限 ID 查询角色与权限的关联关系是否存在
     *
     * @param roleId       角色 ID
     * @param permissionId 权限 ID
     * @return 如果角色与权限的关联关系存在则返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/2/28 22:46
     */
    @Override
    public Boolean isRolePermissionRefExists(Long roleId, Long permissionId) {
        final Integer count = this.jdbcTemplate.queryForObject(
                QUERY_ROLE_PERMISSION_REF_EXISTS,
                Integer.class,
                roleId,
                permissionId
        );

        return count != null && count > 0;
    }
}
