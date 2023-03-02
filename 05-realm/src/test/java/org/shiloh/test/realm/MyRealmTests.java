package org.shiloh.test.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.junit.Test;
import org.shiloh.test.base.BaseTests;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 自定义 Shiro Realm 单元测试
 *
 * @author shiloh
 * @date 2023/3/1 17:42
 */
public class MyRealmTests extends BaseTests {

    /**
     * 配置文件位置
     */
    private static final String INI_FILE_PATH = "classpath:shiro/shiro.ini";

    /**
     * 登录测试
     *
     * @author shiloh
     * @date 2023/3/1 17:43
     */
    @Test
    public void testLogin() {
        // shiloh
        login(INI_FILE_PATH, user1.getUsername(), user1.getPassword());
        // tom
        login(INI_FILE_PATH, user2.getUsername(), user2.getPassword());
        // bruce
        login(INI_FILE_PATH, user3.getUsername(), user3.getPassword());
    }

    /**
     * 测试使用未知的用户名登录
     *
     * @author shiloh
     * @date 2023/3/2 18:10
     */
    @Test(expected = UnknownAccountException.class)
    public void testLoginFailUsingUnknownUsername() {
        login(INI_FILE_PATH, user1.getUsername() + "123", user1.getPassword());
    }

    /**
     * 测试使用错误的密码登录
     *
     * @author shiloh
     * @date 2023/3/2 18:13
     */
    @Test(expected = IncorrectCredentialsException.class)
    public void testLoginFailUsingIncorrectCredentials() {
        login(INI_FILE_PATH, user1.getUsername(), user1.getPassword() + "1");
    }

    /**
     * 测试使用已被锁定的帐号登录
     *
     * @author shiloh
     * @date 2023/3/2 18:15
     */
    @Test(expected = LockedAccountException.class)
    public void testLoginFailUsingLockedUser() {
        login(INI_FILE_PATH, user4.getUsername(), user4.getPassword());
    }

    /**
     * 测试密码错误重试次数
     *
     * @author shiloh
     * @date 2023/3/2 18:16
     */
    @Test(expected = ExcessiveAttemptsException.class)
    public void testLoginFailWithLimitRetryCount() {
        for (int i = 0; i < 5; i++) {
            try {
                login(INI_FILE_PATH, user1.getUsername(), user1.getPassword() + "1");
            } catch (Exception ignored) {
            }
        }
        login(INI_FILE_PATH, user1.getUsername(), user1.getPassword() + "1");
    }

    /**
     * 测试用户是否拥有指定角色
     *
     * @author shiloh
     * @date 2023/3/2 18:18
     */
    @Test
    public void testUserHasRole() {
        login(INI_FILE_PATH, user1.getUsername(), user1.getPassword());
        assertThat(SecurityUtils.getSubject().hasRole("admin")).isTrue();
    }

    /**
     * 测试用户是否没有指定角色
     *
     * @author shiloh
     * @date 2023/3/2 18:19
     */
    @Test
    public void testUserNoRole() {
        login(INI_FILE_PATH, user2.getUsername(), user2.getPassword());
        assertThat(SecurityUtils.getSubject().hasRole("admin")).isFalse();
    }

    /**
     * 测试用户是否拥有指定权限
     *
     * @author shiloh
     * @date 2023/3/2 18:20
     */
    @Test
    public void testUserHasPermissions() {
        login(INI_FILE_PATH, user1.getUsername(), user1.getPassword());
        assertThat(SecurityUtils.getSubject().isPermittedAll("user:create", "menu:create")).isTrue();
    }

    /**
     * 测试用户是否没有指定权限
     *
     * @author shiloh
     * @date 2023/3/2 18:22
     */
    @Test
    public void testUserNoPermissions() {
        login(INI_FILE_PATH, user2.getUsername(), user2.getPassword());
        assertThat(SecurityUtils.getSubject().isPermittedAll("user:create")).isFalse();
    }
}
