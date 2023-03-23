package org.shiloh.web.oauth2.shiro.token;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * OAuth2 Token
 *
 * @author shiloh
 * @date 2023/3/23 22:28
 */
@Data
@NoArgsConstructor
public class OAuth2Token implements AuthenticationToken {
    /**
     * 授权码
     */
    private String authCode;

    /**
     * 用户信息
     */
    private String principal;

    public OAuth2Token(String authCode) {
        this.authCode = authCode;
    }


    /**
     * Returns the account identity submitted during the authentication process.
     * <p/>
     * <p>Most application authentications are username/password based and have this
     * object represent a username.  If this is the case for your application,
     * take a look at the {@link UsernamePasswordToken UsernamePasswordToken}, as it is probably
     * sufficient for your use.
     * <p/>
     * <p>Ultimately, the object returned is application specific and can represent
     * any account identity (user id, X.509 certificate, etc).
     *
     * @return the account identity submitted during the authentication process.
     * @see UsernamePasswordToken
     */
    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    /**
     * Returns the credentials submitted by the user during the authentication process that verifies
     * the submitted {@link #getPrincipal() account identity}.
     * <p/>
     * <p>Most application authentications are username/password based and have this object
     * represent a submitted password.  If this is the case for your application,
     * take a look at the {@link UsernamePasswordToken UsernamePasswordToken}, as it is probably
     * sufficient for your use.
     * <p/>
     * <p>Ultimately, the credentials Object returned is application specific and can represent
     * any credential mechanism.
     *
     * @return the credential submitted by the user during the authentication process.
     */
    @Override
    public Object getCredentials() {
        return this.authCode;
    }
}
