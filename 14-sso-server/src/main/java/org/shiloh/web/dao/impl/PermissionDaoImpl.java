package org.shiloh.web.dao.impl;

import lombok.RequiredArgsConstructor;
import org.shiloh.web.dao.PermissionDao;
import org.shiloh.web.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;

/**
 * 权限信息 DAO Impl
 *
 * @author shiloh
 * @date 2023/2/28 18:32
 */
@Repository
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class PermissionDaoImpl implements PermissionDao {
    /* =================== SQL ===================== */

    /**
     * SQL：新增权限信息
     */
    private static final String INSERT = "insert into learn_shiro.sys_permissions(permission, description, available)" +
            " values(?, ?, ?)";

    /**
     * SQL：根据 ID 删除权限信息
     */
    private static final String DELETE_BT_ID = "delete from learn_shiro.sys_permissions where id = ?";

    /**
     * SQL：根据权限 ID 删除角色与权限的关联数据
     */
    private static final String DELETE_ROLE_PERMISSION_REF = "delete from learn_shiro.sys_roles_permissions" +
            " where permission_id = ?";

    /* =================== INSTANCE FIELDS ===================== */

    private final JdbcTemplate jdbcTemplate;

    /**
     * 新增权限信息
     *
     * @param permission 待新增的权限信息
     * @return 包含主键的权限实体
     * @author shiloh
     * @date 2023/2/28 18:31
     */
    @Override
    public Permission add(Permission permission) {
        // 用于获取新增后的主键
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            // 第二个参数指定要从插入的行数据中返回的字段名称
            final PreparedStatement statement = connection.prepareStatement(INSERT, new String[]{"id"});
            statement.setString(1, permission.getPermission());
            statement.setString(2, permission.getDescription());
            statement.setBoolean(3, permission.getAvailable());
            return statement;
        }, keyHolder);
        // 插入成功后设置自增主键
        permission.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return permission;
    }

    /**
     * 根据 ID 删除权限信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    @Override
    public void deleteById(Long id) {
        // 先删掉角色与权限关联数据
        this.jdbcTemplate.update(DELETE_ROLE_PERMISSION_REF, id);
        // 再删除权限信息
        this.jdbcTemplate.update(DELETE_BT_ID, id);
    }
}
