use bmos_wms;
set foreign_key_checks = 0;

create table bmos_wms.bw_active
(
    active_code text null
)
    comment '激活码';

create table bmos_wms.bw_cargo
(
    id                   bigint               not null comment '物理主键'
        primary key,
    cargo_category_id    bigint               null comment '货品分类id',
    cargo_name           varchar(100)         null comment '货品名称',
    cargo_code           varchar(100)         null comment '货品编码',
    merge_code           varchar(100)         null comment '货品合并编码',
    specification        varchar(100)         null comment '规格',
    unit_id              bigint               null comment '单位id',
    is_member            tinyint(1)           null comment '是否是成员物料',
    sub_material_id      bigint               null comment '所属物料id',
    single_quantity      decimal(18, 2)       null comment '单件量',
    supplier             varchar(100)         null comment '供应商',
    producer             varchar(100)         null comment '生产商',
    remark               varchar(100)         null comment '备注',
    platform_material_id bigint               null comment '平台物料id',
    enable               tinyint(1)           null comment '是否启用',
    create_time          datetime             null,
    update_time          datetime             null,
    create_by            varchar(100)         null,
    update_by            varchar(100)         null,
    is_deleted           tinyint(1) default 0 null
)
    comment '货品信息表';

create table bmos_wms.bw_cargo_category
(
    id                        bigint               not null comment '物理主键'
        primary key,
    parent_id                 bigint               null comment '父级id',
    cargo_category_name       varchar(100)         null comment '货位分类名称',
    cargo_category_code       varchar(100)         null comment '货位分类编码',
    cargo_category_merge_code varchar(100)         null comment '货位合并编码',
    platform_category_id      bigint               null comment '平台分类id',
    create_time               datetime             null,
    update_time               datetime             null,
    create_by                 varchar(100)         null,
    update_by                 varchar(100)         null,
    is_deleted                tinyint(1) default 0 null
)
    comment '货品分类表';

create table bmos_wms.bw_cargo_log
(
    id                 bigint               not null comment '物理主键'
        primary key,
    operate_time       datetime             null comment '操作时间',
    operate_type       int                  null comment '操作类型',
    operate_info       varchar(100)         null comment '操作信息',
    operator_id        varchar(255)         null comment '操作人id',
    operator_name      varchar(100)         null comment '操作人名称',
    cargo_id           bigint               null comment '货品id',
    cargo_name         varchar(100)         null comment '货品名称',
    merge_code         varchar(255)         null comment '货品合并编码',
    inventory_batch_no varchar(100)         null comment '货品批号',
    inventory_no       varchar(100)         null comment '货品件编号',
    reserve_quantity   varchar(100)         null comment '预定量',
    available_quantity varchar(100)         null comment '可用量',
    unit_id            bigint               null comment '单位id',
    available          tinyint(1)           null comment '是否可用',
    effective_date     date                 null comment '有效期',
    product_name       varchar(100)         null comment '产品名称',
    product_merge_code varchar(100)         null comment '产品编码',
    product_batch_no   varchar(100)         null comment '生产批号',
    process_name       varchar(100)         null comment '工艺名称',
    pull_order_no      varchar(100)         null comment '领料单号',
    position           varchar(100)         null comment '货位名称',
    position_code      varchar(100)         null comment '货位编码',
    position_path      varchar(255)         null comment '所属位置',
    supplier           varchar(100)         null comment '供应商',
    producer           varchar(100)         null comment '生产商',
    factory_batch_no   varchar(100)         null comment '原厂批号',
    remark             varchar(100)         null comment '备注',
    validate_order_no  varchar(100)         null comment '请验单号',
    report_order_no    varchar(100)         null comment '报告单编号',
    license_order_no   varchar(100)         null comment '放行单编号',
    check_info         varchar(100)         null comment '检验信息',
    create_time        datetime             null,
    update_time        datetime             null,
    create_by          varchar(100)         null,
    update_by          varchar(100)         null,
    is_deleted         tinyint(1) default 0 null
)
    comment '货品日志表';

create table bmos_wms.bw_cargo_position
(
    id          bigint               not null comment '物理主键'
        primary key,
    storage_id  bigint               null comment '所属区域 的id',
    position    varchar(100)         null comment '暂存货位',
    code        varchar(100)         null comment '货位编码',
    id_path     varchar(100)         null comment '所属区域id路径',
    remark      varchar(200)         null comment '备注',
    enable      tinyint(1)           null comment '启停',
    create_time datetime             null,
    update_time datetime             null,
    create_by   varchar(100)         null,
    update_by   varchar(100)         null,
    is_deleted  tinyint(1) default 0 null
)
    comment '货品货位表';

create table bmos_wms.bw_inventory
(
    id                 bigint               not null comment '物理主键'
        primary key,
    cargo_id           bigint               null comment '货品id',
    inventory_batch_id bigint               null comment '货品批次id',
    position_id        bigint               null comment '货位id',
    no                 varchar(100)         null comment '物料件号',
    init_quantity      decimal(18, 2)       null comment '初始量',
    available_quantity decimal(18, 2)       null comment '可用量',
    consume_quantity   decimal(18, 2)       null comment '消耗量',
    reserve_quantity   decimal(18, 2)       null comment '预订量',
    unit_id            bigint               null comment '单位id',
    create_time        datetime             null,
    update_time        datetime             null,
    create_by          varchar(100)         null,
    update_by          varchar(100)         null,
    is_deleted         tinyint(1) default 0 null
)
    comment '货品件表';

