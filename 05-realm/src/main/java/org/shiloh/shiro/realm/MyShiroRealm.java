package org.shiloh.shiro.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.shiloh.entity.User;
import org.shiloh.service.UserService;
import org.shiloh.service.impl.UserServiceImpl;

/**
 * 自定义 Shiro Realm
 *
 * @author shiloh
 * @date 2023/3/1 17:32
 */
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {
    /* ============================= INSTANCE FIELDS ============================== */
    private final UserService userService;

    public MyShiroRealm() {
        super();
        this.userService = new UserServiceImpl();
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
        // 根据用户名获取用户关联的角色和权限，并设置到 Shiro 的权限信息中
        final String username = (String) principals.getPrimaryPrincipal();
        final SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(this.userService.findRolesByUsername(username));
        authorizationInfo.setStringPermissions(this.userService.findPermissionsByUsername(username));

        return authorizationInfo;
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
        // 根据用户名查询用户信息
        final String username = (String) token.getPrincipal();
        final User user = this.userService.findByUsername(username);
        if (user == null) {
            // 帐号不存在
            throw new UnknownAccountException();
        }
        if (Boolean.TRUE.equals(user.getLocked())) {
            // 帐号已被锁定
            throw new LockedAccountException();
        }
        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以在此判断或自定义实现
        return new SimpleAuthenticationInfo(
                // 帐号
                user.getUsername(),
                // 密码
                user.getPassword(),
                // salt = username + salt
                // CredentialsMatcher 使用盐加密传入的明文密码和此处的密文密码进行匹配
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                // realm name
                getName()
        );
    }
}
