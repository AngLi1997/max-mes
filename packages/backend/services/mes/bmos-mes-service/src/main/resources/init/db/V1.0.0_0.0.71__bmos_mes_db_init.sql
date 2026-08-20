-- 物料投入记录表
drop table if exists bm_weigh_input_record;
create table if not exists bm_weigh_input_record
(
    id                          bigint               not null comment '物理主键'
        primary key,
    material_id                 bigint               null comment '物料id',
    storage_material_batch_id   bigint               null comment '物料批次id',
    storage_material_id         bigint               null comment '暂存物料id',
    quantity                    varchar(255)         null comment '投料量',
    input_component_instance_id bigint               null comment '投料组件实例id',
    unit_id                     bigint               null comment '单位id',
    input_user_id               varchar(100)         null comment '投料人id',
    input_user_name             varchar(100)         null comment '投料人名称',
    input_time                  datetime             null comment '投料时间',
    device_id                   bigint               null comment '投料设备id',
    device_name                 varchar(100)         null comment '设备名称',
    device_code                 varchar(100)         null comment '设备编码',
    weigh_requirement_key       varchar(255)         null comment '称量需求key(工步id_工步模型id（复用就是0))',
    product_plan_id             bigint               null comment '配料计划id',
    create_time                 datetime             null,
    update_time                 datetime             null,
    create_by                   varchar(64)          null,
    update_by                   varchar(64)          null,
    is_deleted                  tinyint(1) default 0 null
)
    comment '物料投入记录表';