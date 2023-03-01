package org.shiloh.test.service;

import org.junit.Test;
import org.shiloh.test.base.BaseTests;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Service 单元测试
 *
 * @author shiloh
 * @date 2023/3/1 16:46
 */
public class ServiceTest extends BaseTests {
    /**
     * 测试用户-角色-权限关联关系
     *
     * @author shiloh
     * @date 2023/3/1 17:18
     */
    @Test
    public void testUserRolePermissionRef() {
        // shiloh
        Set<String> roles = this.userService.findRolesByUsername(user1.getUsername());
        assertThat(roles).isNotEmpty();
        assertThat(roles.size()).isEqualTo(1);
        assertThat(roles).contains(role1.getRole());

        Set<String> permissions = this.userService.findPermissionsByUsername(user1.getUsername());
        assertThat(permissions).isNotEmpty();
        assertThat(permissions.size()).isEqualTo(3);
        assertThat(permissions).contains(permission3.getPermission());

        // tom
        roles = this.userService.findRolesByUsername(user2.getUsername());
        assertThat(roles).isEmpty();

        permissions = this.userService.findPermissionsByUsername(user2.getUsername());
        assertThat(permissions).isEmpty();

        // 解除 admin 角色与权限 menu:create 的关联关系
        this.roleService.removePermissionRefs(role1.getId(), permission3.getId());
        permissions = this.userService.findPermissionsByUsername(user1.getUsername());
        assertThat(permissions).isNotEmpty();
        assertThat(permissions.size()).isEqualTo(2);

        // 删除一个权限信息
        this.permissionService.deleteById(permission2.getId());
        permissions = this.userService.findPermissionsByUsername(user1.getUsername());
        assertThat(permissions).isNotEmpty();
        assertThat(permissions.size()).isEqualTo(1);

        // 接触用户 shiloh 与角色 admin 的关联关系
        this.userService.removeRoleRefs(user1.getId(), role1.getId());
        roles = this.userService.findRolesByUsername(user1.getUsername());
        assertThat(roles).isEmpty();
    }
}