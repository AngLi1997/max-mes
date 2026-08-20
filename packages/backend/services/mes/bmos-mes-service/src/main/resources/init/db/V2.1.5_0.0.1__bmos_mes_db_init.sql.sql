-- form_data表中添加工步模型id
alter table bm_execute_form_data
    add procedure_step_model_id bigint DEFAULT NULL COMMENT '工步模型id';

ALTER TABLE `bm_execute_form_data` ADD COLUMN `is_empty_value` tinyint(1) NULL COMMENT '是否是录入的空值';


-- 新增工艺看板配置表
create table bm_process_dashboard_config
(
    id              bigint            not null comment '物理主键'
        primary key,
    process_id      bigint            null comment '工艺id',
    process_name    varchar(255)      null comment '工艺名称',
    process_version varchar(255)      null comment '工艺版本',
    procedure_list  varchar(2048)     null comment '工序配置json',
    create_time     datetime          null,
    update_time     datetime          null,
    create_by       varchar(100)      null,
    update_by       varchar(100)      null,
    is_deleted      tinyint default 0 null comment '是否删除'
) comment '工艺看板配置表';

-- 设备数采数据
alter table bm_procedure_equipment_acquisition
    add column `acquisition_sort` int DEFAULT NULL COMMENT '采集顺序';
alter table bm_procedure_equipment_acquisition
    add column `reuse` tinyint(1) DEFAULT NULL COMMENT '是否复用';
alter table bm_procedure_equipment_acquisition
    add column `equipment_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备名称';
alter table bm_procedure_equipment_acquisition
    add column `equipment_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备编码';


drop table IF exists bm_weigh_requirement;
create table bm_weigh_requirement
(
    id                        bigint               not null comment '物理主键'
        primary key,
    procedure_step_config_id  bigint               null comment '组件配置id',
    formula_material_id       bigint               null comment '配方物料id',
    unit_id                   bigint               null comment '单位id',
    weigh_centre_id           bigint               null comment '称量中心id',
    requirement_date          date                 null comment '需求日期',
    expired_date              date                 null comment '失效日期',
    requirement_quantity      varchar(100)         null comment '需求量',
    product_plan_id           bigint               null comment '生产批次id',
    batch_no                  varchar(100)         null comment '生产批次号',
    product_name              varchar(100)         null comment '产品名称',
    product_merge_code        varchar(100)         null comment '产品合并编码',
    weigh_requirement_task_id bigint               null comment '规划称量任务id',
    program_time              datetime             null comment '规划时间',
    requirement_status        int                  null comment '需求状态 0 未规划 1 未称量 2 称量中 3 已完成 4 已失效',
    weigh_status              int                  null comment '称量状态 0 未称量 1 称量中 2 已完成称量 3 已完成签名',
    weigh_process             int                  null comment '称量阶段 1 物料称量 2 更换需求 3 余料称量 4 已完成称量 5 已完成签名',
    storage_material_batch_id bigint               null comment '当前添加物料批次id',
    weigher_id                varchar(255)         null comment '称量人id',
    re_checker_id             bigint               null comment '复核人id',
    remark                    varchar(200)         null comment '备注',
    create_time               datetime             null,
    update_time               datetime             null,
    create_by                 varchar(64)          null,
    update_by                 varchar(64)          null,
    is_deleted                tinyint(1) default 0 null
)
    comment '称量中心称量需求表';

-- 设备数采数据
alter table bm_procedure_equipment_acquisition
    add column `group_component_id` bigint DEFAULT NULL COMMENT '分组组件id';

drop table if exists bm_weigh_input_record;
create table bm_weigh_input_record
(
    id                          bigint               not null comment '物理主键'
        primary key,
    material_id                 bigint               null comment '物料id',
    formula_material_id         bigint               null comment '配方物料id',
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
    requirement_id              bigint               null comment '称量需求id',
    product_plan_id             bigint               null comment '配料计划id',
    create_time                 datetime             null,
    update_time                 datetime             null,
    create_by                   varchar(64)          null,
    update_by                   varchar(64)          null,
    is_deleted                  tinyint(1) default 0 null
) comment '物料投入记录表';