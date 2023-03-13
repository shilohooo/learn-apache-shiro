package org.shiloh.web.entity;

import lombok.*;
import org.shiloh.web.entity.base.BaseEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 角色实体
 *
 * @author shiloh
 * @date 2023/2/28 17:47
 */
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity implements RowMapper<Role> {
    private static final long serialVersionUID = -8454431431240769944L;

    /**
     * 角色标识，程序中判断使用，如："admin"
     */
    private String role;

    /**
     * 角色描述信息，用于展示
     */
    private String description;

    /**
     * 是否可以，如果不可用将不会添加给用户
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
        final Role role = (Role) o;
        return Objects.equals(getId(), role.getId());
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
     * @return 角色信息实体
     * @author shiloh
     * @date 2023/2/28 18:17
     */
    @Override
    public Role mapRow(ResultSet resultSet, int i) throws SQLException {
        final Role role = new Role();
        role.setId(resultSet.getLong("id"));
        role.setRole(resultSet.getString("role"));
        role.setDescription(resultSet.getString("description"));
        role.setAvailable(resultSet.getBoolean("available"));
        return role;
    }
}
