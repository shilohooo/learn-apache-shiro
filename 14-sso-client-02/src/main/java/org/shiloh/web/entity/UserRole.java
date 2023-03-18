package org.shiloh.web.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用户角色关联实体
 *
 * @author shiloh
 * @date 2023/2/28 17:55
 */
@Setter
@Getter
@ToString
public class UserRole implements Serializable {
    private static final long serialVersionUID = -2086991711898550385L;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 角色 ID
     */
    private Long roleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserRole userRole = (UserRole) o;
        return Objects.equals(getUserId(), userRole.getUserId())
                && Objects.equals(getRoleId(), userRole.getRoleId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getRoleId());
    }
}
