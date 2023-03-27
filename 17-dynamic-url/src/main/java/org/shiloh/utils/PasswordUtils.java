package org.shiloh.utils;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @author shiloh
 * @date 2023/3/27 22:50
 */
public final class PasswordUtils {
    private PasswordUtils() {
    }

    /* ============================== 常量 ============================== */

    private static final RandomNumberGenerator RANDOM_NUMBER_GENERATOR = new SecureRandomNumberGenerator();

    /**
     * 加密使用的算法
     */
    private static final String ALGORITHM_NAME = "MD5";

    /**
     * 散列迭代次数
     */
    private static final int HASH_ITERATIONS = 2;

    /**
     * 密码加密
     *
     * @param plaintext 明文
     * @param salt      盐
     * @return 加密后的十六进制字符串
     * @author shiloh
     * @date 2023/3/27 22:54
     */
    public static String encrypt(String plaintext, String salt) {
        return new SimpleHash(
                ALGORITHM_NAME,
                plaintext,
                salt,
                HASH_ITERATIONS
        )
                .toHex();
    }

    /**
     * 生成盐
     *
     * @return 十六进制随机字符串
     * @author shiloh
     * @date 2023/3/27 22:57
     */
    public static String generateSalt() {
        return RANDOM_NUMBER_GENERATOR.nextBytes(16)
                .toHex();
    }
}
