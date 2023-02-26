package org.shiloh.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Shiro授权测试用例：角色-权限
 *
 * @author shiloh
 * @date 2022/2/10 15:19
 */
public class ShiroPermissionsTests extends BaseShiroTests {
    /**
     * 测试用户是否具有指定的权限
     * <p>
     * Shiro提供了{@link Subject#isPermitted(String)}、{@link Subject#isPermittedAll(String...)}方法用于判断用户是否拥有某个
     * 权限或所有权限
     *
     * @expected 登录用户具有指定的权限
     * @author shiloh
     * @date 2022/2/10 15:19
     */
    @Test
    public void testIsPermitted() {
        // login
        final String username = "zhang";
        final String password = "123";
        super.login("classpath:shiro/config/shiro-permissions.ini", username, password);

        final Subject subject = SecurityUtils.getSubject();
        // 判断用户具有权限：user:create
        assertTrue(subject.isPermitted("user:create"));
        // 判断用户具有权限：user:update 和 user:delete
        assertTrue(subject.isPermittedAll("user:update", "user:delete"));
        // 判断用户不具有权限：user:view
        assertFalse(subject.isPermitted("user:view"));
    }

    /**
     * 测试检查用户权限
     * <p>
     * Shiro提供了{@link Subject#checkPermission(String)}、{@link Subject#checkPermissions(String...)}方法用于检查用户是否
     * 具有指定的一个/多个权限，检查不通过会抛出{@link org.apache.shiro.authz.UnauthorizedException}异常
     *
     * @author shiloh
     * @date 2022/2/10 15:26
     */
    @Test(expected = UnauthorizedException.class)
    public void testCheckPermissions() {
        // login
        final String username = "zhang";
        final String password = "123";
        super.login("classpath:shiro/config/shiro-permissions.ini", username, password);

        final Subject subject = SecurityUtils.getSubject();
        // 判断是否拥有权限：user:create
        subject.checkPermission("user:create");
        // 判断是否拥有权限：user:update 和 user:delete
        subject.checkPermissions("user:update", "user:delete");
        // 判断是否拥有权限：user:view，失败则抛出异常
        subject.checkPermission("user:view");
    }
}
