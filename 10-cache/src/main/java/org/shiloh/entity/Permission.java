package org.shiloh.entity;

import lombok.*;
import org.shiloh.entity.base.BaseEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 权限实体
 *
 * @author shiloh
 * @date 2023/2/28 17:50
 */
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseEntity implements RowMapper<Permission> {
    private static final long serialVersionUID = -6628063033051192038L;

    /**
     * 权限标识，用于程序判断，如："sys:user:create"
     */
    private String permission;

    /**
     * 权限描述信息，用于界面展示
     */
    private String description;

    /**
     * 是否可用，如果不可用将不会添加给用户
     */
    private Boolean available = Boolean.FALSE;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final Permission permission = (Permission) o;
        return Objects.equals(getId(), permission.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }

    /**
     * {@link org.springframework.jdbc.core.JdbcTemplate} 查询映射
     *
     * @param resultSet 查询结果集
     * @param i         当前下标
     * @return 权限信息实体
     * @author shiloh
     * @date 2023/2/28 18:17
     */
    @Override
    public Permission mapRow(ResultSet resultSet, int i) throws SQLException {
        final Permission permission = new Permission();
        permission.setId(resultSet.getLong("id"));
        permission.setPermission(resultSet.getString("permission"));
        permission.setDescription(resultSet.getString("description"));
        permission.setAvailable(resultSet.getBoolean("available"));
        return permission;
    }
}
