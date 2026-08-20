-- auto-generated definition
create table bp_acquisition_point
(
    id              bigint               null comment '主键id',
    code            varchar(128)         not null,
    name            varchar(255)         not null comment '名称',
    data_point_name varchar(255)         not null comment '采集点名称，用这个值和scada系统关联',
    type            varchar(64)          null comment '类型',
    data_type       varchar(64)          null comment '数据类型',
    status          varchar(64)          not null comment '状态',
    description     varchar(500)         null comment '描述',
    create_by       varchar(64)          not null comment '创建人',
    update_by       varchar(64)          not null comment '更新人',
    create_time     datetime             not null comment '创建时间',
    update_time     datetime             not null comment '更新时间',
    is_deleted      tinyint(1) default 0 not null comment '是否删除'
)
    comment '采集点数据表';

