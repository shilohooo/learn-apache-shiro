package org.shiloh.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.shiloh.web.utils.PasswordUtils;

/**
 * @author shiloh
 * @date 2023/3/27 22:48
 */
@Slf4j
public class PasswordTests {
    /**
     * 密码加密测试
     *
     * @author shiloh
     * @date 2023/3/27 22:58
     */
    @Test
    public void test() {
        final String plaintext = "123456";
        log.info("password: {}", plaintext);
        final String salt = PasswordUtils.generateSalt();
        log.info("salt: {}", salt);
        final String encryptedPassword = PasswordUtils.encrypt(plaintext, salt);
        log.info("encryptedPassword: {}", encryptedPassword);
    }
}
