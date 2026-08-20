create table bm_material_trace_template
(
    id            bigint               not null comment '物理主键'
        primary key,
    template_name varchar(100)         null comment '模板名称',
    product_id    bigint               null comment '产品id',
    process_id    bigint               null comment '工艺id',
    enabled       tinyint              null comment '是否启用',
    material_tree text                 null comment '物料树 list json',
    create_time   datetime             null,
    update_time   datetime             null,
    create_by     varchar(64)          null,
    update_by     varchar(64)          null,
    is_deleted    tinyint(1) default 0 null
) comment '物料追溯模板信息';

create table bm_material_trace_template_procedure_step
(
    id                  bigint               not null comment '物理主键'
        primary key,
    relation_id         bigint               null comment '物料追溯物料关联id',
    template_id         bigint               null comment '物料追溯模板id',
    material_id         bigint               null comment '物料追溯物料id',
    process_id          bigint               null comment '工艺id',
    process_name        varchar(100)         null comment '工艺名称',
    procedure_id        bigint               null comment '工序id',
    procedure_name      varchar(100)         null comment '工序名称',
    procedure_step_id   bigint               null comment '工序步骤id',
    procedure_step_name varchar(100)         null comment '工序步骤名称',
    trace_type          int                  null comment '物料追溯类型 1 消耗 2 产出',
    create_time         datetime             null,
    update_time         datetime             null,
    create_by           varchar(64)          null,
    update_by           varchar(64)          null,
    is_deleted          tinyint(1) default 0 null
) comment '物料追溯物料关联工序步骤信息';

create table bm_material_trace_history
(
    id                        bigint               not null comment '物理主键'
        primary key,
    material_id               bigint               not null comment '物料id',
    material_name             varchar(255)         not null comment '物料名称',
    material_category_id      bigint               not null comment '物料分类 id',
    material_category_name    varchar(255)         not null comment '物料分类名称',
    material_category_type    tinyint              not null comment '物料分类类型 0 原辅包 1 中间品 2 产品',
    merge_code                varchar(255)         not null comment '合并编码',
    material_specification    varchar(255)         not null comment '物料规格',
    storage_material_id       bigint               not null comment '物料件 id',
    storage_material_no       varchar(255)         not null comment '物料件号',
    storage_material_batch_id bigint               not null comment '物料件批次 id',
    storage_material_batch_no varchar(255)         not null comment '物料件批次号',
    quantity                  varchar(255)         not null comment '物料量',
    unit_id                   bigint               not null comment '单位 id',
    unit_name                 varchar(255)         not null comment '单位名称',
    product_plan_id           bigint               null comment '生产计划 id',
    batch_no                  varchar(255)         null comment '生产批号',
    process_id                bigint               null comment '工艺 id',
    procedure_id              bigint               null comment '工序 id',
    procedure_step_id         bigint               null comment '工序步骤 id',
    operate_type              tinyint              not null comment '操作类型 1 生产投料 2 配料投入 3 配液投入 4 物料投入 5 中间品产出 6 配液产出',
    operate_user_id           varchar(255)         not null comment '操作人 id',
    operate_time              datetime             not null comment '操作时间',
    trace_type                tinyint              not null comment '物料追溯类型 1 消耗 2 产出',
    create_time               datetime             null,
    update_time               datetime             null,
    create_by                 varchar(64)          null,
    update_by                 varchar(64)          null,
    is_deleted                tinyint(1) default 0 null
) comment ='物料追溯历史表';

create index bm_material_trace_history_material_id_index
    on bm_material_trace_history (material_id);

create index bm_material_trace_history_procedure_step_id_index
    on bm_material_trace_history (procedure_step_id);

create index bm_material_trace_history_process_id_index
    on bm_material_trace_history (process_id);

create index bm_material_trace_history_product_plan_id_index
    on bm_material_trace_history (product_plan_id);

alter table bm_material_trace_history
    modify storage_material_id bigint null comment '物料件 id';

alter table bm_material_trace_history
    modify storage_material_no varchar(255) null comment '物料件号';

alter table bm_material_trace_history
    modify storage_material_batch_id bigint null comment '物料件批次 id';

alter table bm_material_trace_history
    modify storage_material_batch_no varchar(255) null comment '物料件批次号';

alter table bm_material_trace_history
    modify operate_type tinyint not null comment '操作类型 1 生产投料 2 配料投入 3 配液投入 4 物料投入 5 中间品产出 6 配液产出 7 成品产出';