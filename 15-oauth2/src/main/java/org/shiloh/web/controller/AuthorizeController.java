package org.shiloh.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.shiloh.web.constant.OAuthConstants;
import org.shiloh.web.entity.Client;
import org.shiloh.web.service.ClientService;
import org.shiloh.web.service.OAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * OAuth 授权接口
 *
 * @author shiloh
 * @date 2023/3/21 22:15
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthorizeController {
    private final OAuthService oAuthService;
    private final ClientService clientService;

    /**
     * OAuth 授权
     *
     * @param model   model
     * @param request 当前请求
     * @author shiloh
     * @date 2023/3/21 22:15
     */
    @RequestMapping("/authorize")
    public Object authorize(Model model, HttpServletRequest request) throws OAuthSystemException, URISyntaxException {
        try {
            // 构建 OAuth 授权请求
            final OAuthAuthzRequest oAuthAuthzRequest = new OAuthAuthzRequest(request);
            // 检查传入的 clientId 是否存在
            if (!this.oAuthService.checkClientId(oAuthAuthzRequest.getClientId())) {
                final OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(OAuthConstants.INVALID_CLIENT_ID_DESC)
                        .buildJSONMessage();
                return new ResponseEntity<>(
                        response.getBody(), HttpStatus.valueOf(response.getResponseStatus())
                );
            }
            // 判断用户是否已登录
            final Subject subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()) {
                // 尝试登录
                if (!this.login(subject, request)) {
                    // 登录失败时跳转回登录页面
                    log.info("登录失败，跳转回登录页面");
                    final Client client = this.clientService.findByClientId(oAuthAuthzRequest.getClientId());
                    model.addAttribute("client", client);
                    return "oauth2-login";
                }
            }
            // 生成授权码
            final String username = (String) subject.getPrincipal();
            String authCode = null;
            final String responseType = oAuthAuthzRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            log.info("responseType: {}", responseType);
            if (ResponseType.CODE.toString().equals(responseType)) {
                log.info("生成授权码");
                final OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
                authCode = oAuthIssuer.authorizationCode();
                this.oAuthService.addAuthCode(authCode, username);
            }
            log.info("生成的授权码为：{}", authCode);
            // 构建OAuth响应
            final OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(
                    request, HttpServletResponse.SC_FOUND
            );
            // 设置授权码
            builder.setCode(authCode);
            // 获取客户端重定向地址
            final String redirectUri = oAuthAuthzRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            final OAuthResponse response = builder.location(redirectUri).buildQueryMessage();
            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity<>(httpHeaders, HttpStatus.valueOf(response.getResponseStatus()));
        } catch (OAuthProblemException e) {
            log.error("OAuth2授权出错：", e);
            final String redirectUri = e.getRedirectUri();
            if (OAuthUtils.isEmpty(redirectUri)) {
                // 告诉客户端没有传入重定向地址
                return new ResponseEntity<>("OAuth回调地址不能为空！！！", HttpStatus.NOT_FOUND);
            }
            // 返回错误信息（如?error=）
            final OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                    .error(e)
                    .location(redirectUri)
                    .buildQueryMessage();
            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity<>(httpHeaders, HttpStatus.valueOf(response.getResponseStatus()));
        }
    }

    /**
     * 登录操作
     *
     * @param subject 当前主体
     * @param request 当前请求
     * @return 登录成功返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/3/21 22:36
     */
    private Boolean login(Subject subject, HttpServletRequest request) {
        // 不支持 GET 请求授权
        if (HttpMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        // 获取用户名和密码
        final String username = request.getParameter(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        final String password = request.getParameter(FormAuthenticationFilter.DEFAULT_PASSWORD_PARAM);
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return false;
        }

        final UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            log.info("登录成功");
            subject.login(token);
            return true;
        } catch (Exception e) {
            request.setAttribute("error", "登录失败：" + e.getClass().getName());
            return false;
        }
    }
}
