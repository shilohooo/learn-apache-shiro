package org.shiloh.web.service;

import lombok.Setter;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.shiloh.web.entity.User;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 密码加密工具类
 *
 * @author shiloh
 * @date 2023/3/1 16:30
 */
@Component
@Setter
@PropertySource("classpath:password-encrypt-config.properties")
public class PasswordHelper {

    /**
     * 随机数生成器
     */
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    private final Environment env;

    /**
     * 加密算法
     */
    private String algorithmName;

    /**
     * 散列次数
     */
    private Integer hashIterations;

    public PasswordHelper(Environment env) {
        this.env = env;
        this.algorithmName = this.env.getProperty("password.algorithmName");
        this.hashIterations = this.env.getProperty("password.hashIterations", Integer.class);
    }


    /**
     * 用户密码加密，以及盐设置
     *
     * @param user 用户信息
     * @author shiloh
     * @date 2023/3/1 16:32
     */
    public void encryptPassword(User user) {
        user.setSalt(randomNumberGenerator.nextBytes().toHex());
        final String newPassword = new SimpleHash(
                algorithmName,
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                hashIterations
        )
                .toHex();
        user.setPassword(newPassword);
    }

    /**
     * 用户密码加密，以及盐设置
     *
     * @param passwordPlaintext 明文密码
     * @param salt              密码盐 - username + salt
     * @return 加密后的密码（十六进制）
     * @author shiloh
     * @date 2023/3/1 16:32
     */
    public String encryptPassword(String passwordPlaintext, String salt) {
        return new SimpleHash(
                algorithmName,
                passwordPlaintext,
                ByteSource.Util.bytes(salt),
                hashIterations
        )
                .toHex();
    }
}
