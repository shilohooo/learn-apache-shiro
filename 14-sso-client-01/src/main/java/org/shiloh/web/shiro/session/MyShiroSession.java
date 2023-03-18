package org.shiloh.web.shiro.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.shiro.session.mgt.SimpleSession;

/**
 * 自定义 Shiro Session
 *
 * @author shiloh
 * @date 2023/3/17 17:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyShiroSession extends SimpleSession {
    private static final long serialVersionUID = -479272423695489206L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 带参构造器
     *
     * @param host     用户登录主机
     * @param username 用户名
     * @author shiloh
     * @date 2023/3/17 17:22
     */
    public MyShiroSession(String host, String username) {
        super(host);
        this.username = username;
    }
}
