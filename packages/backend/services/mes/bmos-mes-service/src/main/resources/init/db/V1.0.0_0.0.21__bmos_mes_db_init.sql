use bmos_mes;
set names utf8mb4;
set foreign_key_checks = 0;

create table bm_output_weigh_process
(
    id                                  bigint(20) primary key comment '物理主键',
    product_plan_id                     bigint(20)   null comment '生产计划id',
    procedure_step_id                   bigint(20)   null comment '工序步骤id',
    copy_version                        int(11)      null comment '拷贝版本',
    component_id                        bigint(20)   null comment '组件id',
    weigher_id                          varchar(100) null comment '称量人id',
    re_checker_id                       varchar(100) null comment '复核人id',
    remark                              varchar(100) null comment '备注',
    material_id                         bigint(20)   null comment '物料id',
    storage_material_batch_no           varchar(100) null comment '物料批次编号',
    expired_date                        date         null comment '有效期',
    relevance_material_id               bigint(20)   null comment '关联物料id',
    relevance_storage_material_batch_id bigint(20)   null comment '关联物料批次id',
    create_time                         datetime     null,
    update_time                         datetime     null,
    create_by                           varchar(100) null,
    update_by                           varchar(100) null,
    is_deleted                          tinyint(1)   null default 0
) comment '产出称量流程表';

create table bm_output_weigh_record
(
    id                        bigint(20) primary key comment '物理主键',
    output_weigh_process_id   bigint(20)   null comment '产出称量流程id',
    storage_material_id       bigint(20)   null comment '物料件id',
    storage_material_no       varchar(100) null comment '物料件号',
    storage_material_batch_id bigint(20)   null comment '暂存物料批次id',
    tare_weight               varchar(255) null comment '皮重',
    gross_weight              varchar(255) null comment '毛重',
    net_weight                varchar(255) null comment '净重',
    unit_id                   bigint(20)   null comment '单位id',
    container_id              bigint(20)   null comment '容器id',
    container_name            varchar(100) null comment '容器名称',
    weigh_mode                int          null comment '称量模式',
    sign_status               int          null comment '签名状态',
    material_position_id      bigint(20)   null comment '货位id',
    weigher_id                varchar(100) null comment '称量人id',
    re_checker_id             varchar(100) null comment '复核人id',
    weigh_time                datetime     null comment '称量时间',
    create_time               datetime     null,
    update_time               datetime     null,
    create_by                 varchar(100) null,
    update_by                 varchar(100) null,
    is_deleted                tinyint(1)   null default 0
) comment '产出称量记录表';
set foreign_key_checks = 1;