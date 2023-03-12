package org.shiloh.web.mvc.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理
 *
 * @author shiloh
 * @date 2023/3/12 12:35
 */
@ControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    /**
     * 密码错误异常处理
     *
     * @author shiloh
     * @date 2023/3/12 12:35
     */
    @ExceptionHandler(IncorrectCredentialsException.class)
    public ModelAndView handleIncorrectCredentialsException(IncorrectCredentialsException e) {
        log.error("密码错误：", e);
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", "用户名/密码错误");
        modelAndView.setViewName("login");
        return modelAndView;
    }

    /**
     * 无权限异常处理：跳转都提示页面
     *
     * @param e 异常对象
     * @author shiloh
     * @date 2023/3/12 14:10
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView handleUnauthorizedException(UnauthorizedException e) {
        log.error("无权限访问：", e);
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", e);
        modelAndView.setViewName("unauthorized");
        return modelAndView;
    }
}
