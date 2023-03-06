package org.shiloh.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.shiloh.entity.User;

/**
 * 密码加密工具类
 *
 * @author shiloh
 * @date 2023/3/1 16:30
 */
public final class PasswordEncryptUtils {
    private PasswordEncryptUtils() {
    }

    /**
     * 随机数生成器
     */
    private static final RandomNumberGenerator RANDOM_NUMBER_GENERATOR = new SecureRandomNumberGenerator();

    /**
     * 密码加密使用的算法名称
     */
    private static final String ALGORITHM_NAME = "md5";

    /**
     * Hash 散列次数
     */
    private static final int HASH_ITERATIONS = 2;

    /**
     * 用户密码加密，以及盐设置
     *
     * @param user 用户信息
     * @author shiloh
     * @date 2023/3/1 16:32
     */
    public static void encryptPassword(User user) {
        user.setSalt(RANDOM_NUMBER_GENERATOR.nextBytes().toHex());
        final String newPassword = new SimpleHash(
                ALGORITHM_NAME,
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                HASH_ITERATIONS
        )
                .toHex();
        user.setPassword(newPassword);
    }
}
