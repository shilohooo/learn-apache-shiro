package org.shiloh.test.base;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.Ini;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 测试基类
 *
 * @author shiloh
 * @date 2022/1/28 15:48
 */
public class BaseTests {
    protected final Logger LOG = getLogger(BaseTests.class);


    /**
     * 统一登录逻辑
     *
     * @param iniFilePath ini配置文件位置
     * @param username    登录帐号
     * @param password    密码
     * @author shiloh
     * @date 2022/1/28 15:49
     */
    protected void login(String iniFilePath, String username, String password) {
        // 获取ini配置文件
        final Ini ini = Ini.fromResourcePath(iniFilePath);
        // 获取security manager
        final SecurityManager securityManager = new BasicIniEnvironment(ini).getSecurityManager();
        // 将security manager绑定到security utils
        SecurityUtils.setSecurityManager(securityManager);
        // 获取subject
        final Subject subject = SecurityUtils.getSubject();
        // 创建用户名、密码用于身份验证
        final UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        // login
        subject.login(token);

        if (subject.isAuthenticated()) {
            LOG.info("用户【{}】登录成功啦:)", username);
        }
    }

    /**
     * 获取当前线程中的{@link Subject}对象实例
     *
     * @return {@link Subject}
     * @author shiloh
     * @date 2022/1/28 15:54
     */
    protected Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 测试结束后将Subject解绑，避免影响下一次测试
     *
     * @author shiloh
     * @date 2022/1/28 15:48
     */
    @After
    public void unbindSubject() {
        // logout
        SecurityUtils.getSubject().logout();
        LOG.info("解除绑定当前线程中的subject信息");
        try {
            ThreadContext.unbindSubject();
        } catch (Exception ignored) {
        }
    }
}
