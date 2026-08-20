create table bm_preparation_input_component_instance
(
    id                      bigint               not null
        primary key,
    product_plan_id         bigint               null comment '生产计划id',
    procedure_step_model_id bigint               null comment '工序步骤模型id',
    component_id            bigint               null comment '组件id',
    copy_version            bigint               null comment '复制版本',
    reuse                   tinyint(1)           null comment '是否复用',
    preparation_plan_id     bigint               null comment '绑定的配液单id',
    record_item_id          bigint               null comment '记录项id',
    record_version_id       bigint               null comment '记录项版本id',
    complete                tinyint(1)           null comment '是否完成配液投入',
    create_by               varchar(64)          null,
    update_by               varchar(64)          null,
    create_time             datetime             null,
    update_time             datetime             null,
    is_deleted              tinyint(1) default 0 not null
)
    comment '配液投入实例';

create table bm_preparation_input_record
(
    id                        bigint               not null comment '物理主键'
        primary key,
    preparation_plan_id       bigint               null comment '配料单id',
    storage_material_batch_id bigint               null comment '暂存物料批次id',
    storage_material_batch_no varchar(100)         null comment '暂存物料批次编号',
    storage_material_id       bigint               null comment '物料件id',
    storage_material_no       varchar(100)         null comment '物料件编号',
    formula_material_id       bigint               null comment '配方物料id',
    quantity                  varchar(255)         null comment '投料量',
    unit_id                   bigint               null comment '投料单位',
    device_id                 bigint               null comment '设备id',
    device_name               varchar(128)         null comment '设备名称',
    device_code               varchar(64)          null comment '设备编码',
    sort                      int                  null comment '投入顺序',
    importer_id               varchar(100)         null comment '投料人id',
    remark                    varchar(100)         null comment '备注',
    input_time                datetime             null comment '投料时间',
    sign_status               tinyint              null comment '签名状态',
    component_instance_id     bigint               null comment '配液投入组件实例id',
    create_time               datetime             null,
    update_time               datetime             null,
    create_by                 varchar(100)         null,
    update_by                 varchar(100)         null,
    is_deleted                tinyint(1) default 0 null
)
    comment '配液投入记录表';

create table bm_preparation_produce_progress
(
    id                      bigint               not null comment '物理主键'
        primary key,
    product_plan_id         bigint               null comment '生产计划id',
    procedure_step_model_id bigint               null comment '工序步骤模型id',
    copy_version            int                  null comment '拷贝版本',
    component_id            bigint               null comment '组件id',
    reuse                   tinyint(1)           null comment '是否复用',
    record_item_id          bigint               null comment '记录项id',
    record_version_id       bigint               null comment '记录项版本id',
    preparation_plan_id     bigint               null comment '配液单id',
    producer_id             varchar(100)         null comment '产出人id',
    re_checker_id           varchar(100)         null comment '复核人id',
    remark                  varchar(100)         null comment '备注',
    formula_material_id     bigint               null comment '配方物料id',
    material_batch_id       bigint               null comment '物料批次id',
    material_batch_no       varchar(100)         null comment '物料批次编号',
    expired_date            date                 null comment '有效期',
    create_time             datetime             null,
    update_time             datetime             null,
    create_by               varchar(100)         null,
    update_by               varchar(100)         null,
    is_deleted              tinyint(1) default 0 null
)
    comment '配液产出称量流程表';

create table bm_preparation_produce_record
(
    id                            bigint               not null comment '物理主键'
        primary key,
    procedure_produce_progress_id bigint               null comment '配液产出称量流程id',
    storage_material_id           bigint               null comment '物料件id',
    storage_material_no           varchar(100)         null comment '物料件号',
    storage_material_batch_id     bigint               null comment '物料批次id',
    storage_material_batch_no     varchar(100)         null comment '物料批次编号',
    tare_weight                   varchar(255)         null comment '皮重',
    gross_weight                  varchar(255)         null comment '毛重',
    net_weight                    varchar(255)         null comment '净重',
    unit_id                       bigint               null comment '单位id',
    container_id                  bigint               null comment '容器id',
    container_code                varchar(100)         null comment '容易编码',
    container_name                varchar(100)         null comment '容器名称',
    weigh_mode                    int                  null comment '称量模式 1-配料称量 2-手动称量',
    sort                          int                  null comment '产出排序',
    sign_time                     datetime             null comment '签名时间',
    sign_status                   int                  null comment '签名状态 0-未签名 1-已签名 2-已作废',
    material_position_id          bigint               null comment '货位id',
    producer_id                   varchar(100)         null comment '产出人id',
    re_checker_id                 varchar(100)         null comment '复核人id',
    produce_time                  datetime             null comment '产出时间',
    create_time                   datetime             null,
    update_time                   datetime             null,
    create_by                     varchar(100)         null,
    update_by                     varchar(100)         null,
    is_deleted                    tinyint(1) default 0 null
)
    comment '配液产出称量记录表';