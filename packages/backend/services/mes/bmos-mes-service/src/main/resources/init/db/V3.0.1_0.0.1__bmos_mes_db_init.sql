-- 配方物料表添加称量需求字段
alter table bm_product_formula_material
    add weigh_requirement_list_json text null comment '称量需求列表json' after oddment_tolerance_lower;


create table bm_weigh_ticket
(
    id                        bigint               not null comment '物理主键'
        primary key,
    ticket_no                 varchar(100)         null comment '工单编号',
    material_name             varchar(255)         null comment '物料名称',
    material_id               bigint               null comment '物料id',
    material_merge_code       varchar(100)         null comment '合并编码',
    storage_material_batch_id bigint               null comment '物料批次id',
    material_specification    varchar(255)         null comment '物料规格',
    weigh_centre_id           bigint               null comment '称量中心id',
    requirement_quantity      varchar(255)         null comment '需求量',
    unit_id                   bigint               null comment '单位id',
    plan_date                 date                 null comment '计划日期',
    status                    int                  null comment '工单状态 0 编辑中 1 已下发 2 已完成 3 已取消',
    ticket_weigh_status       tinyint              null comment '工单称量状态1-未称量 2-称量中 3-已完成',
    send_time                 datetime             null comment '下发时间',
    complete_time             datetime             null comment '工单称量完成时间',
    task_program_type         int                  null comment '规划类型 1 自动规划 2 手动规划',
    enough_complete_condition tinyint(1)           null comment '是否满足完成称量条件',
    create_time               datetime             null,
    update_time               datetime             null,
    create_by                 varchar(64)          null,
    update_by                 varchar(64)          null,
    is_deleted                tinyint(1) default 0 null
)
    comment '称量工单表';

    create table bm_weigh_ticket_requirement
    (
        id                        bigint               not null comment '物理主键'
            primary key,
        requirement_group_id      bigint               null comment '称量需求组id',
        material_id               bigint               null comment '物料id',
        formula_material_id       bigint               null comment '配方物料id',
        requirement_key           varchar(255)         null comment '称量需求key',
        storage_material_batch_id bigint               null comment '物料批次id',
        formula_quantity          varchar(255)         null comment '配方量',
        theoretical_quantity      varchar(255)         null comment '理论量',
        unit_id                   bigint               null comment '单位id',
        ticket_id                 bigint               null comment '工单id',
        program_time              datetime             null comment '规划时间',
        weigh_centre_id           bigint               null comment '称量中心id',
        complete_time             datetime             null comment '称量需求完成时间',
        complete_user             varchar(64)          null comment '称量需求完成人（最后一次称量满足当前需求量的称量人）',
        plan_date                 date                 null comment '计划日期',
        requirement_usage         varchar(255)         null comment '需求用途',
        requirement_status        int                  null comment '需求状态 0 未规划 1 未称量 2 称量中 3 已完成 4 已失效',
        weigh_status              int                  null comment '称量状态 0 未称量 1 称量中 2 已完成称量 3 已完成签名',
        create_time               datetime             null,
        update_time               datetime             null,
        create_by                 varchar(64)          null,
        update_by                 varchar(64)          null,
        is_deleted                tinyint(1) default 0 null
    )
        comment '称量工单需求表';

create table bm_weigh_ticket_requirement_group
(
    id              bigint               not null comment '物理主键'
        primary key,
    material_id     bigint               null comment '物料id',
    bom_version_id  bigint               null comment 'bom版本id',
    batch_no        varchar(100)         null comment '生产批号',
    weigh_centre_id bigint               null comment '称量中心id',
    plan_date       date                 null comment '计划日期',
    remark          varchar(500)         null comment '备注',
    release_status  int                  null comment '发布状态 0 编辑中 1 已确认 2 已完成 3 已取消',
    create_time     datetime             null,
    update_time     datetime             null,
    create_by       varchar(64)          null,
    update_by       varchar(64)          null,
    is_deleted      tinyint(1) default 0 null
)
    comment '称量工单需求组表';

create table bm_weigh_requirement_quality
(
    id                          bigint               not null comment '物理主键'
        primary key,
    create_time                 datetime             null comment '创建时间',
    update_time                 datetime             null comment '更新时间',
    create_by                   varchar(64)          null comment '创建人',
    update_by                   varchar(64)          null comment '更新人',
    is_deleted                  tinyint(1) default 0 null comment '逻辑删除',
    weigh_ticket_requirement_id bigint               null comment '称量工单需求ID',
    weigh_quality               varchar(100)         null comment '已称量的量(BigDecimal→varchar(100))',
    quality                     varchar(100)         null comment '当前称量需求内所有物料件的可用量之和(BigDecimal→varchar(100))',
    storage_material_count      bigint               null comment '当前称量需求内所有物料件的数量'
)
    comment '工单需求称量的量';

