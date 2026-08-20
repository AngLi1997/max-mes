use bmos_wms;
set foreign_key_checks = 0;
create table bw_operation_log
(
    id                 bigint               not null
        primary key,
    operation_type     tinyint(1)           null comment '操作类型',
    operation_business varchar(16)          null comment '业务操作',
    operation_object   mediumtext           null comment '操作对象',
    user_name          varchar(64)          null comment '操作人用户名',
    user_id            varchar(32)          null comment '操作人用户id',
    remark             varchar(255)         null comment '备注',
    ip                 varchar(20)          null,
    create_by          varchar(64)          null,
    update_by          varchar(64)          null,
    create_time        datetime             null,
    update_time        datetime             null,
    is_deleted         tinyint(1) default 0 not null,
    menu_id            bigint               null comment '菜单id'
)
    comment '操作日志表';
set foreign_key_checks = 1;