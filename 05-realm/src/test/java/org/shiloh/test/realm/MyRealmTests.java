package org.shiloh.test.realm;

import org.junit.Test;
import org.shiloh.test.base.BaseTests;

/**
 * 自定义 Shiro Realm 单元测试
 *
 * @author shiloh
 * @date 2023/3/1 17:42
 */
public class MyRealmTests extends BaseTests {
    /**
     * 登录测试
     *
     * @author shiloh
     * @date 2023/3/1 17:43
     */
    @Test
    public void testLogin() {
        // shiloh
        login("classpath:shiro/shiro.ini", user1.getUsername(), user1.getPassword());
        // tom
        login("classpath:shiro/shiro.ini", user2.getUsername(), user2.getPassword());
        // bruce
        login("classpath:shiro/shiro.ini", user3.getUsername(), user3.getPassword());
    }
}
