package org.shiloh.shiro.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.DomainPermission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 自定义角色权限解析器
 *
 * @author shiloh
 * @date 2022/2/10 16:44
 */
public class CustomRolePermissionResolver implements RolePermissionResolver {
    /**
     * 从角色中解析出权限信息
     *
     * @param roleString 角色字符串
     * @return 权限信息集合
     * @author shiloh
     * @date 2022/2/10 16:45
     */
    @Override
    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        if ("role1".equals(roleString)) {
            return List.of(new WildcardPermission("menu:*"));
        }
        return null;
    }
}
