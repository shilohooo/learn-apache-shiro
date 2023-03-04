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
 * 权限接口
 *
 * @author shiloh
 * @date 2023/3/4 12:21
 */
@WebServlet(name = "PermissionServlet", value = "/permissions")
public class PermissionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("user:create");
        request.setAttribute("subject", subject);
        request.getRequestDispatcher("has-permission.jsp").forward(request, response);
    }
}
