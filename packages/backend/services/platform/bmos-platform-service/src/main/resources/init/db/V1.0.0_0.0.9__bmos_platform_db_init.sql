create table bp_password_history
(
    id          bigint               not null comment '主键'
        primary key,
    user_id     varchar(64)          not null comment '账户id',
    pwd         varchar(255)         not null comment '编码',
    create_by   varchar(64)          null,
    update_by   varchar(64)          null,
    create_time datetime             null,
    update_time datetime             null,
    is_deleted  tinyint(1) default 0 not null
)
    comment '密码历史表' row_format = DYNAMIC;