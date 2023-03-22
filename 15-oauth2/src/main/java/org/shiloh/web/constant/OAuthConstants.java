package org.shiloh.web.constant;

/**
 * OAuth 常量
 *
 * @author shiloh
 * @date 2023/3/21 22:19
 */
public final class OAuthConstants {
    private OAuthConstants() {
    }

    /**
     * 资源服务器名称
     */
    public static final String RESOURCE_SERVER_NAME = "my-resource-server";

    /**
     * 无效的 ClientId 错误信息
     */
    public static final String INVALID_CLIENT_ID_DESC = "客户端验证失败，如错误的clientId/clientSecret";
}
