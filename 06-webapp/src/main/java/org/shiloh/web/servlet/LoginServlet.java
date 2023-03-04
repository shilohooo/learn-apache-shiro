package org.shiloh.web.servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录接口
 *
 * @author shiloh
 * @date 2023/3/3 23:54
 */
@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    /**
     * 请求转发到 Login 页面
     *
     * @param request  当前请求对象
     * @param response 当前响应
     * @author shiloh
     * @date 2023/3/3 23:56
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);

    }

    /**
     * 登录
     *
     * @param request  当前请求对象
     * @param response 当前响应
     * @author shiloh
     * @date 2023/3/3 23:56
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("LoginServlet.doPost");
        // 获取用户名、密码
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        // 执行登录操作
        final Subject subject = SecurityUtils.getSubject();
        final UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        String errMsg = null;
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            errMsg = "用户不存在";
        } catch (IncorrectCredentialsException e) {
            errMsg = "用户名/密码错误";
        } catch (LockedAccountException e) {
            errMsg = "该帐号已被锁定";
        } catch (AuthenticationException e) {
            // 其他错误
            errMsg = "其他错误：" + e.getMessage();
        }
        // 验证失败，返回登录页面
        if (StringUtils.isNoneBlank(errMsg)) {
            request.setAttribute("errMsg", errMsg);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        // 登录成功
        request.setAttribute("subject", subject);
        request.getRequestDispatcher("login-success.jsp").forward(request, response);
    }
}
