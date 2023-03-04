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
 * 角色接口
 *
 * @author shiloh
 * @date 2023/3/4 12:10
 */
@WebServlet(name = "RoleServlet", value = "/role")
public class RoleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Subject subject = SecurityUtils.getSubject();
        subject.checkRole("admin");
        request.setAttribute("subject", subject);
        request.getRequestDispatcher("has-role.jsp").forward(request, response);
    }
}
