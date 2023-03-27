# 创建数据库
create database if not exists learn_shiro
    charset utf8mb4 collate utf8mb4_unicode_ci;

# 切换数据库
use learn_shiro;

# 删除表
drop table if exists sys_user;
drop table if exists sys_menu;
drop table if exists sys_role;
drop table if exists sys_user_role;
drop table if exists sys_role_menu;

# 创建表

# 用户信息表
create table sys_user
(
    id       bigint(20) primary key auto_increment comment '自增主键',
    username varchar(50) comment '用户名',
    password varchar(500) comment '密码',
    salt     varchar(255) comment '盐'
) comment '用户信息表';

# 菜单信息表
create table sys_menu
(
    id         bigint(20) primary key auto_increment comment '自增主键',
    name       varchar(50) comment '菜单名称',
    type       tinyint(1) comment '菜单类型：0 = 目录，1 = 菜单，2 = 按钮',
    order_no   int(11) comment '排序号',
    url        varchar(255) comment '路由 / iframe 页面地址',
    parent_id  bigint(20) comment '上级菜单 ID，顶级菜单的 parent_id 为 null / 0',
    permission varchar(255) comment '权限字符串',
    available  bit(1) default b'1' comment '是否有效：1 = 是，0 = 否'
) comment '菜单信息表';

# 角色信息表
create table sys_role
(
    id        bigint(20) primary key auto_increment comment '自增主键',
    name      varchar(255) comment '角色名称',
    parent_id bigint(20) comment '上级角色 ID，顶级角色的 parent_id 为 null / 0',
    available bit(1) default b'1' comment '是否有效：1 = 是，0 = 否'
) comment '角色信息表';

# 用户角色关联表
create table sys_user_role
(
    user_id bigint(20) comment '用户 ID',
    role_id bigint(20) comment '角色 ID'
) comment '用户角色关联表';

# 角色菜单关联表
create table sys_role_menu
(
    role_id bigint(20) comment '角色 ID',
    menu_id bigint(20) comment '菜单 ID'
) comment '角色菜单关联表';