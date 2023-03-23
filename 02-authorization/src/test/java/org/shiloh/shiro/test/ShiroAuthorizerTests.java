package org.shiloh.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Shiro自定义授权器配置测试用例
 *
 * @author shiloh
 * @date 2022/2/10 16:55
 */
public class ShiroAuthorizerTests extends BaseShiroTests {
    /**
     * 测试用户是否拥有指定的权限
     *
     * @expected 用户拥有指定的权限
     * @author shiloh
     * @date 2022/2/10 16:56
     */
    @Test
    public void testIsPermitted() {
        // login
        final String username = "shiloh";
        final String password = "123456";
        super.login("classpath:shiro/config/shiro-authorizer.ini", username, password);

        final Subject subject = SecurityUtils.getSubject();
        // 判断用户是否拥有：user:create权限
        assertTrue(subject.isPermitted("user1:update"));
        assertTrue(subject.isPermitted("user2:update"));
        // 通过二进制位的方式表示权限
        // 判断用户是否拥有新增用户的权限
        assertTrue(subject.isPermitted("+user1+2"));
        // 判断用户是否拥有查看用户的权限
        assertTrue(subject.isPermitted("+user1+8"));
        // 判断用户是否拥有新增和查看用户的权限
        assertTrue(subject.isPermitted("+user2+10"));
        // 判断用户不具有删除用户的权限
        assertFalse(subject.isPermitted("+user1+4"));
        // 判断用户是否拥有：menu:view权限
        // 该权限通过自定义的角色权限解析器赋予用户
        // 当用户拥有role1角色时，说明用户对menu资源拥有view权限
        assertTrue(subject.isPermitted("menu:view"));
    }

    /**
     * 使用{@link org.apache.shiro.realm.jdbc.JdbcRealm}测试用户是否拥有指定的权限
     *
     * @expected 用户拥有指定的权限
     * @author shiloh
     * @date 2022/2/10 16:56
     */
    @Test
    public void testIsPermittedUsingJdbcRealm() {
        // login
        final String username = "shiloh";
        final String password = "123456";
        super.login("classpath:shiro/config/shiro-jdbc-authorizer.ini", username, password);

        final Subject subject = SecurityUtils.getSubject();
        // 判断用户是否拥有：user:create权限
        assertTrue(subject.isPermitted("user1:update"));
        assertTrue(subject.isPermitted("user2:update"));
        // 通过二进制位的方式表示权限
        // 判断用户是否拥有新增用户的权限
        assertTrue(subject.isPermitted("+user1+2"));
        // 判断用户是否拥有查看用户的权限
        assertTrue(subject.isPermitted("+user1+8"));
        // 判断用户是否拥有新增和查看用户的权限
        assertTrue(subject.isPermitted("+user2+10"));
        // 判断用户不具有删除用户的权限
        assertFalse(subject.isPermitted("+user1+4"));
        // 判断用户是否拥有：menu:view权限
        // 该权限通过自定义的角色权限解析器赋予用户
        // 当用户拥有role1角色时，说明用户对menu资源拥有view权限
        assertTrue(subject.isPermitted("menu:view"));
    }
}
