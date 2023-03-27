use learn_shiro;

# 清空旧数据
delete
from sys_user_role;
delete
from sys_role_menu;
delete
from sys_user;
delete
from sys_role;
delete
from sys_menu;

# 插入测试数据

# 插入用户信息
insert into sys_user(id, username, password, salt)
values (1, 'shiloh', '49327fa3557b4a1bf8844a112c9b1faf', '931532162f503a3e30f3169c98dd8dfd'),
       (2, 'jackie', '49327fa3557b4a1bf8844a112c9b1faf', '931532162f503a3e30f3169c98dd8dfd'),
       (3, 'bruce', '49327fa3557b4a1bf8844a112c9b1faf', '931532162f503a3e30f3169c98dd8dfd');

# 插入菜单信息
insert into sys_menu(id, name, type, order_no, url, parent_id, permission)
values (1, '系统管理', 0, 1, null, null, null),
       (2, '用户管理', 1, 2, '/sys/user/index', 1, 'sys:user:list'),
       (3, '菜单管理', 1, 3, '/sys/menu/index', 1, 'sys:menu:list'),
       (4, '角色管理', 1, 4, '/sys/role/index', 1, 'sys:role:list'),
       (5, '新增用户', 2, 5, null, 2, null);

# 插入角色信息
insert into sys_role(id, name, parent_id)
values (1, '超级管理员', null),
       (2, '系统管理员', 1),
       (3, '普通管理员', 2);

# 插入用户角色关联关系
insert into sys_user_role(user_id, role_id)
values (1, 1),
       (2, 2),
       (3, 3);

# 插入角色菜单关联关系
insert into sys_role_menu(role_id, menu_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (2, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (3, 1),
       (3, 2);