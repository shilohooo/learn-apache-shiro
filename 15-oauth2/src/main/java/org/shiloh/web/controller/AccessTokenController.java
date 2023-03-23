package org.shiloh.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.shiloh.web.constant.OAuthConstants;
import org.shiloh.web.service.OAuthService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * OAuth2 访问令牌接口
 *
 * @author shiloh
 * @date 2023/3/21 22:41
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class AccessTokenController {
    private final OAuthService oAuthService;

    /**
     * 获取访问令牌
     *
     * @param request 当前请求
     * @return 访问令牌响应数据
     * @author shiloh
     * @date 2023/3/21 22:42
     */
    @RequestMapping("/access-token")
    public HttpEntity<Object> getAccessToken(HttpServletRequest request) throws OAuthSystemException {
        try {
            // 构建OAuth请求
            final OAuthTokenRequest oAuthTokenRequest = new OAuthTokenRequest(request);
            // 检查 clientId 是否存在
            if (!this.oAuthService.checkClientId(oAuthTokenRequest.getClientId())) {
                final OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(OAuthConstants.INVALID_CLIENT_ID_DESC)
                        .buildJSONMessage();
                return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
            }
            // 检查 clientSecret 是否存在
            if (!this.oAuthService.checkClientSecret(oAuthTokenRequest.getClientSecret())) {
                final OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                        .setErrorDescription(OAuthConstants.INVALID_CLIENT_ID_DESC)
                        .buildJSONMessage();
                return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
            }
            // 获取 auth code
            final String authCode = oAuthTokenRequest.getParam(OAuth.OAUTH_CODE);
            // 检查授权类型是否为 code
            if (GrantType.AUTHORIZATION_CODE.toString().equals(oAuthTokenRequest.getParam(OAuth.OAUTH_GRANT_TYPE))) {
                if (!this.oAuthService.checkAuthCode(authCode)) {
                    final OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription("错误的授权码：" + authCode)
                            .buildJSONMessage();
                    return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
                }
            }
            // 生成访问令牌
            final OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
            final String accessToken = oAuthIssuer.accessToken();
            final String username = this.oAuthService.getUsernameByAuthCode(authCode);
            this.oAuthService.addAccessToken(accessToken, username);
            // 生成OAuth响应信息
            final OAuthResponse response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(String.valueOf(this.oAuthService.getExpireIn()))
                    .buildJSONMessage();
            return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
        } catch (OAuthProblemException e) {
            log.error("OAuth2 访问令牌获取出错：", e);
            final OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .error(e)
                    .buildJSONMessage();
            return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
        }
    }
}
