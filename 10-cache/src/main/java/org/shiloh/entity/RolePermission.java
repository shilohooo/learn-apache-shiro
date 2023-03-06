package org.shiloh.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * 角色权限关联实体
 *
 * @author shiloh
 * @date 2023/2/28 17:58
 */
@Setter
@Getter
@ToString
public class RolePermission implements Serializable {
    private static final long serialVersionUID = 1980067851503391822L;

    /**
     * 角色 ID
     */
    private Long roleId;

    /**
     * 权限 ID
     */
    private Long permissionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RolePermission that = (RolePermission) o;
        return Objects.equals(getRoleId(), that.getRoleId())
                && Objects.equals(getPermissionId(), that.getPermissionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(), getPermissionId());
    }
}
