package org.shiloh.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.shiloh.web.constant.OAuthConstants;
import org.shiloh.web.service.OAuthService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户信息接口
 *
 * @author shiloh
 * @date 2023/3/21 22:52
 */
@RestController
@RequestMapping("/user-info")
@RequiredArgsConstructor
@Slf4j
public class UserInfoController {
    private final OAuthService oAuthService;

    /**
     * 获取用户信息
     *
     * @param request 当前请求
     * @return 用户信息
     * @author shiloh
     * @date 2023/3/21 22:54
     */
    @RequestMapping
    public HttpEntity<Object> getUserInfo(HttpServletRequest request) throws OAuthSystemException {
        // 构建 OAuth 资源请求
        try {
            final OAuthAccessResourceRequest oauthReq = new OAuthAccessResourceRequest(
                    request, ParameterStyle.QUERY
            );
            // 获取访问令牌
            final String accessToken = oauthReq.getAccessToken();
            // 检查访问令牌是否有效
            if (!this.oAuthService.checkAccessToken(accessToken)) {
                // 令牌不存在或过期了，返回未验证错误，需要重新验证
                final OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setRealm(OAuthConstants.RESOURCE_SERVER_NAME)
                        .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                        .buildHeaderMessage();
                final HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(OAuth.HeaderType.WWW_AUTHENTICATE, response.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
                return new ResponseEntity<>(httpHeaders, HttpStatus.UNAUTHORIZED);
            }
            // 返回用户名
            final String username = this.oAuthService.getUsernameByAccessToken(accessToken);
            return new ResponseEntity<>(username, HttpStatus.OK);
        } catch (OAuthProblemException e) {
            log.error("获取用户信息出错：", e);
            // 检查是否设置了错误码
            final String errCode = e.getError();
            if (OAuthUtils.isEmpty(errCode)) {
                final OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setRealm(OAuthConstants.RESOURCE_SERVER_NAME)
                        .buildHeaderMessage();
                final HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(OAuth.HeaderType.WWW_AUTHENTICATE, response.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
                return new ResponseEntity<>(httpHeaders, HttpStatus.UNAUTHORIZED);
            }
            final OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .setRealm(OAuthConstants.RESOURCE_SERVER_NAME)
                    .setError(e.getError())
                    .setErrorDescription(e.getDescription())
                    .setErrorUri(e.getUri())
                    .buildHeaderMessage();
            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(OAuth.HeaderType.WWW_AUTHENTICATE, response.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
            return new ResponseEntity<>(httpHeaders, HttpStatus.UNAUTHORIZED);
        }
    }
}
