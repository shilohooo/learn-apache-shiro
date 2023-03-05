# 创建存储 session 信息的表
create table sessions(
    id varchar(255) primary key comment 'Session Id',
    session varchar(2000) comment '会话信息'
) comment '登录会话信息表';