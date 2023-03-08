package org.shiloh.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shiloh.web.ComponentScanConfig;
import org.shiloh.web.entity.Permission;
import org.shiloh.web.entity.Role;
import org.shiloh.web.entity.User;
import org.shiloh.web.service.PermissionService;
import org.shiloh.web.service.RoleService;
import org.shiloh.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

/**
 * Shiro 集成 Spring 单元测试
 *
 * @author shiloh
 * @date 2023/3/8 17:30
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ComponentScanConfig.class)
@Rollback(false)
public class ShiroTests {
    /* ============================= SQL CONSTANTS ============================== */

    /**
     * SQL：删除所有用户信息
     */
    private static final String DELETE_ALL_USERS = "delete from learn_shiro.sys_users";

    /**
     * SQL：删除所有角色信息
     */
    private static final String DELETE_ALL_ROLES = "delete from learn_shiro.sys_roles";

    /**
     * SQL：删除所有权限信息
     */
    private static final String DELETE_ALL_PERMISSIONS = "delete from learn_shiro.sys_permissions";

    /**
     * SQL：删除所有用户与角色关联数据
     */
    private static final String DELETE_ALL_USER_ROLE_REFS = "delete from learn_shiro.sys_users_roles";

    /**
     * SQL：删除所有角色与权限关联数据
     */
    private static final String DELETE_ALL_ROLE_PERMISSION_REFS = "delete from learn_shiro.sys_roles_permissions";

    /* ============================= INSTANCE FIELDS ============================== */

    @Autowired
    protected UserService userService;

    @Autowired
    protected RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    // @Autowired
    // private MyShiroRealm myShiroRealm;

    // @Autowired
    // private JdbcTemplate jdbcTemplate;

    /**
     * 测试密码
     */
    protected static final String PASSWORD = "123456";

    /**
     * 权限
     */
    protected Permission permission1;
    protected Permission permission2;
    protected Permission permission3;

    /**
     * 角色
     */
    protected Role role1;
    protected Role role2;

    /**
     * 用户
     */
    protected User user1;
    protected User user2;
    protected User user3;
    protected User user4;

    /**
     * 插入测试数据
     *
     * @author shiloh
     * @date 2023/3/1 17:04
     */
    // @Before
    public void setup() {
        // 清除旧数据
        // this.jdbcTemplate.update(DELETE_ALL_USERS);
        // this.jdbcTemplate.update(DELETE_ALL_ROLES);
        // this.jdbcTemplate.update(DELETE_ALL_PERMISSIONS);
        // this.jdbcTemplate.update(DELETE_ALL_USER_ROLE_REFS);
        // this.jdbcTemplate.update(DELETE_ALL_ROLE_PERMISSION_REFS);

        // 1.新增权限信息
        permission1 = this.permissionService.add(new Permission("user:create", "用户模块新增", Boolean.TRUE));
        permission2 = this.permissionService.add(new Permission("user:update", "用户模块修改", Boolean.TRUE));
        permission3 = this.permissionService.add(new Permission("menu:create", "菜单模块新增", Boolean.TRUE));

        // 2.新增角色信息
        role1 = this.roleService.add(new Role("admin", "管理员", Boolean.TRUE));
        role2 = this.roleService.add(new Role("user", "用户管理员", Boolean.TRUE));

        // 3.新增角色与权限关联数据
        this.roleService.addPermissionRefs(role1.getId(), permission1.getId());
        this.roleService.addPermissionRefs(role1.getId(), permission2.getId());
        this.roleService.addPermissionRefs(role1.getId(), permission3.getId());

        this.roleService.addPermissionRefs(role2.getId(), permission1.getId());
        this.roleService.addPermissionRefs(role2.getId(), permission2.getId());

        // 4.新增用户信息
        user1 = this.userService.add(new User("shiloh", PASSWORD, Boolean.FALSE));
        user2 = this.userService.add(new User("tom", PASSWORD, Boolean.FALSE));
        user3 = this.userService.add(new User("bruce", PASSWORD, Boolean.FALSE));
        user4 = this.userService.add(new User("jack", PASSWORD, Boolean.TRUE));

        // 5.新增用户与角色关联数据
        this.userService.addRoleRefs(user1.getId(), role1.getId());
    }

    /**
     * Shiro 集成 Spring 测试
     *
     * @author shiloh
     * @date 2023/3/8 17:00
     */
    @Test
    public void test() {
        // final Subject subject = SecurityUtils.getSubject();
        // UsernamePasswordToken token = new UsernamePasswordToken(user1.getUsername(), user1.getPassword());
        // subject.login(token);
        // assertThat(subject.isAuthenticated()).isTrue();
        // subject.checkRole("admin");
        // subject.checkPermission("user:create");
        // this.userService.changePassword(user1.getId(), PASSWORD + "1");
        // this.myShiroRealm.clearCache(subject.getPrincipals());
        //
        // this.user1 = this.userService.findByUsername(user1.getUsername());
        // token = new UsernamePasswordToken(user1.getUsername(), user1.getPassword());
        // subject.login(token);
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ComponentScanConfig.class);
        final String[] beanNames = context.getBeanNamesForAnnotation(Bean.class);
        System.out.println(Arrays.toString(beanNames));
    }
}
