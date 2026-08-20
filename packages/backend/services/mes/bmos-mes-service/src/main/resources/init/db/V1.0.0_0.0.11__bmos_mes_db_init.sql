use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

alter table bm_storage_material_reserve
    add batch_no varchar(100) null comment '预定生产批号' after batch_id;

create table bmos_mes.bm_ingredient_plan
(
    id                      bigint               not null
        primary key,
    name                    varchar(255)         null comment '配料单名称',
    product_plan_id         bigint               null comment '生产计划id',
    batch_no                varchar(255)         null comment '生产批号',
    record_item_id          bigint               null comment '记录项id',
    reuse                   tinyint(1)           null comment '是否复用',
    procedure_step_model_id bigint               null comment '工序步骤模型id',
    serial_no               int                  null comment '流水号',
    component_id            bigint               null comment '组件id',
    completed               tinyint(1)           null comment '该配料单是否已完成',
    create_by               varchar(64)          null,
    update_by               varchar(64)          null,
    create_time             datetime             null,
    update_time             datetime             null,
    is_deleted              tinyint(1) default 0 not null
) comment '配料计划表';

create table bmos_mes.bm_ingredient_plan_material_batch
(
    id                   bigint               not null
        primary key,
    ingredient_plan_id   bigint               not null comment '配料单id',
    material_batch_id    bigint               not null comment '物料批次id',
    formula_material_id  bigint               not null comment '配方物料id',
    ingredient_quantity  decimal(20, 10)      null comment '配料量',
    theoretical_quantity decimal(20, 10)      null comment '理论量',
    unit_id              bigint               not null comment '单位id',
    user_id              bigint               null comment '计划人id',
    create_by            varchar(64)          null,
    update_by            varchar(64)          null,
    create_time          datetime             null,
    update_time          datetime             null,
    is_deleted           tinyint(1) default 0 not null
) comment '配料计划批次表';

create table bmos_mes.bm_ingredient_weigh_batch_process
(
    id                          bigint               not null comment '物理主键'
        primary key,
    ingredient_weigh_process_id bigint               null comment '生产计划id',
    ingredient_plan_id          bigint               null comment '配料单id',
    storage_material_batch_id   bigint               null comment '暂存物料批次id',
    weigher_id                  varchar(100)         null comment '称量人id',
    re_checker_id               varchar(100)         null comment '复核人id',
    remark                      varchar(100)         null comment '备注',
    weigh_status                int                  null comment '称量状态',
    put_in_quantity             varchar(100)         null comment '已投物料量(基本单位量)',
    create_time                 datetime             null,
    update_time                 datetime             null,
    create_by                   varchar(100)         null,
    update_by                   varchar(100)         null,
    is_deleted                  tinyint(1) default 0 null
)
    comment '配料称量批次表';

create table bmos_mes.bm_ingredient_weigh_process
(
    id                 bigint               not null comment '物理主键'
        primary key,
    product_plan_id    bigint               null comment '生产计划id',
    procedure_step_id  bigint               null comment '工序步骤id',
    copy_version       int                  null comment '拷贝版本',
    ingredient_plan_id bigint               null comment '配料计划id',
    create_time        datetime             null,
    update_time        datetime             null,
    create_by          varchar(100)         null,
    update_by          varchar(100)         null,
    is_deleted         tinyint(1) default 0 null
)
    comment '配料称量流程表';

create table bmos_mes.bm_ingredient_weigh_record
(
    id                                bigint               not null comment '物理主键'
        primary key,
    ingredient_weigh_batch_process_id bigint               null comment '生产计划id',
    ingredient_plan_id                bigint               null comment '配料单id',
    storage_material_batch_id         bigint               null comment '暂存物料批次id',
    tare_weight                       decimal(18, 2)       null comment '皮重(基本单位量)',
    gross_weight                      decimal(18, 2)       null comment '毛重(基本单位量)',
    net_weight                        decimal(18, 2)       null comment '净重(基本单位量)',
    container_id                      bigint               null comment '容器id',
    material_position_id              bigint               null comment '货位id',
    weigh_type                        int                  null comment '称量方式',
    weigh_mode                        int                  null comment '称量模式',
    storage_material_id               bigint               null comment '物料件号',
    sign_status                       int                  null comment '签名状态',
    weigher_id                        varchar(100)         null comment '称量人id',
    re_checker_id                     varchar(100)         null comment '复核人id',
    remark                            varchar(100)         null comment '备注',
    create_time                       datetime             null,
    update_time                       datetime             null,
    create_by                         varchar(100)         null,
    update_by                         varchar(100)         null,
    is_deleted                        tinyint(1) default 0 null
)
    comment '配料称重记录';


SET FOREIGN_KEY_CHECKS = 1;