create table bmos_wms.bw_inventory_batch
(
    id                   bigint               not null comment '物理主键'
        primary key,
    cargo_id             bigint               null comment '货品id',
    batch_no             varchar(100)         null comment '货品批号',
    factory_batch_no     varchar(100)         null comment '原厂批号',
    produce_date         date                 null comment '生产日期',
    expired_date         date                 null comment '有效日期',
    hydration            decimal(18, 2)       null comment '水分(%)',
    no_hydration_content decimal(18, 2)       null comment '无水含量(%)',
    unit_id              bigint               null comment '单位id',
    available            tinyint              null comment '是否可用',
    report_no            varchar(100)         null comment '报告单编号',
    licence_no           varchar(100)         null comment '放行单编号',
    create_time          datetime             null,
    update_time          datetime             null,
    create_by            varchar(100)         null,
    update_by            varchar(100)         null,
    is_deleted           tinyint(1) default 0 null
)
    comment '货品批次表';

create table bmos_wms.bw_inventory_reserve
(
    id                  bigint         not null comment 'id'
        primary key,
    inventory_batch_id  bigint         null comment '货品批次id',
    cargo_id            bigint         null comment '货品id',
    reserve_quantity    decimal(18, 2) null comment '预定数量',
    reserve_time        datetime       null comment '预定时间',
    requisition_plan_id bigint         null comment '领料计划单id'
)
    comment '预定信息表';

create table bmos_wms.bw_position_log
(
    id                 bigint               not null comment '物理主键'
        primary key,
    inventory_no       varchar(100)         null comment '货品件号',
    quantity           decimal(18, 2)       null comment '货品量',
    unit_id            bigint               null comment '单位id',
    operate_time       datetime             null comment '操作时间',
    operate_type       int                  null comment '操作类型',
    operate_info       varchar(100)         null comment '操作信息',
    operator_id        varchar(100)         null comment '操作人id',
    operator_name      varchar(100)         null comment '操作人名称',
    position_id        bigint               null comment '货位id',
    position           varchar(100)         null comment '货位名称',
    position_code      varchar(100)         null comment '货位编码',
    position_path      varchar(100)         null comment '所属位置',
    cargo_id           bigint               null comment '货品id',
    cargo_name         varchar(100)         null comment '货品名称',
    merge_code         varchar(255)         null comment '货品合并编码',
    inventory_batch_no varchar(100)         null comment '货品批号',
    product_name       varchar(100)         null comment '产品名称',
    product_merge_code varchar(100)         null comment '产品编码',
    product_batch_no   varchar(100)         null comment '生产批号',
    process_name       varchar(100)         null comment '工艺名称',
    pull_order_no      varchar(100)         null comment '领料单号',
    remark             varchar(200)         null comment '备注',
    create_time        datetime             null,
    update_time        datetime             null,
    create_by          varchar(100)         null,
    update_by          varchar(100)         null,
    is_deleted         tinyint(1) default 0 null
)
    comment '货位日志表';

create table bmos_wms.bw_resource_permission
(
    resource_id bigint null comment '资源id',
    dept_id     bigint null comment '部门id'
)
    comment '部门资源权限表';

create table bmos_wms.bw_send_out_order
(
    id                    bigint               not null comment '物理主键'
        primary key,
    requisition_plan_id   bigint               null comment '领料计划id',
    product_id            bigint               null comment '产品id',
    product_code          varchar(100)         null comment '产品编码',
    product_name          varchar(100)         null comment '产品名称',
    product_specification varchar(100)         null comment '产品规格',
    process_id            bigint               null comment '工艺id',
    process_name          varchar(100)         null comment '工艺名称',
    batch_no              varchar(100)         null comment '生产批号',
    pull_order_no         varchar(100)         null comment '领料单号',
    submitter_id          varchar(255)         null comment '计划人',
    submit_time           datetime             null comment '计划时间',
    send_time             datetime             null comment '发料时间',
    cancel_time           datetime             null comment '取消时间',
    send_order_type       int                  null comment '发料工单类型',
    send_order_status     int                  null comment '发料工单状态',
    create_time           datetime             null,
    update_time           datetime             null,
    create_by             varchar(100)         null,
    update_by             varchar(100)         null,
    is_deleted            tinyint(1) default 0 null
)
    comment '发料工单表';

create table bmos_wms.bw_send_out_order_item
(
    id                 bigint               not null comment '物理主键'
        primary key,
    send_order_id      bigint               null comment '发料工单id',
    send_order_type    int                  null comment '发料工单类型',
    cargo_id           bigint               null comment '货品id',
    inventory_batch_id bigint               null comment '批次id',
    reserve_quantity   decimal(18, 2)       null comment '预订量',
    unit_id            bigint               null comment '单位id',
    create_time        datetime             null,
    update_time        datetime             null,
    create_by          varchar(100)         null,
    update_by          varchar(100)         null,
    is_deleted         tinyint(1) default 0 null
)
    comment '发料单列表项表';

create table bmos_wms.bw_storage
(
    id          bigint               not null comment '物理主键'
        primary key,
    parent_id   bigint               null comment '上级区域id',
    name        varchar(100)         null comment '区域名称',
    level       int                  null comment '层级',
    create_time datetime             null,
    update_time datetime             null,
    create_by   varchar(100)         null,
    update_by   varchar(100)         null,
    is_deleted  tinyint(1) default 0 null
)
    comment '存储区域表';

set foreign_key_checks = 1;