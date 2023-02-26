INSERT INTO learn_shiro.users (username, password, password_salt) VALUES ('shiloh', '123456', null);

INSERT INTO learn_shiro.user_roles (username, role_name) VALUES ('shiloh', 'role1');
INSERT INTO learn_shiro.user_roles (username, role_name) VALUES ('shiloh', 'role2');

INSERT INTO learn_shiro.roles_permissions (role_name, permission) VALUES ('role1', '+user1+10');
INSERT INTO learn_shiro.roles_permissions (role_name, permission) VALUES ('role1', 'user1:*');
INSERT INTO learn_shiro.roles_permissions (role_name, permission) VALUES ('role1', '+user2+10');
INSERT INTO learn_shiro.roles_permissions (role_name, permission) VALUES ('role1', 'user2:*');