create table bm_weigh_requirement_record
(
    id                          bigint               not null comment '物理主键'
        primary key,
    create_time                 datetime             null comment '创建时间',
    update_time                 datetime             null comment '更新时间',
    create_by                   varchar(64)          null comment '创建人',
    update_by                   varchar(64)          null comment '更新人',
    is_deleted                  tinyint(1) default 0 null comment '逻辑删除',
    weigh_ticket_requirement_id bigint               null comment '称量工单需求id bm_weigh_ticket_requirement_id主键id',
    ticket_id                   bigint               null comment '称量工单id',
    net_weight                  varchar(100)         null comment '净重(BigDecimal→varchar(100))',
    tare_weight                 varchar(100)         null comment '皮重(BigDecimal→varchar(100))',
    gross_weight                varchar(100)         null comment '毛重(BigDecimal→varchar(100))',
    weigh_func                  int                  null comment '称量方式 1-手动称量 2-物料称量',
    weigh_type                  int                  null comment '称量类型 1-余料称量 2-正常称量',
    sign_status                 int                  null comment '签名状态 1-已签名 2-未签名',
    sign_user                   varchar(64)          null comment '签名人user_id',
    sign_remark                 varchar(255)         null comment '签名备注',
    sign_time                   datetime             null comment '签名时间',
    unit_id                     bigint               null comment '单位id',
    weigh_user_id               varchar(64)          null comment '称量人user_id',
    weigh_time                  datetime             null comment '称量时间',
    device_id                   bigint               null comment '容器id',
    device_name                 varchar(255)         null comment '容器名称',
    device_code                 varchar(255)         null comment '容器编码',
    storage_id                  bigint               null comment '暂存货位id',
    storage_material_id         bigint               null comment '物料件id',
    storage_material_no         varchar(64)          null comment '物料件号',
    storage_material_batch_id   bigint               null comment '物料批次id',
    storage_material_batch_no   varchar(64)          null comment '物料批号',
    product_material_id         bigint               null comment '生产的产品物料id'
)
    comment '称量需求的称量记录';

create table bm_weigh_storage_material_requirement_record
(
    id                          bigint               not null comment '物理主键'
        primary key,
    create_time                 datetime             null comment '创建时间',
    update_time                 datetime             null comment '更新时间',
    create_by                   varchar(64)          null comment '创建人',
    update_by                   varchar(64)          null comment '更新人',
    is_deleted                  tinyint(1) default 0 null comment '逻辑删除',
    weigh_ticket_requirement_id bigint               null comment '称量工单需求id bm_weigh_ticket_requirement_id主键id',
    storage_material_id         bigint               null comment '暂存货位id',
    consume_quantity            varchar(100)         null comment '当前物料件消耗的量(BigDecimal→varchar(100))'
)
    comment '物料件与称量需求绑定关系';

create table bm_weigh_ticket_quality
(
    id              bigint               not null comment '物理主键'
        primary key,
    create_time     datetime             null comment '创建时间',
    update_time     datetime             null comment '更新时间',
    create_by       varchar(64)          null comment '创建人',
    update_by       varchar(64)          null comment '更新人',
    is_deleted      tinyint(1) default 0 null comment '逻辑删除',
    weigh_ticket_id bigint               null comment '称量工单ID',
    weigh_quality   varchar(100)         null comment '已称量的量(BigDecimal→varchar(100))',
    quality         varchar(100)         null comment '添加的物料件的总量'
)
    comment '工单称量的量';

create table bm_weigh_ticket_user
(
    id              bigint               not null comment '物理主键'
        primary key,
    create_time     datetime             null comment '创建时间',
    update_time     datetime             null comment '更新时间',
    create_by       varchar(64)          null comment '创建人',
    update_by       varchar(64)          null comment '更新人',
    is_deleted      tinyint(1) default 0 null comment '逻辑删除',
    weigh_ticket_id bigint               null comment '称量工单id bm_weigh_ticket主键id',
    operator        varchar(64)          null comment '工单操作人id',
    sign_user       varchar(64)          null comment '签名人id（工单操作人变更时需要）',
    remark          varchar(255)         null comment '备注'
)
    comment '称量工单与操作人绑定关系DO';

