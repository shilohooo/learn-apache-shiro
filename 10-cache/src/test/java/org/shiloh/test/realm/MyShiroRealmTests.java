package org.shiloh.test.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.junit.Test;
import org.shiloh.shiro.realm.MyShiroRealm;
import org.shiloh.test.base.BaseTests;

/**
 * 自定义 Shiro Realm 单元测试
 *
 * @author shiloh
 * @date 2023/3/1 17:42
 */
public class MyShiroRealmTests extends BaseTests {

    /**
     * 配置文件位置
     */
    private static final String INI_FILE_PATH = "classpath:shiro/shiro.ini";

    /**
     * Subject
     *
     * @author shiloh
     * @date 2023/3/6 22:59
     */
    @Test
    public void testUnbindSubject() {
        this.userService.changePassword(user1.getId(), PASSWORD);
        final RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        final MyShiroRealm myShiroRealm = (MyShiroRealm) securityManager.getRealms()
                .iterator()
                .next();
        myShiroRealm.clearCachedAuthenticationInfo(this.getSubject().getPrincipals());
        super.unbindSubject();
    }

    /**
     * 测试清除已缓存的身份验证信息
     *
     * @author shiloh
     * @date 2023/3/6 22:17
     */
    @Test
    public void testClearCachedAuthenticationInfo() {
        // 登录成功后，会缓存相应的 AuthenticationInfo
        login(INI_FILE_PATH, user1.getUsername(), user1.getPassword());
        this.userService.changePassword(user1.getId(), PASSWORD + "1");
        final RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        final MyShiroRealm myShiroRealm = (MyShiroRealm) securityManager.getRealms()
                .iterator()
                .next();
        // 修改密码后，清空之前缓存的 AuthenticationInfo
        // 否则下次登录还会获取到修改密码之前的 AuthenticationInfo
        myShiroRealm.clearCachedAuthenticationInfo(this.getSubject().getPrincipals());
        this.user1 = this.userService.findById(user1.getId());
        login(INI_FILE_PATH, user1.getUsername(), user1.getPassword());
    }

    /**
     * 测试清除已缓存的授权信息
     *
     * @author shiloh
     * @date 2023/3/6 23:13
     */
    @Test
    public void testClearCachedAuthorizationInfo() {
        // 登录成功后，会缓存 AuthorizationInfo
        login(INI_FILE_PATH, user1.getUsername(), user1.getPassword());
        this.getSubject().checkRole(role1.getRole());
        // 添加用户与角色的关联关系
        this.userService.addRoleRefs(user1.getId(), role2.getId());
        final RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        final MyShiroRealm myShiroRealm = (MyShiroRealm) securityManager.getRealms()
                .iterator()
                .next();
        // 清除已缓存的 AuthorizationInfo
        myShiroRealm.clearCachedAuthorizationInfo(this.getSubject().getPrincipals());
        this.getSubject().checkRole(role2.getRole());
    }

    /**
     * 测试同时清除身份验证缓存和授权缓存
     *
     * @author shiloh
     * @date 2023/3/6 23:18
     */
    @Test
    public void testClearCache() {
        login(INI_FILE_PATH, user1.getUsername(), user1.getPassword());
        this.getSubject().checkRole(role1.getRole());
        // 修改密码
        this.userService.changePassword(user1.getId(), PASSWORD + "1");
        // 添加用户与角色的关联关系
        this.userService.addRoleRefs(user1.getId(), role2.getId());

        final RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        final MyShiroRealm myShiroRealm = (MyShiroRealm) securityManager.getRealms()
                .iterator()
                .next();
        // 清空身份验证缓存和授权缓存
        myShiroRealm.clearCache(this.getSubject().getPrincipals());
        // 重新登录
        this.user1 = this.userService.findById(user1.getId());
        login(INI_FILE_PATH, user1.getUsername(), user1.getPassword());
        // 检查权限
        this.getSubject().checkRole(role2.getRole());
    }
}
