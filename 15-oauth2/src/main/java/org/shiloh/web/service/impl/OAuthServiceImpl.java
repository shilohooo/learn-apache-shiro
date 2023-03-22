package org.shiloh.web.service.impl;

import org.shiloh.web.service.ClientService;
import org.shiloh.web.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * OAuth Service Impl
 *
 * @author shiloh
 * @date 2023/3/21 21:44
 */
@Service
public class OAuthServiceImpl implements OAuthService {
    private final ClientService clientService;
    private Cache cache;

    @Autowired
    public OAuthServiceImpl(ClientService clientService, CacheManager cacheManager) {
        this.clientService = clientService;
        this.cache = cacheManager.getCache("code-cache");
    }


    /**
     * 添加 AuthCode 到对应的用户名
     *
     * @param authCode authCode
     * @param username 用户名
     * @author shiloh
     * @date 2023/3/20 23:27
     */
    @Override
    public void addAuthCode(String authCode, String username) {
        this.cache.put(authCode, username);
    }

    /**
     * 添加 AccessToken 到对应的用户名
     *
     * @param accessToken accessToken
     * @param username    用户名
     * @author shiloh
     * @date 2023/3/20 23:27
     */
    @Override
    public void addAccessToken(String accessToken, String username) {
        this.cache.put(accessToken, username);
    }

    /**
     * 验证 AuthCode 是否有效
     *
     * @param authCode authCode
     * @return 有效返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/3/20 23:27
     */
    @Override
    public Boolean checkAuthCode(String authCode) {
        return this.cache.get(authCode) != null;
    }

    /**
     * 验证 AccessToken 是否有效
     *
     * @param accessToken accessToken
     * @return 有效返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/3/20 23:27
     */
    @Override
    public Boolean checkAccessToken(String accessToken) {
        return this.cache.get(accessToken) != null;
    }

    /**
     * 根据 AuthCode 获取用户名
     *
     * @param authCode authCode
     * @return 用户名
     * @author shiloh
     * @date 2023/3/20 23:28
     */
    @Override
    public String getUsernameByAuthCode(String authCode) {
        return (String) this.cache.get(authCode).get();
    }

    /**
     * 根据 AccessToken 获取用户名
     *
     * @param accessToken accessToken
     * @return 用户名
     * @author shiloh
     * @date 2023/3/20 23:28
     */
    @Override
    public String getUsernameByAccessToken(String accessToken) {
        return (String) this.cache.get(accessToken).get();
    }

    /**
     * 获取 AuthCode / AccessToken 的过期时间，单位：秒
     *
     * @return AuthCode / AccessToken 的过期时间，单位：秒
     * @author shiloh
     * @date 2023/3/20 23:29
     */
    @Override
    public Long getExpireIn() {
        return 3600L;
    }

    /**
     * 验证 ClientId 是否有效
     *
     * @param clientId clientId
     * @return 有效返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/3/20 23:29
     */
    @Override
    public Boolean checkClientId(String clientId) {
        return this.clientService.findByClientId(clientId) != null;
    }

    /**
     * 验证 ClientSecret 是否有效
     *
     * @param clientSecret clientSecret
     * @return 有效返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/3/20 23:29
     */
    @Override
    public Boolean checkClientSecret(String clientSecret) {
        return this.clientService.findByClientSecret(clientSecret) != null;
    }
}
