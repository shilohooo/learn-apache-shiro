package org.shiloh.shiro.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * 自定义位运算权限与通配符权限解析器
 *
 * @author shiloh
 * @date 2022/2/10 16:40
 */
public class BitAndWildcardPermissionResolver implements PermissionResolver {
    /**
     * 将权限字符串解析为{@link Permission}对象
     *
     * @param permissionString 权限字符串
     * @return {@link Permission}
     * @author shiloh
     * @date 2022/2/10 16:41
     */
    @Override
    public Permission resolvePermission(String permissionString) {
        // 如果权限字符串以+开头，则说明时自定义的位运算权限
        if (permissionString.startsWith("+")) {
            return new BitPermission(permissionString);
        }
        return new WildcardPermission(permissionString);
    }
}
