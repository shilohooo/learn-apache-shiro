package org.shiloh.test.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.Ini;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Before;
import org.shiloh.entity.Permission;
import org.shiloh.entity.Role;
import org.shiloh.entity.User;
import org.shiloh.factory.JdbcTemplateFactory;
import org.shiloh.service.PermissionService;
import org.shiloh.service.RoleService;
import org.shiloh.service.UserService;
import org.shiloh.service.impl.PermissionServiceImpl;
import org.shiloh.service.impl.RoleServiceImpl;
import org.shiloh.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 测试基类
 *
 * @author shiloh
 * @date 2022/1/28 15:48
 */
@Slf4j
public class BaseTests {
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

    protected final JdbcTemplate jdbcTemplate;
    protected final PermissionService permissionService;
    protected final RoleService roleService;
    protected final UserService userService;
    protected final Logger LOG = getLogger(BaseTests.class);

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

    /* ============================= CONSTRUCTORS ============================== */

    public BaseTests() {
        this.jdbcTemplate = JdbcTemplateFactory.getInstance();
        this.permissionService = new PermissionServiceImpl();
        this.roleService = new RoleServiceImpl();
        this.userService = new UserServiceImpl();
    }

    /**
     * 插入测试数据
     *
     * @author shiloh
     * @date 2023/3/1 17:04
     */
    @Before
    public void setup() {
        // 清除旧数据
        this.jdbcTemplate.update(DELETE_ALL_USERS);
        this.jdbcTemplate.update(DELETE_ALL_ROLES);
        this.jdbcTemplate.update(DELETE_ALL_PERMISSIONS);
        this.jdbcTemplate.update(DELETE_ALL_USER_ROLE_REFS);
        this.jdbcTemplate.update(DELETE_ALL_ROLE_PERMISSION_REFS);

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
     * 统一登录逻辑
     *
     * @param iniFilePath ini配置文件位置
     * @param username    登录帐号
     * @param password    密码
     * @author shiloh
     * @date 2022/1/28 15:49
     */
    protected void login(String iniFilePath, String username, String password) {
        // 获取ini配置文件
        final Ini ini = Ini.fromResourcePath(iniFilePath);
        // 获取security manager
        final SecurityManager securityManager = new BasicIniEnvironment(ini).getSecurityManager();
        // 将security manager绑定到security utils
        SecurityUtils.setSecurityManager(securityManager);
        // 获取subject
        final Subject subject = SecurityUtils.getSubject();
        // 创建用户名、密码用于身份验证
        final UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        log.info("login password = {}", password);
        // login
        subject.login(token);

        if (subject.isAuthenticated()) {
            LOG.info("用户【{}】登录成功啦:)", username);
        }
    }

    /**
     * 获取当前线程中的{@link Subject}对象实例
     *
     * @return {@link Subject}
     * @author shiloh
     * @date 2022/1/28 15:54
     */
    protected Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 测试结束后将Subject解绑，避免影响下一次测试
     *
     * @author shiloh
     * @date 2022/1/28 15:48
     */
    @After
    public void unbindSubject() {
        // logout
        SecurityUtils.getSubject().logout();
        LOG.info("解除绑定当前线程中的subject信息");
        try {
            ThreadContext.unbindSubject();
        } catch (Exception ignored) {}
    }
}
