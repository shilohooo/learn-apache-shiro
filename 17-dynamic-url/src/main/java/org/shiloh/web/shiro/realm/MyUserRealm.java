package org.shiloh.web.shiro.realm;

import lombok.Setter;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.shiloh.web.entity.SysUser;
import org.shiloh.web.service.SysUserService;

import java.util.Set;

/**
 * Shiro Realm
 *
 * @author shiloh
 * @date 2023/4/10 22:33
 */
@Setter
public class MyUserRealm extends AuthorizingRealm {
    private SysUserService sysUserService;

    /**
     * 获取用户授权信息
     *
     * @param principals 用户主体信息
     * @return 用户授权信息
     * @author shiloh
     * @date 2023/4/10 22:34
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        final String username = (String) principals.getPrimaryPrincipal();
        final SysUser sysUser = this.sysUserService.getByUsername(username);
        final SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 设置权限信息
        final Set<String> permissions = this.sysUserService.getPermissions(sysUser.getId());
        authorizationInfo.setStringPermissions(permissions);
        // 设置角色信息
        final Set<String> roles = this.sysUserService.getRoles(sysUser.getId());
        authorizationInfo.setRoles(roles);
        return authorizationInfo;
    }

    /**
     * 获取用户身份验证信息
     *
     * @param token 身份验证 token
     * @return 用户身份验证信息
     * @author shiloh
     * @date 2023/4/10 22:36
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        final String username = (String) token.getPrincipal();
        final SysUser sysUser = this.sysUserService.getByUsername(username);
        // 用户信息校验
        if (sysUser == null) {
            throw new UnknownAccountException("用户名不存在");
        }
        if (!new String((char[]) token.getCredentials()).equals(sysUser.getPassword())) {
            throw new IncorrectCredentialsException("用户名或密码错误");
        }
        if (Boolean.TRUE.equals(sysUser.getLocked())) {
            throw new LockedAccountException("该帐号已被锁定");
        }

        return new SimpleAuthenticationInfo(
                sysUser.getUsername(),
                sysUser.getPassword(),
                this.getName()
        );
    }
}
