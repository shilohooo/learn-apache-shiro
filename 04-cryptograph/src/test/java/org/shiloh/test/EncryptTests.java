package org.shiloh.test;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Assert;
import org.junit.Test;

import java.security.Key;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 编码 / 解码测试
 *
 * @author shiloh
 * @date 2023/2/27 9:22
 */
public class EncryptTests {
    /**
     * Base64 编码 / 解码测试
     *
     * @author shiloh
     * @date 2023/2/27 9:22
     */
    @Test
    public void testBaseEncodeAndDecode() {
        final String msg = "Hello";
        final String encodedMsg = Base64.encodeToString(msg.getBytes());
        Assert.assertEquals(msg, Base64.decodeToString(encodedMsg));
    }

    /**
     * 16进制编解码测试
     *
     * @author shiloh
     * @date 2023/2/27 9:31
     */
    @Test
    public void testHexEncodeAndDecode() {
        final String msg = "Hello";
        final String encoded = Hex.encodeToString(msg.getBytes());
        Assert.assertEquals(msg, new String(Hex.decode(encoded)));
    }

    /**
     * byte 数组与字符串互相转换的测试
     *
     * @author shiloh
     * @date 2023/2/27 9:33
     */
    @Test
    public void testByteConvert() {
        final String msg = "Hello";
        final byte[] bytes = CodecSupport.toBytes(msg, CodecSupport.PREFERRED_ENCODING);
        Assert.assertEquals(msg, CodecSupport.toString(bytes, CodecSupport.PREFERRED_ENCODING));
    }

    /**
     * MD5 散列生成摘要数据的测试
     * <p>
     * 使用MD5生成摘要数据时，要加一些只有内部系统知道的干扰数据，如用户名和 ID（即盐），
     * 这样散列的对象会从 "密码" 变为 "密码 + 用户名 + ID"，生成的散列值更难破解。
     *
     * @author shiloh
     * @date 2023/2/27 9:38
     */
    @Test
    public void testMd5Hash() {
        final String password = "Shiloh#19!98";
        final String salt = "shiloh595";
        final String encryptedPassword = new Md5Hash(password, salt).toString();
        assertThat(encryptedPassword).isNotBlank();
    }

    /**
     * SHA256 散列测试
     *
     * @author shiloh
     * @date 2023/2/27 9:43
     */
    @Test
    public void testSha256Hash() {
        final String password = "Shiloh#19!98";
        final String salt = "shiloh595";
        // 通过调用 SimpleHash 时指定散列算法，其内部使用了 Java 的 MessageDigest 实现。
        final String encryptedPassword = new SimpleHash("SHA-256", password, salt).toString();
        assertThat(encryptedPassword).isNotBlank();
        // 为了方便使用，Shiro 提供了 HashService，默认提供了 DefaultHashService 实现。
        // 默认算法为：SHA-512
        final DefaultHashService defaultHashService = new DefaultHashService();
        // 设置使用的算法名称
        defaultHashService.setHashAlgorithmName("SHA-256");
        // 设置私盐，其在散列时自动与用户传入的公盐混合产生一个新盐，默认无
        defaultHashService.setPrivateSalt(new SimpleByteSource(salt));
        // 在用户没有传入公盐的情况下是否生成公盐，默认为:false
        defaultHashService.setGeneratePublicSalt(true);
        // 设置用于生成公盐的生成器，默认为：SecureRandomNumberGenerator
        defaultHashService.setRandomNumberGenerator(new SecureRandomNumberGenerator());
        // 设置 Hash 值的迭代次数
        defaultHashService.setHashIterations(1);
        final HashRequest hashRequest = new HashRequest.Builder()
                .setAlgorithmName("MD5")
                .setSource(ByteSource.Util.bytes(password))
                .setSalt(ByteSource.Util.bytes(salt))
                .setIterations(2)
                .build();
        final String encryptedStr = defaultHashService.computeHash(hashRequest).toString();
        assertThat(encryptedStr).isNotBlank();
        final String encryptedBase64 = defaultHashService.computeHash(hashRequest).toBase64();
        assertThat(encryptedBase64).isNotBlank();
        final String encryptedHex = defaultHashService.computeHash(hashRequest).toHex();
        assertThat(encryptedHex).isNotBlank();
    }

    /**
     * 生成随机数据的测试
     *
     * @author shiloh
     * @date 2023/2/27 9:55
     */
    @Test
    public void testGenerateRandomStr() {
        final SecureRandomNumberGenerator generator = new SecureRandomNumberGenerator();
        generator.setSeed("123456".getBytes());
        final String randomBase64 = generator.nextBytes().toBase64();
        assertThat(randomBase64).isNotBlank();
        final String randomHex = generator.nextBytes().toHex();
        assertThat(randomHex).isNotBlank();
    }

    /**
     * 测试Shiro提供的对称加密算法：AES
     *
     * @author shiloh
     * @date 2023/2/27 9:57
     */
    @Test
    public void testAesEncrypt() {
        final AesCipherService aesCipherService = new AesCipherService();
        // 设置密钥长度
        aesCipherService.setKeySize(128);
        // 生成密码
        final Key key = aesCipherService.generateNewKey();
        // 加密
        final String msg = "Hello";
        final String encryptedMsg = aesCipherService.encrypt(msg.getBytes(), key.getEncoded()).toHex();
        assertThat(encryptedMsg).isNotBlank();
        // 解密
        final byte[] decryptedMsg = aesCipherService.decrypt(Hex.decode(encryptedMsg), key.getEncoded())
                .getBytes();
        assertThat(msg).isEqualTo(new String(decryptedMsg));
    }
}
