package org.shiloh.web.servlet;

import org.apache.shiro.SecurityUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

/**
 * 未经授权处理
 *
 * @author shiloh
 * @date 2023/3/4 11:56
 */
@WebServlet(name = "UnauthorizedServlet", value = "/unauthorized")
public class UnauthorizedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("subject", SecurityUtils.getSubject());
        request.getRequestDispatcher("unauthorized.jsp").forward(request, response);
    }
}
