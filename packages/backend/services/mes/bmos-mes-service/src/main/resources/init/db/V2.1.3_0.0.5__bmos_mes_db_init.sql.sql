-- 称量数据组件
create table bm_weigh_data
(
    id                    bigint               not null comment '物理主键'
        primary key,
    weight                varchar(100)          null comment '重量',
    unit_id                bigint               null comment '单位id',
    weigher_id            varchar(100)          null comment '称量人id',
    weigh_time            datetime             null comment '称量时间',
    component_instance_id bigint               null comment '称量数据组件实例id',
    create_time           datetime             null,
    update_time           datetime             null,
    create_by             varchar(64)          null,
    update_by             varchar(64)          null,
    is_deleted            tinyint(1) default 0 null
)
    comment '称量数据组件记录';