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
 * 自定义认证策略：至少有2个Realm身份认证通过才算认证成功
 *
 * @author shiloh
 * @date 2022/1/28 15:18
 */
public class AtLeastTowAuthenticatorStrategy extends AbstractAuthenticationStrategy {
    /**
     * 此方法在所有Realm调用之前调用
     *
     * @author shiloh
     * @date 2022/1/28 15:19
     */
    @Override
    public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, AuthenticationToken token)
            throws AuthenticationException {
        // 返回一个认证信息
        return new SimpleAuthenticationInfo();
    }

    /**
     * 此方法在每个Realm调用之前调用
     *
     * @author shiloh
     * @date 2022/1/28 15:20
     */
    @Override
    public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo aggregate)
            throws AuthenticationException {
        return aggregate;
    }

    /**
     * 此方法在每个Realm调用之后调用
     *
     * @author shiloh
     * @date 2022/1/28 15:20
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
            }
        }

        return authenticationInfo;
    }

    /**
     * 此方法在所有realm调用之后调用
     *
     * @author shiloh
     * @date 2022/1/28 15:22
     */
    @Override
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token, AuthenticationInfo aggregate)
            throws AuthenticationException {
        if (Objects.isNull(aggregate) || aggregate.getPrincipals().isEmpty()
                || aggregate.getPrincipals().getRealmNames().size() < 2) {
            throw new AuthenticationException("无法验证类型为：" + token.getClass()
                    + "的令牌，请确保至少有2个或以上的已配置的Realm支持类型为" + token.getClass() + "的令牌");
        }

        return aggregate;
    }
}
