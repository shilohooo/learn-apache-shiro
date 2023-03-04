package org.shiloh.web.servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 基于表单的拦截器身份验证
 *
 * @author shiloh
 * @date 2023/3/4 13:14
 */
@WebServlet(name = "FormFilterLoginServlet", value = "/form-filter-login")
public class FormFilterLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String errClassName = (String) request.getAttribute("shiroLoginFailure");
        System.out.println("errClassName = " + errClassName);
        String errMsg = null;
        if (UnknownAccountException.class.getName().equals(errClassName)) {
            errMsg = "用户不存在";
        } else if (IncorrectCredentialsException.class.getName().equals(errClassName)) {
            errMsg = "用户名/密码错误";
        } else if (StringUtils.isNoneBlank(errClassName)) {
            errMsg = "未知错误：" + errClassName;
        }
        if (StringUtils.isNoneBlank(errMsg)) {
            request.setAttribute("errMsg", errMsg);
        }

        request.getRequestDispatcher("form-filter-login.jsp")
                .forward(request, response);
    }
}
