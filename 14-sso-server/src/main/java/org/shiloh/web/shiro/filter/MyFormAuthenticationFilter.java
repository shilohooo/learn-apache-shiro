package org.shiloh.web.shiro.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义基于表单的身份验证过滤器
 *
 * @author shiloh
 * @date 2023/3/12 12:55
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter {
    /**
     * 重写登录失败处理，根据异常类型提示用户不同的错误信息
     *
     * @param token    token
     * @param e        异常信息
     * @param request  当前请求对象
     * @param response 当前响应对象
     * @author shiloh
     * @date 2023/3/12 13:37
     */
    @Override
    protected boolean onLoginFailure(
            AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response
    ) {
        String errMsg = null;
        if (e instanceof IncorrectCredentialsException) {
            errMsg = "用户名密码错误";
        } else if (e instanceof UnknownAccountException) {
            errMsg = "用户名不存在";
        } else if (e instanceof LockedAccountException) {
            errMsg = "帐号已被锁定";
        }
        if (StringUtils.isNoneBlank(errMsg)) {
            request.setAttribute(this.getFailureKeyAttribute(), errMsg);
        }
        return true;
    }
}
