package org.shiloh.web.oauth2.shiro.realm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.shiloh.web.oauth2.shiro.exception.OAuth2AuthenticationException;
import org.shiloh.web.oauth2.shiro.token.OAuth2Token;

import static org.apache.oltu.oauth2.common.OAuth.HttpMethod.GET;
import static org.apache.oltu.oauth2.common.OAuth.HttpMethod.POST;

/**
 * OAuth2 Realm
 *
 * @author shiloh
 * @date 2023/3/23 22:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class OAuth2Realm extends AuthorizingRealm {
    /**
     * 客户端 ID
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * accessToken获取地址
     */
    private String accessTokenUrl;

    /**
     * 用户信息获取地址
     */
    private String userInfoUrl;

    /**
     * 重定向地址
     */
    private String redirectUrl;

    /**
     * Convenience implementation that returns
     * <tt>getAuthenticationTokenClass().isAssignableFrom( token.getClass() );</tt>.  Can be overridden
     * by subclasses for more complex token checking.
     * <p>Most configurations will only need to set a different class via
     * {@link #setAuthenticationTokenClass}, as opposed to overriding this method.
     *
     * @param token the token being submitted for authentication.
     * @return true if this authentication realm can process the submitted token instance of the class, false otherwise.
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        // 仅支持验证 OAuth2Token
        return token instanceof OAuth2Token;
    }

    /**
     * Retrieves the AuthorizationInfo for the given principals from the underlying data store.  When returning
     * an instance from this method, you might want to consider using an instance of
     * {@link SimpleAuthorizationInfo SimpleAuthorizationInfo}, as it is suitable in most cases.
     *
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return the AuthorizationInfo associated with this principals.
     * @see SimpleAuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 暂时不需要权限
        return new SimpleAuthorizationInfo();
    }

    /**
     * Retrieves authentication data from an implementation-specific datasource (RDBMS, LDAP, etc) for the given
     * authentication token.
     * <p/>
     * For most datasources, this means just 'pulling' authentication data for an associated subject/user and nothing
     * more and letting Shiro do the rest.  But in some systems, this method could actually perform EIS specific
     * log-in logic in addition to just retrieving data - it is up to the Realm implementation.
     * <p/>
     * A {@code null} return value means that no account could be associated with the specified token.
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return an {@link AuthenticationInfo} object containing account data resulting from the
     * authentication ONLY if the lookup is successful (i.e. account exists and is valid, etc.)
     * @throws AuthenticationException if there is an error acquiring data or performing
     *                                 realm-specific authentication logic for the specified <tt>token</tt>
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        final OAuth2Token oAuth2Token = (OAuth2Token) token;
        final String authCode = oAuth2Token.getAuthCode();
        final String username = this.extractUsername(authCode);
        log.info("OAuth2 Realm 身份验证成功，根据 code：{} 获取到的用户信息为：{}", authCode, username);
        // 返回身份验证信息
        return new SimpleAuthenticationInfo(username, authCode, this.getName());
    }

    /**
     * 根据授权码获取用户信息
     *
     * @param authCode 授权码
     * @return 用户名
     * @author shiloh
     * @date 2023/3/23 22:57
     */
    private String extractUsername(String authCode) {
        try {
            // 构建客户端
            final OAuthClient client = new OAuthClient(new URLConnectionClient());
            // 构建客户端请求
            final OAuthClientRequest tokenReq = OAuthClientRequest.tokenLocation(this.accessTokenUrl)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(this.clientId)
                    .setClientSecret(this.clientSecret)
                    .setCode(authCode)
                    .setRedirectURI(this.redirectUrl)
                    .buildQueryMessage();
            // 获取 token
            final OAuthJSONAccessTokenResponse tokenResp = client.accessToken(tokenReq, POST);
            final String accessToken = tokenResp.getAccessToken();
            log.info("OAuth2 身份验证 access token 获取成功，获取到的 access token 为：{}", accessToken);
            final Long expiresIn = tokenResp.getExpiresIn();
            // 根据 token 获取用户信息
            final OAuthClientRequest userInfoReq = new OAuthBearerClientRequest(this.userInfoUrl)
                    .setAccessToken(accessToken)
                    .buildQueryMessage();
            final OAuthResourceResponse userInfoResp = client.resource(userInfoReq, GET, OAuthResourceResponse.class);
            log.info("OAuth2 用户信息获取成功，获取到的用户信息为：{}", userInfoResp.getBody());
            return userInfoResp.getBody();
        } catch (Exception e) {
            log.error("OAuth2 Realm 身份验证出错，code：{}，异常信息：", authCode, e);
            throw new OAuth2AuthenticationException(e);
        }
    }
}
