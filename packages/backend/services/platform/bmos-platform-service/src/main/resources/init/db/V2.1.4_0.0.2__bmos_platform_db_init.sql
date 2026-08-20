-- 签名密码
create table bp_user_signature_pwd
(
    id                 bigint               not null comment '主键'
        primary key,
    user_id            varchar(255)         not null comment '用户id',
    signature_password varchar(255)         not null comment '签名密码',
    create_time        datetime             null,
    update_time        datetime             null,
    create_by          varchar(100)         null,
    update_by          varchar(100)         null,
    is_deleted         tinyint(1) default 0 null
) comment '签名密码';