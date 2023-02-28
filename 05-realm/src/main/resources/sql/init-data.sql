# 创建数据库
create database if not exists learn_shiro;
# 切换数据库
use learn_shiro;
# 删除测试表
drop table if exists sys_users;
drop table if exists sys_roles;
drop table if exists sys_permissions;
drop table if exists sys_users_roles;
drop table if exists sys_roles_permissions;
# 创建测试表
create table sys_users
(
    id       bigint(20) auto_increment primary key comment 'ID',
    username varchar(100) comment '用户名',
    password varchar(255) comment '密码',
    salt     varchar(255) comment '密码盐',
    locked   boolean default false comment '帐号是否已被锁定：true = 是，false = 否'
) comment '用户信息';

create table sys_roles
(
    id          bigint(20) auto_increment primary key comment 'ID',
    role        varchar(100) comment '角色标识',
    description varchar(100) comment '角色描述',
    available   boolean default false comment '角色是否可用：true = 是，false = 否'
) comment '角色信息';

create table sys_permissions
(
    id          bigint(20) auto_increment primary key comment 'ID',
    permission  varchar(100) comment '权限标识',
    description varchar(100) comment '权限描述',
    available   boolean default false comment '权限是否可用：true = 是，false = 否'
) comment '权限信息';

create table sys_users_roles
(
    user_id bigint(20) comment '用户 ID',
    role_id bigint(20) comment '角色 ID',
    constraint pk_sys_users_roles primary key (user_id, role_id)
) comment '用户角色关联表';

create table sys_roles_permissions
(
    role_id       bigint(20) comment '角色 ID',
    permission_id bigint(20) comment '权限 ID',
    constraint pk_sys_roles_permissions primary key (role_id, permission_id)
) comment '角色权限关联表';
