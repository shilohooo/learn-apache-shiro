package org.shiloh.test.realm;

import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.shiloh.test.base.BaseTests;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Shiro 多 Realm 单元测试
 *
 * @author shiloh
 * @date 2023/3/2 23:00
 */
public class PrincipalCollectionTests extends BaseTests {

    /**
     * 多 Realm 登录测试
     *
     * @author shiloh
     * @date 2023/3/2 23:00
     */
    @Test
    @SuppressWarnings("rawtypes")
    public void test() {
        login("classpath:shiro/shiro-multi-realm.ini", "shiloh", "123456");
        final Subject subject = this.getSubject();
        // 从 Subject 对象中获取 Principal，即第一个
        final Object firstPrincipal = subject.getPrincipal();
        assertThat(firstPrincipal).isNotNull();
        // 从 Subject 中获取所有 Principals
        final PrincipalCollection principals = subject.getPrincipals();
        assertThat(principals).isNotEmpty();
        // 从 principals 中获取 PrimaryPrincipal
        final Object primaryPrincipal = principals.getPrimaryPrincipal();
        assertThat(primaryPrincipal).isNotNull();
        // 由于多个 Realm 都返回了 Principal，所以不能确定他们是否一致
        assertThat(firstPrincipal).isEqualTo(primaryPrincipal);

        // 获取所有身份验证成功的 Realm 名称：a, b, c
        final Set<String> realmNames = principals.getRealmNames();
        assertThat(realmNames).isNotEmpty();
        // 将身份信息转换为 Set / List
        // Realm1 和 Realm2 返回的身份信息一致，在此处会排除掉重复的
        final Set principalSet = principals.asSet();
        assertThat(principalSet).isNotEmpty();
        // 转换为 List 时，是先转换为 Set，再转换为 List 的
        // 具体可以查看 SimplePrincipalCollection#asList()
        final List principalList = principals.asList();
        assertThat(principalList).isNotEmpty();

        // 根据 Realm 名称获取身份信息，因为 Realm 的名称可能重复，所以返回值为 Collection
        final Collection principalsByRealmName = principals.fromRealm("c");
        assertThat(principalsByRealmName).isNotEmpty();
        System.out.println();
    }
}
