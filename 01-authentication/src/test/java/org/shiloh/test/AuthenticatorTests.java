package org.shiloh.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.Ini;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

/**
 * 不同策略的{@link org.apache.shiro.authc.Authenticator}登录认证测试用例
 *
 * @author shiloh
 * @date 2022/1/28 14:53
 */
public class AuthenticatorTests extends BaseShiroTests {

    /**
     * 测试所有Realm都认证成功才算身份验证成功的逻辑
     *
     * @excepted 所有Realm的身份验证操作都通过
     * @author shiloh
     * @date 2022/1/28 14:59
     */
    @Test
    public void testAllSuccessfulStrategyWithSuccess() {
        final String configFilePath = "classpath:shiro-authenticator-all-success.ini";
        login(configFilePath);
        // 获取subject
        final Subject subject = SecurityUtils.getSubject();
        // 得到一个身份集合，其包含了Realm验证成功的身份信息
        final PrincipalCollection principalCollection = subject.getPrincipals();
        // 断言：principalCollection包含了zhang和zhang@gmail.com的身份信息
        Assert.assertEquals(2, principalCollection.asList().size());
    }

    /**
     * 测试所有Realm都认证成功才算身份验证成功的逻辑
     *
     * @excepted 其中有一个Realm验证失败并抛出异常
     * @author shiloh
     * @date 2022/1/28 14:59
     */
    @Test(expected = UnknownAccountException.class)
    public void testAllSuccessfulStrategyWithFail() {
        final String configFilePath = "classpath:shiro-authenticator-all-fail.ini";
        login(configFilePath);
        // 获取subject
        final Subject subject = SecurityUtils.getSubject();
    }

    /**
     * 测试只要有一个Realm认证成功就算身份验证成功的逻辑
     *
     * @excepted 身份验证操作通过且返回所有Realm身份验证通过的认证信息
     * @author shiloh
     * @date 2022/1/28 14:59
     */
    @Test
    public void testAtLeastOneSuccessfulStrategyWithSuccess() {
        final String configFilePath = "classpath:shiro-authenticator-at-least-one-success.ini";
        login(configFilePath);
        // 获取subject
        final Subject subject = SecurityUtils.getSubject();
        // 得到一个身份集合，其包含了Realm验证成功的身份信息
        final PrincipalCollection principalCollection = subject.getPrincipals();
        // 断言：principalCollection包含了zhang和zhang@gmail.com的身份信息
        Assert.assertEquals(2, principalCollection.asList().size());
    }

    /**
     * 测试只要有一个Realm认证成功就算身份验证成功的逻辑
     *
     * @excepted 身份验证操作通过且只返回第一个身份验证通过的Realm包含的认证信息
     * @author shiloh
     * @date 2022/1/28 14:59
     */
    @Test
    public void testFirstOneSuccessfulStrategyWithSuccess() {
        final String configFilePath = "classpath:shiro-authenticator-first-success.ini";
        login(configFilePath);
        // 获取subject
        final Subject subject = SecurityUtils.getSubject();
        // 得到一个身份集合，其包含了Realm验证成功的身份信息
        final PrincipalCollection principalCollection = subject.getPrincipals();
        // 断言：principalCollection包含了zhang和zhang@gmail.com的身份信息
        Assert.assertEquals(1, principalCollection.asList().size());
    }

    /**
     * 测试至少有2个Realm认证成功就算身份验证成功的逻辑
     *
     * @excepted 至少有2个Realm身份验证操作通过
     * @author shiloh
     * @date 2022/1/28 14:59
     */
    @Test
    public void testAtLeastTwoSuccessfulStrategyWithSuccess() {
        final String configFilePath = "classpath:shiro-authenticator-at-least-two-success.ini";
        login(configFilePath);
        // 获取subject
        final Subject subject = SecurityUtils.getSubject();
        // 得到一个身份集合，其包含了Realm验证成功的身份信息
        // 因为customShiroRealm和customShiroRealm4返回的认证信息一致，所以principalCollection只包含一个
        final PrincipalCollection principalCollection = subject.getPrincipals();
        // 断言：principalCollection包含了zhang的身份信息
        Assert.assertEquals(1, principalCollection.asList().size());
    }

    /**
     * 测试至少有2个Realm认证成功就算身份验证成功的逻辑
     *
     * @excepted 至少有2个Realm身份验证操作通过
     * @author shiloh
     * @date 2022/1/28 14:59
     */
    @Test
    public void testOnlyOneSuccessfulStrategyWithSuccess() {
        final String configFilePath = "classpath:shiro-authenticator-only-one-success.ini";
        login(configFilePath);
        // 获取subject
        final Subject subject = SecurityUtils.getSubject();
        // 得到一个身份集合，其包含了Realm验证成功的身份信息
        final PrincipalCollection principalCollection = subject.getPrincipals();
        // 断言：principalCollection仅包含了1个身份信息
        Assert.assertEquals(1, principalCollection.asList().size());
    }

    /**
     * 通用登录逻辑
     *
     * @param configFile ini配置文件路径
     * @author shiloh
     * @date 2022/1/28 14:56
     */
    private void login(String configFile) {
        // 基于ini环境获取security manager
        final Ini ini = Ini.fromResourcePath(configFile);
        final SecurityManager securityManager = new BasicIniEnvironment(ini).getSecurityManager();
        // 将security manager绑定到security utils
        SecurityUtils.setSecurityManager(securityManager);
        // 获取subject
        final Subject subject = SecurityUtils.getSubject();
        // 创建用户名/密码进行身份验证
        final String username = "zhang";
        final String password = "123";
        final UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // login
        subject.login(token);
    }
}
