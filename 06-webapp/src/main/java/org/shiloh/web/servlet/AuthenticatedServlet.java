package org.shiloh.web.servlet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 身份验证通过处理
 *
 * @author shiloh
 * @date 2023/3/4 12:04
 */
@WebServlet(name = "AuthenticatedServlet", value = "/authenticated")
public class AuthenticatedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            request.setAttribute("subject", subject);
            request.getRequestDispatcher("authenticated.jsp").forward(request, response);
            return;
        }
        // 身份验证不通过回到登录页面
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
