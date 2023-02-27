package org.shiloh.test;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.shiloh.test.base.BaseShiroTests;

/**
 * 密码验证测试
 *
 * @author shiloh
 * @date 2023/2/27 10:21
 */
public class PasswordValidateTests extends BaseShiroTests {
    /**
     * 测试使用自定义的 Realm 进行密码验证登录，
     *
     * @author shiloh
     * @date 2023/2/27 10:25
     */
    @Test
    public void testPasswordServiceWithMyRealm() {
        login("classpath:shiro/config/shiro-password-service.ini", "shiloh", "123456");
    }

    /**
     * 测试使用 JdbcRealm 进行密码验证登录，
     *
     * @author shiloh
     * @date 2023/2/27 10:25
     */
    @Test
    public void testPasswordServiceWithJdbcRealm() {
        login("classpath:shiro/config/shiro-jdbc-password-service.ini", "wu", "123");
    }

    /**
     * 测试 {@link org.apache.shiro.authc.credential.HashedCredentialsMatcher} 验证密码登录
     *
     * @author shiloh
     * @date 2023/2/27 11:10
     */
    @Test
    public void testHashCredentialsMatcherWithMyRealm2() {
        // 此处的帐号密码来自testGeneratePassword
        login("classpath:shiro/config/shiro-hash-credentials-matcher.ini", "liu", "123");
    }

    /**
     * 测试 {@link org.apache.shiro.authc.credential.HashedCredentialsMatcher}
     * 和 {@link org.apache.shiro.realm.jdbc.JdbcRealm} 验证密码登录
     *
     * @author shiloh
     * @date 2023/2/27 11:12
     */
    @Test
    public void testHashCredentialsMatcherWithJdbcRealm() {
        BeanUtilsBean.getInstance()
                .getConvertUtils()
                .register(new EnumConverter(), JdbcRealm.SaltStyle.class);
        // 此处的帐号密码来自testGeneratePassword
        login("classpath:shiro/config/shiro-jdbc-hash-credentials-matcher.ini", "liu", "123");
    }

    /**
     * 测试生成密码
     * <p>
     * 如果要写用户模块，需要在新增用户 / 重置密码时使用如上算法保存密码，
     * 将生成的密码及 salt2 存入数据库（因为我们的散列算法是：md5(md5(密码 +username+salt2))）
     *
     * @author shiloh
     * @date 2023/2/27 10:46
     */
    public static void main(String[] args) {
        final String username = "liu";
        final String password = "123";
        final String randomSalt = new SecureRandomNumberGenerator().nextBytes().toHex();
        Assertions.assertThat(randomSalt).isNotBlank();
        final SimpleHash simpleHash = new SimpleHash("md5", password, username + randomSalt, 2);
        final String encodedPassword = simpleHash.toHex();
        Assertions.assertThat(encodedPassword).isNotBlank();
        System.out.println("randomSalt = " + randomSalt);
        System.out.println("encodedPassword = " + encodedPassword);
    }

    /**
     * Shiro 默认使用了 apache commons BeanUtils，
     * 默认是不进行 Enum 类型转型的，此时需要自己注册一个 Enum 转换器
     *
     * @author shiloh
     * @date 2023/2/27 11:19
     */
    private static class EnumConverter extends AbstractConverter {
        @Override
        protected Object convertToType(Class aClass, Object o) throws Throwable {
            return Enum.valueOf(aClass, o.toString());
        }

        @Override
        protected String convertToString(Object value) throws Throwable {
            return ((Enum) value).name();
        }

        @Override
        protected Class<?> getDefaultType() {
            return Enum.class;
        }
    }
}
