package org.shiloh.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Shiro 提供了 {@link PasswordService} 和 {@link org.apache.shiro.authc.credential.CredentialsMatcher} 用于提供加密
 * 密码和密码验证服务
 * <p>
 * {@link PasswordService} 默认实现为：{@link org.apache.shiro.authc.credential.DefaultPasswordService}
 * <p>
 * {@link org.apache.shiro.authc.credential.CredentialsMatcher} 的实现有以下两个：
 * <p>
 * 1.{@link org.apache.shiro.authc.credential.PasswordMatcher}
 * <p>
 * 2.{@link org.apache.shiro.authc.credential.HashedCredentialsMatcher}，只用于密码验证，且可以提供自己的盐，而不是随机生成盐
 *
 * @author shiloh
 * @date 2023/2/27 10:04
 */
public class MyRealm extends AuthorizingRealm {
    /**
     * 为了方便，直接注入一个 passwordService 来加密密码，
     * 实际使用时需要在 Service 层使用 passwordService 加密密码并存到数据库。
     */
    private PasswordService passwordService;

    public void setPasswordService(PasswordService passwordService) {
        this.passwordService = passwordService;
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
        return null;
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
        return new SimpleAuthenticationInfo(
                "shiloh",
                // 将密码通过 passwordService 加密，以便后续进行验证
                this.passwordService.encryptPassword("123456"),
                getName()
        );
    }
}
