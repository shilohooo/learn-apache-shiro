package org.shiloh.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录接口
 *
 * @author shiloh
 * @date 2023/3/21 21:51
 */
@Controller
public class LoginController {

    /**
     * 跳转到登录页面
     *
     * @param request 当前请求
     * @param model   model
     * @return 登录页面名称
     * @author shiloh
     * @date 2023/3/21 22:04
     */
    @RequestMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        final String exceptionClassName = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String errMsg = null;
        if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
            errMsg = "用户名不存在";
        } else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
            errMsg = "用户名/密码错误";
        } else if (StringUtils.isNoneBlank(exceptionClassName)) {
            errMsg = "未知错误：" + exceptionClassName;
        }

        model.addAttribute("error", errMsg);
        return "login";

    }
}
