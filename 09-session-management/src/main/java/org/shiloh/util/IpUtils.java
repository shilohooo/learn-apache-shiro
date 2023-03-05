package org.shiloh.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Ip工具类
 *
 * @author shiloh
 * @date 2023/3/5 22:04
 */
public final class IpUtils {

    /**
     * 请求头 key
     */
    private static final String LOWER_X_FORWARDED_FOR = "x-forwarded-for";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String UPPER_X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String X_REAL_IP = "X-Real-IP";

    /**
     * 未知 IP 地址
     */
    private static final String UNKNOWN_IP = "unknown";

    private IpUtils() {
    }

    /**
     * 从当前请求中获取 IP 地址
     *
     * @param request 当前请求对象
     * @return IP 地址
     * @author shiloh
     * @date 2023/3/5 22:05
     */
    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN_IP;
        }

        String ip = request.getHeader(LOWER_X_FORWARDED_FOR);
        if (StringUtils.isBlank(ip) || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader(PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader(UPPER_X_FORWARDED_FOR);
        }
        if (StringUtils.isBlank(ip) || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader(X_REAL_IP);
        }
        if (StringUtils.isBlank(ip) || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
