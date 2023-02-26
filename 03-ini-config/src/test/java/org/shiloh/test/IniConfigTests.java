package org.shiloh.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.config.Ini;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * Shiro ini 配置测试
 *
 * @author shiloh
 * @date 2023/2/26 18:44
 */
public class IniConfigTests {
    /**
     * 纯Java代码写法的 ini 配置测试
     *
     * @author shiloh
     * @date 2023/2/26 18:44
     */
    @Test
    public void testIniConfigUsingPureJavaCode() {
        final DefaultSecurityManager securityManager = new DefaultSecurityManager();
        // 设置认证器
        final ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        // 使用认证策略：至少需要有一个 Realm 认证成功才算认证成功
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        securityManager.setAuthenticator(authenticator);
        // 设置 Realm
        final DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:13306/learn_shiro?useUnicode=true&useSSL=false&characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        final JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        // 允许查看权限信息
        jdbcRealm.setPermissionsLookupEnabled(true);
        securityManager.setRealms(Collections.singleton(jdbcRealm));
        // 将 securityManager 设置到 SecurityUtils，方便全局使用
        SecurityUtils.setSecurityManager(securityManager);
        // 登录
        final Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken("shiloh", "123456"));
        Assert.assertTrue(subject.isAuthenticated());
    }

    /**
     * 使用 ini 文件配置的测试
     *
     * @author shiloh
     * @date 2023/2/26 18:54
     */
    @Test
    public void testIniConfig() {
        final Ini ini = Ini.fromResourcePath("classpath:shiro/config/shiro-config.ini");
        final SecurityManager securityManager = new BasicIniEnvironment(ini).getSecurityManager();
        SecurityUtils.setSecurityManager(securityManager);
        final Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken("shiloh", "123456"));
        Assert.assertTrue(subject.isAuthenticated());
    }
}
