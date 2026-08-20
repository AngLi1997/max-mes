-- auto-generated definition
create table bp_message_info
(
    id            bigint            not null comment '主键id'
        primary key,
    msg_type      varchar(64)       not null comment '消息类型',
    send_id       varchar(64)       not null comment '发送人',
    msg_content   text              null comment '消息内容',
    send_time     datetime          null comment '发送时间',
    create_time   datetime          null comment '创建时间',
    create_update varchar(64)       null comment '创建人',
    create_by     varchar(255)      null comment '创建人',
    update_by     varchar(64)       null comment '更新人',
    update_time   datetime          null comment '更新时间',
    is_deleted    tinyint default 0 null comment '删除标识'
);

-- auto-generated definition
create table bp_message_template
(
    id               bigint               null comment '主键id',
    title_template   text                 null comment '标题模板信息',
    content_template text                 null comment '内容模板信息',
    message_type     varchar(255)         null comment '消息类型',
    create_by        varchar(255)         null comment '创建人',
    update_by        varchar(255)         null comment '更新人',
    create_time      datetime             null comment '创建时间',
    update_time      datetime             null comment '更新时间',
    is_deleted       tinyint(1) default 0 null comment '是否删除'
)
    comment '消息模板';

-- auto-generated definition
create table bp_message_user
(
    id          bigint                  not null comment '主键id'
        primary key,
    message_id  bigint                  not null comment '消息主表id',
    msg_type    varchar(64)             not null comment '消息类型',
    user_id     varchar(64)             not null comment '接收人',
    msg_status  varchar(64) default '0' not null comment '消息状态',
    create_time datetime                null comment '创建时间',
    create_by   varchar(64)             null comment '创建人',
    update_time datetime                null comment '更新时间',
    update_by   varchar(64)             null comment '更新人',
    is_deleted  tinyint     default 0   not null comment '是否删除'
);

create index bp_message_user_message_id_index
	on bp_message_user (message_id);
