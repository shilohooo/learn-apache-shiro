package org.shiloh.web.shiro.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义 shiro web 会话管理
 *
 * @author shiloh
 * @date 2023/3/18 14:13
 */
@Slf4j
public class MyShiroWebSessionManager extends DefaultWebSessionManager {
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        return super.getSessionId(request, response);
    }
}
