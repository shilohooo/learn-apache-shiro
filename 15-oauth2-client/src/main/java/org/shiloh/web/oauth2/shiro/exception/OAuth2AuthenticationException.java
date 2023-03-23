package org.shiloh.web.oauth2.shiro.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 自定义 OAuth2 身份验证异常
 * @author shiloh
 * @date 2023/3/23 22:59
 */
public class OAuth2AuthenticationException extends AuthenticationException {

    public OAuth2AuthenticationException(Throwable throwable) {
        super(throwable);
    }
}
