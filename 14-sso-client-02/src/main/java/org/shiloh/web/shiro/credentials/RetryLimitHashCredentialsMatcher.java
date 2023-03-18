package org.shiloh.web.shiro.credentials;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.shiloh.web.entity.User;
import org.shiloh.web.service.UserService;
import org.shiloh.web.shiro.cache.ShiroRedisCacheManager;
import org.shiloh.web.util.PasswordEncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限制密码重试次数的 {@link HashedCredentialsMatcher}
 *
 * @author shiloh
 * @date 2023/2/28 17:05
 */
@Slf4j
@Component
public class RetryLimitHashCredentialsMatcher extends HashedCredentialsMatcher {
    public static final String PASSWORD_RETRY_CACHE_NAME = "passwordRetryCache";

    private final Cache<String, AtomicInteger> passwordRetryCache;

    @Autowired
    private UserService userService;

    public RetryLimitHashCredentialsMatcher(ShiroRedisCacheManager shiroRedisCacheManager) {
        this.passwordRetryCache = shiroRedisCacheManager.getCache(PASSWORD_RETRY_CACHE_NAME);
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
        AtomicInteger retryCount = this.passwordRetryCache.get(username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            this.passwordRetryCache.put(username, retryCount);
        }
        if (retryCount.incrementAndGet() > 5) {
            // 重试次数大于 5 次，抛出异常
            throw new ExcessiveAttemptsException();
        }

        log.info("密码校验开始");
        final User user = this.userService.findByUsername(username);
        final String credentials = PasswordEncryptUtils.encryptPassword(
                new String(((char[]) token.getCredentials())), user.getCredentialsSalt()
        );
        token = new UsernamePasswordToken(username, credentials);
        final boolean matchOrNot = super.doCredentialsMatch(token, info);
        if (matchOrNot) {
            // 清除重试次数
            this.passwordRetryCache.remove(username);
        }

        return matchOrNot;
    }

    @Override
    protected Object getCredentials(AuthenticationInfo info) {
        Object credentials = info.getCredentials();
        byte[] storedBytes = toBytes(credentials);
        // 认证信息中的密码经过 16 进制解码后导致登录时密码匹配失败，具体原因未知
        // if (credentials instanceof String || credentials instanceof char[]) {
        //     //account.credentials were a char[] or String, so
        //     //we need to do text decoding first:
        //     if (isStoredCredentialsHexEncoded()) {
        //         storedBytes = Hex.decode(storedBytes);
        //     } else {
        //         storedBytes = Base64.decode(storedBytes);
        //     }
        // }
        return new SimpleHash(
                getHashAlgorithmName(),
                storedBytes,
                ((SaltedAuthenticationInfo) info).getCredentialsSalt(),
                getHashIterations()
        );
    }
}
