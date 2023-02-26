package org.shiloh.shiro.authc.strategy;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.pam.AbstractAuthenticationStrategy;
import org.apache.shiro.realm.Realm;

import java.util.Collection;
import java.util.Objects;

/**
 * 自定义认证逻辑：只能有一个Realm身份验证通过才算认证成功
 *
 * @author shiloh
 * @date 2022/1/28 15:27
 */
public class OnlyOneAuthenticatorStrategy extends AbstractAuthenticationStrategy {
    /**
     * 此方法在所有Realm调用之前调用
     *
     * @author shiloh
     * @date 2022/1/28 15:28
     */
    @Override
    public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, AuthenticationToken token)
            throws AuthenticationException {
        return new SimpleAuthenticationInfo();
    }


    /**
     * 此方法在每个Realm调用之前调用
     *
     * @author shiloh
     * @date 2022/1/28 15:28
     */
    @Override
    public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo aggregate)
            throws AuthenticationException {
        return aggregate;
    }


    /**
     * 此方法在所有Realm调用之前调用
     *
     * @author shiloh
     * @date 2022/1/28 15:28
     */
    @Override
    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo,
                                           AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
        AuthenticationInfo authenticationInfo;
        if (Objects.isNull(singleRealmInfo)) {
            authenticationInfo = aggregateInfo;
        } else {
            if (Objects.isNull(aggregateInfo)) {
                authenticationInfo = singleRealmInfo;
            } else {
                authenticationInfo = merge(singleRealmInfo, aggregateInfo);
                if (aggregateInfo.getPrincipals().getRealmNames().size() > 1) {
                    throw new AuthenticationException("无法验证类型为" + token.getClass()
                            + "的令牌，请确保始终只有一个已配置的Realm支持验证类型为" + token.getClass() + "的令牌");
                }
            }
        }

        return aggregateInfo;
    }

    /**
     * 此方法在每个Realm调用之后调用
     * @author shiloh
     * @date 2022/1/28 15:31
     */
    @Override
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token, AuthenticationInfo aggregate)
            throws AuthenticationException {
        return aggregate;
    }
}
