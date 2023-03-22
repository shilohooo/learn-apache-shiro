package org.shiloh.web.service;

/**
 * OAuth Service
 *
 * @author shiloh
 * @date 2023/3/20 23:24
 */
public interface OAuthService {
    /**
     * 添加 AuthCode 到对应的用户名
     *
     * @param authCode authCode
     * @param username 用户名
     * @author shiloh
     * @date 2023/3/20 23:27
     */
    void addAuthCode(String authCode, String username);

    /**
     * 添加 AccessToken 到对应的用户名
     *
     * @param accessToken accessToken
     * @param username    用户名
     * @author shiloh
     * @date 2023/3/20 23:27
     */
    void addAccessToken(String accessToken, String username);

    /**
     * 验证 AuthCode 是否有效
     *
     * @param authCode authCode
     * @return 有效返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/3/20 23:27
     */
    Boolean checkAuthCode(String authCode);

    /**
     * 验证 AccessToken 是否有效
     *
     * @param accessToken accessToken
     * @return 有效返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/3/20 23:27
     */
    Boolean checkAccessToken(String accessToken);

    /**
     * 根据 AuthCode 获取用户名
     *
     * @param authCode authCode
     * @return 用户名
     * @author shiloh
     * @date 2023/3/20 23:28
     */
    String getUsernameByAuthCode(String authCode);

    /**
     * 根据 AccessToken 获取用户名
     *
     * @param accessToken accessToken
     * @return 用户名
     * @author shiloh
     * @date 2023/3/20 23:28
     */
    String getUsernameByAccessToken(String accessToken);

    /**
     * 获取 AuthCode / AccessToken 的过期时间，单位：秒
     *
     * @return AuthCode / AccessToken 的过期时间，单位：秒
     * @author shiloh
     * @date 2023/3/20 23:29
     */
    Long getExpireIn();

    /**
     * 验证 ClientId 是否有效
     *
     * @param clientId clientId
     * @return 有效返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/3/20 23:29
     */
    Boolean checkClientId(String clientId);

    /**
     * 验证 ClientSecret 是否有效
     *
     * @param clientSecret clientSecret
     * @return 有效返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/3/20 23:29
     */
    Boolean checkClientSecret(String clientSecret);
}
