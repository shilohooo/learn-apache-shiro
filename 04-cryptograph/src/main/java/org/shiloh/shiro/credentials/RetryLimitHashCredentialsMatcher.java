package org.shiloh.shiro.credentials;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限制密码重试次数的 {@link org.apache.shiro.authc.credential.HashedCredentialsMatcher}
 *
 * @author shiloh
 * @date 2023/2/28 17:05
 */
public class RetryLimitHashCredentialsMatcher extends HashedCredentialsMatcher {
    private final Ehcache passwordRetryCache;

    public RetryLimitHashCredentialsMatcher() {
        final CacheManager cacheManager = CacheManager.newInstance(
                Objects.requireNonNull(
                        CacheManager.class.getClassLoader()
                                .getResource("shiro/config/ehcache.xml")
                )
        );
        this.passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }


    /**
     * 重写该方法限制用户密码重试次数
     * <p>
     * 如果密码输入正确清除 cache 中的记录；否则 cache 中的重试次数 +1，如果超出 5 次那么抛出异常表示超出重试次数了
     *
     * @param token 认证 token 对象
     * @param info  认证信息
     * @return 凭证验证成功返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/2/28 17:05
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        final String username = (String) token.getPrincipal();
        // 重试次数 + 1
        Element element = this.passwordRetryCache.get(username);
        if (element == null) {
            element = new Element(username, new AtomicInteger(0));
            this.passwordRetryCache.put(element);
        }
        final AtomicInteger retryCount = (AtomicInteger) element.getObjectValue();
        if (retryCount.incrementAndGet() > 5) {
            // 重试次数大于 5 次，抛出异常
            throw new ExcessiveAttemptsException();
        }


        final boolean matchOrNot = super.doCredentialsMatch(token, info);
        if (matchOrNot) {
            // 清除重试次数
            this.passwordRetryCache.remove(username);
        }
        return matchOrNot;
    }
}
