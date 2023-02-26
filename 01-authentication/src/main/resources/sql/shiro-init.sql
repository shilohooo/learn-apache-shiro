create database learn_shiro;

use learn_shiro;

create table users(
    id bigint(20) primary key auto_increment comment 'ID',
    username varchar(255) comment '用户名',
    password varchar(255) comment '密码'
) comment '用户信息表';

create table user_roles(
  id bigint(20) primary key auto_increment comment 'ID',
  username varchar(255) comment '用户名',
  role_name varchar(50) comment '角色名称'
) comment '用户角色信息表';

create table role_permissions(
    id bigint(20) primary key auto_increment comment 'ID',
    role_name varchar(255) comment '角色名称',
    permission varchar(255) comment '角色权限'
) comment '角色权限表';

insert into users(username, password) VALUES ('shiloh', '123456');

