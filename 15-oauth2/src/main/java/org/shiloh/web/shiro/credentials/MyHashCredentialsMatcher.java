package org.shiloh.web.shiro.credentials;

import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.shiloh.web.entity.User;
import org.shiloh.web.service.PasswordHelper;
import org.shiloh.web.service.UserService;
import org.springframework.stereotype.Component;

/**
 * 限制密码重试次数的 {@link HashedCredentialsMatcher}
 *
 * @author shiloh
 * @date 2023/2/28 17:05
 */
@Component
@RequiredArgsConstructor
public class MyHashCredentialsMatcher extends HashedCredentialsMatcher {
    private final UserService userService;
    private final PasswordHelper passwordHelper;

    /**
     * This implementation first hashes the {@code token}'s credentials, potentially using a
     * {@code salt} if the {@code info} argument is a
     * {@link SaltedAuthenticationInfo SaltedAuthenticationInfo}.  It then compares the hash
     * against the {@code AuthenticationInfo}'s
     * {@link #getCredentials(AuthenticationInfo) already-hashed credentials}.  This method
     * returns {@code true} if those two values are {@link #equals(Object, Object) equal}, {@code false} otherwise.
     *
     * @param token the {@code AuthenticationToken} submitted during the authentication attempt.
     * @param info  the {@code AuthenticationInfo} stored in the system matching the token principal
     * @return {@code true} if the provided token credentials hash match to the stored account credentials hash,
     * {@code false} otherwise
     * @since 1.1
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        final String username = (String) token.getPrincipal();
        final User user = this.userService.findByUsername(username);
        final String credentials = this.passwordHelper.encryptPassword(
                new String(((char[]) token.getCredentials())), user.getCredentialsSalt()
        );
        token = new UsernamePasswordToken(username, credentials);
        return super.doCredentialsMatch(token, info);
    }

    /**
     * Returns a {@link Hash Hash} instance representing the already-hashed AuthenticationInfo credentials stored in the system.
     * <p/>
     * This method reconstructs a {@link Hash Hash} instance based on a {@code info.getCredentials} call,
     * but it does <em>not</em> hash that value - it is expected that method call will return an already-hashed value.
     * <p/>
     * This implementation's reconstruction effort functions as follows:
     * <ol>
     * <li>Convert {@code account.getCredentials()} to a byte array via the {@link #toBytes toBytes} method.
     * <li>If {@code account.getCredentials()} was originally a String or char[] before {@code toBytes} was
     * called, check for encoding:
     * <li>If {@link #storedCredentialsHexEncoded storedCredentialsHexEncoded}, Hex decode that byte array, otherwise
     * Base64 decode the byte array</li>
     * <li>Set the byte[] array directly on the {@code Hash} implementation and return it.</li>
     * </ol>
     *
     * @param info the AuthenticationInfo from which to retrieve the credentials which assumed to be in already-hashed form.
     * @return a {@link Hash Hash} instance representing the given AuthenticationInfo's stored credentials.
     */
    @Override
    protected Object getCredentials(AuthenticationInfo info) {
        Object credentials = info.getCredentials();
        byte[] storedBytes = toBytes(credentials);
        return new SimpleHash(
                getHashAlgorithmName(),
                storedBytes,
                ((SaltedAuthenticationInfo) info).getCredentialsSalt(),
                getHashIterations()
        );
    }
}
