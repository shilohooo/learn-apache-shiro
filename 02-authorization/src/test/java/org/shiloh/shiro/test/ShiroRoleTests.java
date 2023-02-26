package org.shiloh.shiro.test;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Shiro角色测试用例
 *
 * @author shiloh
 * @date 2022/1/28 15:47
 */
public class ShiroRoleTests extends BaseShiroTests {
    /**
     * 测试用户是否拥有给定角色
     *
     * @expected 用户具有给定角色
     * @author shiloh
     * @date 2022/1/28 15:52
     */
    @Test
    public void testUserHasGivenRoles() {
        // login
        final String username = "zhang";
        final String password = "123";
        super.login("classpath:shiro/config/shiro-role.ini", username, password);
        // 获取subject
        final Subject subject = super.getSubject();
        // 判断当前用户是否具有角色：role1
        assertTrue(subject.hasRole("role1"));
        // 判断当前用户是否拥有角色：role2
        assertTrue(subject.hasRole("role2"));
        // 判断当前用户是否拥有角色：role1、role2，是否不具有角色：role3
        final boolean[] results = subject.hasRoles(List.of("role1", "role2", "role3"));
        assertTrue(results[0]);
        assertTrue(results[1]);
        assertFalse(results[2]);
    }

    /**
     * 测试用户是否有指定的角色，如果没有则抛出{@link org.apache.shiro.authz.UnauthorizedException}异常
     * <p>
     * {@link Subject}对象的{@link Subject#checkRole(String)}/{@link Subject#checkRoles(String...)}方法与
     * {@link Subject#hasRole(String)}/{@link Subject#hasAllRoles(Collection)}方法的区别：
     * <p>
     * {@link Subject#hasRole(String)}/{@link Subject#hasAllRoles(Collection)}方法在判断为{@code false}的情况下不会抛出异常
     *
     * @expected 当用户没有指定角色时抛出指定异常
     * @author shiloh
     * @date 2022/2/10 15:03
     */
    @Test(expected = UnauthorizedException.class)
    public void checkUserRoleTest() {
        // login
        final String username = "zhang";
        final String password = "123";
        super.login("classpath:shiro/config/shiro-role.ini", username, password);
        // 获取subject
        final Subject subject = super.getSubject();
        // 判断用户是否拥有角色：role1
        subject.checkRole("role1");
        // 判断用户是否拥有角色：role1 和 role3，失败则抛出异常
        subject.checkRoles("role1", "role3");
    }
}
