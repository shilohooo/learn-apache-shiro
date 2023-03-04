package org.shiloh.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 表单登录过滤器
 *
 * @author shiloh
 * @date 2023/3/4 23:14
 */
@Slf4j
public class FormLoginFilter extends PathMatchingFilter {
    /**
     * 登录地址
     */
    public static final String LOGIN_URL = "/login.jsp";

    /**
     * 登录成功后跳转的地址
     */
    private static final String SUCCESS_URL = "/";

    /**
     * Http 请求方法：POST
     */
    private static final String HTTP_METHOD_POST = "POST";

    @Override
    protected boolean onPreHandle(ServletRequest req, ServletResponse res, Object mappedValue) throws Exception {
        log.info("FormLoginFilter.onPreHandle");
        if (SecurityUtils.getSubject().isAuthenticated()) {
            // 已登录了
            return true;
        }

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        // 判断是否为登录请求
        if (!this.pathsMatch(LOGIN_URL, WebUtils.getPathWithinApplication(request))) {
            // 如果不是登录请求，则将当前请求保存起来然后重定向到登录页面
            WebUtils.saveRequest(request);
            WebUtils.issueRedirect(request, response, LOGIN_URL);
            return false;
        }
        // 登录请求：请求方法如果不是 POST，则跳转到登录页面
        if (!HTTP_METHOD_POST.equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // 执行登录逻辑
        final boolean isLoginSuccess = this.login(request);
        if (isLoginSuccess) {
            // 跳转到上一次请求的页面或登录成功后跳转的默认页面
            WebUtils.redirectToSavedRequest(request, response, SUCCESS_URL);
            return false;
        }

        // 继续执行过滤器链
        return true;
    }

    /**
     * 登录操作
     *
     * @param request 当前请求
     * @return 如果登录成功则返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/3/4 23:22
     */
    private boolean login(HttpServletRequest request) {
        // 获取登录用户名和密码
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(username, password));
            return true;
        } catch (Exception e) {
            request.setAttribute("shiroLoginFailure", e.getClass());
            return false;
        }
    }
}
