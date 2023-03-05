package org.shiloh.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.shiloh.test.base.BaseTests;

import java.io.Serializable;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Shiro 会话管理单元测试
 *
 * @author shiloh
 * @date 2023/3/5 13:52
 */
public class SessionTests extends BaseTests {
    /**
     * Shiro 会话管理
     * <p>
     * Shiro 提供的会话可以用于 JavaSE/JavaEE 环境，不依赖于任何底层容器，可以独立使用，是完整的会话模块。
     *
     * @author shiloh
     * @date 2023/3/5 13:52
     */
    @Test
    public void test() {
        login("classpath:shiro.ini", "shiloh", "123");
        final Subject subject = SecurityUtils.getSubject();
        // 获取当前会话，等价于 Subject.getSession(true)
        // 即如果当前没有创建 session 对象，就会创建一个
        final Session session = subject.getSession();
        assertThat(session).isNotNull();
        // 获取当前会话的唯一标识，默认是一个随机生成的 UUID
        final Serializable sessionId = session.getId();
        assertThat(sessionId).isNotNull();
        // 获取当前 Subject 的主机地址，如果是未知地址则返回 null
        final String sessionHost = session.getHost();
        assertThat(sessionHost).isNull();
        // 获取当前 session 的过期时间，单位：毫秒。默认值：1800000，即半个小时
        final long sessionTimeout = session.getTimeout();
        assertThat(sessionTimeout).isGreaterThan(0L);
        // 设置 session 的过期时间
        // session.setTimeout(3600000L);
        // 虎丘会话的启动时间
        final Date sessionStartTime = session.getStartTimestamp();
        assertThat(sessionStartTime).isNotNull();
        // 获取会话最后访问时间
        final Date lastAccessTime = session.getLastAccessTime();
        assertThat(lastAccessTime).isNotNull();
        // 更新会话最后访问时间
        session.touch();
        // 销毁会话
        // session.stop();
        // 设置会话属性
        session.setAttribute("key", "123");
        // 获取会话属性
        final String attribute = (String) session.getAttribute("key");
        assertThat(attribute).isNotBlank();
        assertThat(attribute).isEqualTo("123");
        // 删除会话属性
        session.removeAttribute("key");
        assertThat(session.getAttribute("key")).isNull();
    }
}
