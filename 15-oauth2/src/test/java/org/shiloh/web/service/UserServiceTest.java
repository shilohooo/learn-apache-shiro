package org.shiloh.web.service;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shiloh.web.WebApp;
import org.shiloh.web.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebApp.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    private static final RandomNumberGenerator RANDOM_NUMBER_GENERATOR = new SecureRandomNumberGenerator();

    /**
     * 新增用户测试
     *
     * @author shiloh
     * @date 2023/3/20 22:48
     */
    @Test
    public void create() {
        User user = new User();
        user.setUsername("shiloh");
        user.setPassword("123456");
        user.setSalt(RANDOM_NUMBER_GENERATOR.nextBytes(16).toHex());

        user = this.userService.create(user);
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId()).isNotNull();

        user = this.userService.findById(user.getId());
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getUsername()).isNotBlank();
    }

    /**
     * 删除用户测试
     *
     * @author shiloh
     * @date 2023/3/20 22:53
     */
    @Test
    public void deleteById() {
        final long id = 7L;
        this.userService.deleteById(id);
        final User user = this.userService.findById(id);
        Assertions.assertThat(user).isNull();
    }

    /**
     * 修改用户信息测试
     *
     * @author shiloh
     * @date 2023/3/20 23:02
     */
    @Test
    public void update() {
        final long id = 9L;
        User user = this.userService.findById(id);
        Assertions.assertThat(user).isNotNull();
        final String username = "shiloh595";
        user.setUsername(username);
        this.userService.update(user);

        user = this.userService.findById(id);
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(username).isEqualTo(user.getUsername());
    }

    /**
     * 根据 ID 查询用户信息测试
     *
     * @author shiloh
     * @date 2023/3/20 23:05
     */
    @Test
    public void findById() {
        final User user = this.userService.findById(9L);
        Assertions.assertThat(user).isNotNull();
    }

    /**
     * 查询所有用户信息测试
     *
     * @author shiloh
     * @date 2023/3/20 23:06
     */
    @Test
    public void findAll() {
        final List<User> users = this.userService.findAll();
        Assertions.assertThat(users).isNotEmpty();
    }

    /**
     * 根据用户名查询用户信息测试
     *
     * @author shiloh
     * @date 2023/3/20 23:11
     */
    @Test
    public void findByUsername() {
        final User user = this.userService.findByUsername("shiloh595");
        Assertions.assertThat(user).isNotNull();
    }

    /**
     * 修改密码测试
     *
     * @author shiloh
     * @date 2023/3/20 23:12
     */
    @Test
    public void changePassword() {
        this.userService.changePassword(9L, "654321");
    }
}