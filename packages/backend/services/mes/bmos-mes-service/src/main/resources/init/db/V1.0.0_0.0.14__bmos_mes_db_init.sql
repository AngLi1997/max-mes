use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
create table bm_ingredient_input_record
(
    id                                bigint(20) primary key comment '物理主键',
    ingredient_weigh_batch_process_id bigint(20)   null comment '生产计划id',
    ingredient_plan_id                bigint(20)   null comment '配料单id',
    storage_material_batch_id         bigint(20)   null comment '暂存物料批次id',
    storage_material_id               bigint(20)   null comment '物料id',
    device_id                         bigint(20)   null comment '设备id',
    importer_id                       varchar(100) null comment '投料人id',
    remark                            varchar(100) null comment '备注',
    input_time                        datetime     null comment '投料时间',
    create_time                       datetime     null,
    update_time                       datetime     null,
    create_by                         varchar(100) null,
    update_by                         varchar(100) null,
    is_deleted                        tinyint(1)   null default 0
) comment '配料投入记录表';
SET FOREIGN_KEY_CHECKS = 1;
