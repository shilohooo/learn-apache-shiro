package org.shiloh.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.Ini;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.env.Environment;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Shiro登录/注销测试用例
 *
 * @author shiloh
 * @date 2022/1/27 17:49
 */
public class LoginTests extends BaseShiroTests {
    /**
     * 登录、注销测试
     * <p>
     * 登录步骤：
     * <ol>
     *     <li>
     *         通过{@link Ini#fromResourcePath(String)}读取classpath下的shiro.ini配置文件，
     *         创建{@link Ini}对象实例，然后将其作为参数创建{@link BasicIniEnvironment#BasicIniEnvironment(Ini)}对象实例，
     *         再通过{@link Environment#getSecurityManager()}获取{@link SecurityManager}对象实例
     *     </li>
     *     <li>
     *         将获取到的{@link SecurityManager}对象实例绑定到{@link SecurityUtils#setSecurityManager(SecurityManager)}，
     *         这是一个全局设置，只需要设置一次即可。
     *     </li>
     *     <li>
     *         通过{@link SecurityUtils#getSubject()}得到{@link Subject}对象实例，其会自动绑定到当前线程。如果当前环境为Web环境，
     *         在请求结束时需要解除绑定，然后创建{@link UsernamePasswordToken#UsernamePasswordToken(String, String)}token，
     *         用于身份验证。
     *     </li>
     *     <li>
     *         调用{@link Subject#login(AuthenticationToken)}方法，将token作为参数进行登录，
     *         其会自动委托给{@link SecurityManager#login(Subject, AuthenticationToken)}方法进行登录
     *     </li>
     *     <li>
     *         当身份验证失败时，可以通过捕获{@link org.apache.shiro.authc.AuthenticationException}或其子类异常从而得知身份验证失
     *         败的原因，常见的如：
     *         <ul>
     *             <li>
     *                 帐号已被禁用：{@link org.apache.shiro.authc.DisabledAccountException}
     *             </li>
     *             <li>
     *                 帐号已被锁定：{@link org.apache.shiro.authc.LockedAccountException}
     *             </li>
     *             <li>
     *                 未知帐号：{@link UnknownAccountException}
     *             </li>
     *             <li>
     *                 登录失败次数超过设定阈值：{@link org.apache.shiro.authc.ExcessiveAttemptsException}
     *             </li>
     *             <li>
     *                 错误的凭证：{@link IncorrectCredentialsException}，即代表密码错误
     *             </li>
     *             <li>
     *                 凭证过期：{@link org.apache.shiro.authc.ExpiredCredentialsException}
     *             </li>
     *         </ul>
     *         具体可以查看异常的继承关系。
     *     </li>
     * </ol>
     * <p>
     * 注销步骤：
     * <ol>
     *     <li>
     *         调用{@link Subject#logout()}方法退出登录，其会自动委托给{@link SecurityManager#logout(Subject)}方法退出。
     *     </li>
     * </ol>
     *
     * @excepted 成功登录、注销
     * @author shiloh
     * @date 2022/1/27 17:50
     */
    @Test
    public void loginAndLogoutTest() {
        // 1.获取SecurityManager
        final Ini ini = Ini.fromResourcePath("classpath:shiro.ini");
        final SecurityManager securityManager = new BasicIniEnvironment(ini).getSecurityManager();
        // 将SecurityManager绑定到SecurityUtils
        SecurityUtils.setSecurityManager(securityManager);
        // 获取Subject用于创建基于帐号密码身份验证的token对象
        final Subject subject = SecurityUtils.getSubject();
        // 用户信息，对应是shiro.ini的[users]配置
        final String username = "zhang";
        final String password = "123";
        final UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            // 登录，即身份验证
            subject.login(token);
            LOG.info("用户【{}】登录成功", username);
        } catch (UnknownAccountException e) {
            // 未知帐号
            LOG.error("未知用户【{}】", username);
        } catch (IncorrectCredentialsException e) {
            // 凭证（密码）不正确
            LOG.error("用户名/密码错误");
        }

        // 断言校验：用户是否已登录
        assertTrue(subject.isAuthenticated());

        // 注销
        subject.logout();
        LOG.info("用户【{}】已退出登录", username);
    }

    /**
     * 使用自定义的{@link org.apache.shiro.realm.Realm}实现测试登录操作
     *
     * @excepted 登录成功
     * @author shiloh
     * @date 2022/1/28 10:00
     */
    @Test
    public void testLoginWithCustomShiroRealm() {
        // 读取classpath中的shiro-realm.ini配置文件
        final Ini ini = Ini.fromResourcePath("classpath:shiro-realm.ini");
        // 指定当前环境为ini环境
        final Environment environment = new BasicIniEnvironment(ini);
        // 获取security manager
        final SecurityManager securityManager = environment.getSecurityManager();
        // 将security manager绑定到security utils中
        SecurityUtils.setSecurityManager(securityManager);
        // 获取 subject
        final Subject subject = SecurityUtils.getSubject();
        // 创建包含身份验证信息的token，准备登录
        final String username = "zhang";
        final String password = "123";
        final UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            // 登录
            subject.login(token);
            LOG.info("用户【{}】登录成功", username);
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            e.printStackTrace();
        }

        // 断言判断用户是否登录成功
        assertTrue(subject.isAuthenticated());

        // 注销
        subject.logout();
        LOG.info("用户【{}】已退出", username);
    }

    /**
     * 测试使用多个{@link org.apache.shiro.realm.Realm}进行登录认证
     *
     * @excepted 登录认证成功
     * @author shiloh
     * @date 2022/1/28 10:22
     */
    @Test
    public void testLoginWithMultiRealm() {
        final Ini ini = Ini.fromResourcePath("classpath:shiro-multi-realm.ini");
        final SecurityManager securityManager = new BasicIniEnvironment(ini).getSecurityManager();
        SecurityUtils.setSecurityManager(securityManager);

        final Subject subject = SecurityUtils.getSubject();
        final String username = "zhang";
        final String password = "123";
        final UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            // login
            subject.login(token);
        } catch (UnknownAccountException | IncorrectCredentialsException exception) {
            exception.printStackTrace();
        }

        // assert
        assertTrue(subject.isAuthenticated());
        LOG.info("用户【{}】登录成功", username);

        // logout
        subject.logout();
        LOG.info("用户【{}】已退出", username);
    }

    /**
     * 使用{@link org.apache.shiro.realm.jdbc.JdbcRealm}进行登录测试
     *
     * @excepted 登录成功
     * @author shiloh
     * @date 2022/1/28 14:35
     */
    @Test
    public void testLoginWithJdbcRealm() {
        // 获取security manager
        final Ini ini = Ini.fromResourcePath("classpath:shiro-jdbc-realm.ini");
        final SecurityManager securityManager = new BasicIniEnvironment(ini).getSecurityManager();
        // 将security manager 绑定到 security utils
        SecurityUtils.setSecurityManager(securityManager);
        // 获取subject
        final Subject subject = SecurityUtils.getSubject();
        // 创建token，用于登录认证
        final String username = "shiloh";
        final String password = "123456";
        final UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            // login
            subject.login(token);
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            e.printStackTrace();
        }

        assertTrue(subject.isAuthenticated());
        LOG.info("用户【{}】登录成功", username);

        // logout
        subject.logout();
        LOG.info("用户【{}】已退出登录", username);
    }
}
