package org.shiloh.shiro.config.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 自定义的{@link Realm}实现类
 * <p>
 * {@link Realm}的作用：
 * <ol>
 *     <li>
 *         提供安全数据（如用户信息、权限、角色）给{@link org.apache.shiro.mgt.SecurityManager}获取，可以看成是一个安全数据源
 *     </li>
 * </ol>
 *
 * @author shiloh
 * @date 2022/1/28 9:40
 */
public class CustomShiroRealm3 implements Realm {
    private final Logger LOG = getLogger(CustomShiroRealm3.class);

    private static final String USERNAME_IN_INI_CONFIG = "zhang";
    private static final String PASSWORD_IN_INI_CONFIG = "123";

    /**
     * 获取{@link CustomShiroRealm3}在应用中的唯一名称
     *
     * @return {@link CustomShiroRealm3}在应用中的唯一名称
     * @author shiloh
     * @date 2022/1/28 9:43
     */
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    /**
     * 如果该{@link CustomShiroRealm3}支持传入的{@link AuthenticationToken}则返回{@code true}，否则返回{@code false}
     * <p>
     * 返回{@code true}则说明：该{@link CustomShiroRealm3}可以对传入的{@link AuthenticationToken}进行身份验证
     * <p>
     * 这里设置为支持类型为{@link UsernamePasswordToken}的token
     *
     * @return 该 {@link CustomShiroRealm3} 是否支持传入的{@link AuthenticationToken}，
     * {@code true} = 支持，{@code false} = 不支持
     * @author shiloh
     * @date 2022/1/28 9:45
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    /**
     * 获取传入的{@link AuthenticationToken}所对应帐号的身份验证信息，如果没有对应的则返回{@code null}
     *
     * @param token 应用中用于身份验证的token
     * @return token对应帐号的身份验证信息
     * @throws AuthenticationException 若获取身份验证信息出错则抛出此异常
     * @author shiloh
     * @date 2022/1/28 9:51
     */
    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        LOG.info("{}开始身份验证", getName());

        // 从token中获取用户名、密码
        final String username = (String) token.getPrincipal();
        final String password = new String(((char[]) token.getCredentials()));
        // 帐号密码校验
        if (!USERNAME_IN_INI_CONFIG.equals(username)) {
            // 未知帐号
            throw new UnknownAccountException("用户名不存在:(");
        }
        if (!PASSWORD_IN_INI_CONFIG.equals(password)) {
            // 密码错误
            throw new IncorrectCredentialsException("用户名/密码错误:(");
        }

        LOG.info("{}身份验证结束", getName());

        // 身份认证成功，返回一个身份验证信息（AuthenticationInfo实例）
        return new SimpleAuthenticationInfo(
                // 帐号
                username + "@gmail.com",
                // 密码
                password,
                // realm 的唯一名称
                getName()
        );
    }
}
