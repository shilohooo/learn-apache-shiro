use learn_shiro;

drop table if exists oauth2_user;
drop table if exists oauth2_client;

# 创建表结构
# 用户表存储着认证 / 资源服务器的用户信息，即资源拥有者；比如用户名 / 密码
create table oauth2_user
(
    id       bigint(20) primary key auto_increment comment '主键ID',
    username varchar(100) comment '用户名',
    password varchar(500) comment '密码',
    salt     varchar(255) comment '密码盐'
) comment 'oauth2 用户表';
# 客户端表存储客户端的的客户端 id 及客户端安全 key；在进行授权时使用。
create table oauth2_client
(
    id            bigint(20) primary key auto_increment comment '主键ID',
    client_name   varchar(100) comment '客户端名称',
    client_id     varchar(255) comment '客户端ID',
    client_secret varchar(255) comment '客户端密钥'
) comment 'oauth2 客户端表';
