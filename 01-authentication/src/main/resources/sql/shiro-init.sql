create database learn_shiro;

use learn_shiro;

create table users
(
    id            bigint(20) primary key auto_increment comment 'ID',
    username      varchar(255) comment '用户名',
    password      varchar(255) comment '密码',
    password_salt varchar(255) comment '密码盐'
) comment '用户信息表';

create table user_roles
(
    id        bigint(20) primary key auto_increment comment 'ID',
    username  varchar(255) comment '用户名',
    role_name varchar(50) comment '角色名称'
) comment '用户角色信息表';

create table role_permissions
(
    id         bigint(20) primary key auto_increment comment 'ID',
    role_name  varchar(255) comment '角色名称',
    permission varchar(255) comment '角色权限'
) comment '角色权限表';

insert into users(username, password, password_salt)
values ('wu',
        '$shiro1$SHA-512$1$$PJkJr+wlNU1VHa4hWQuybjjVPyFzuNPcPu5MBH56scHri4UQPjvnumE7MbtcnDYhTcnxSkL9ei/bhIVrylxEwg==',
        null);
insert into users(username, password, password_salt)
values ('liu', 'a9a114054aa6758184314fbb959fbda4', '24520ee264eab73ec09451d0e9ea6aac');


