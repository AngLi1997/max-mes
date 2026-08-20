-- 称量中心分类表
create table if not exists bm_weigh_centre_category
(
    id          bigint comment '物理主键'
        primary key,
    parent_id   bigint               null comment '上级称量中心分类id',
    name        varchar(100)         null comment '称量中心分类名称',
    id_path     text                 null comment 'id路径',
    create_time datetime             null,
    update_time datetime             null,
    create_by   varchar(64)          null,
    update_by   varchar(64)          null,
    is_deleted  tinyint(1) default 0 null
) comment '称量中心分类表';

-- 称量中心表
create table if not exists bm_weigh_centre
(
    id          bigint comment '物理主键'
        primary key,
    category_id bigint               null comment '称量中心分类id',
    code        varchar(100)         null comment '称量中心编码',
    name        varchar(100)         null comment '称量中心名称',
    remark      varchar(200)         null comment '备注',
    enabled     boolean              null comment '启停',
    create_time datetime             null,
    update_time datetime             null,
    create_by   varchar(64)          null,
    update_by   varchar(64)          null,
    is_deleted  tinyint(1) default 0 null
) comment '称量中心表';

-- 称量中心工位关联表
create table if not exists bm_weigh_centre_station
(
    id              bigint comment '物理主键'
        primary key,
    weigh_centre_id bigint null comment '称量中心id',
    station_id      bigint null comment '工位id'
) comment '称量中心工位关联表';

-- 称量中心称量需求表
create table if not exists bm_weigh_requirement
(
    id                        bigint               not null comment '物理主键'
        primary key,
    weigh_requirement_key     varchar(100)         null comment '称量需求key(工步id_工步模型id（复用就是0))',
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
    requirement_status        int                  null comment '需求状态 0 未规划 1 未称量 2 称量中 3 已完成 4 已失效',
    weigh_requirement_task_id bigint               null comment '规划称量任务id',
    program_time              datetime             null comment '规划时间',
    weigh_status              int                  null comment '称量状态 0 未称量 1 称量中 2 已完成',
    storage_material_batch_id bigint               null comment '当前添加物料批次id',
    weigher_id                varchar(255)         null comment '称量人id',
    re_checker_id             bigint               null comment '复核人id',
    remark                    varchar(200)         null comment '备注',
    weigh_process             int                  null comment '称量阶段 1 物料 2 余料 3 已完成',
    create_time               datetime             null,
    update_time               datetime             null,
    create_by                 varchar(64)          null,
    update_by                 varchar(64)          null,
    is_deleted                tinyint(1) default 0 null
)
    comment '称量中心称量需求表';


-- 称量中心称量任务表
create table if not exists bm_weigh_task
(
    id                   bigint               not null comment '物理主键'
        primary key,
    task_no              varchar(100)         null comment '称量任务编号',
    material_id          bigint               null comment '物料id',
    unit_id              bigint               null comment '单位id',
    weigh_centre_id      bigint               null comment '称量中心id',
    requirement_quantity varchar(100)         null comment '需求量',
    execute_date         date                 null comment '执行时间',
    task_status          int                  null comment '任务状态 0 编辑 1 待下发 2 已下发 3 已执行',
    send_time            datetime             null comment '任务下发时间',
    finish_time          datetime             null comment '任务完成时间',
    task_program_type    int                  null comment '规划类型 1 自动规划 2 手动规划',
    process_time         datetime             null comment '规划时间',
    process_operator_id  varchar(100)         null comment '规划人id',
    pre_weigher_id       varchar(255)         null comment '称量人id',
    pre_re_checker_id    varchar(255)         null comment '复核人id',
    remark               varchar(200)         null comment '备注',
    create_time          datetime             null,
    update_time          datetime             null,
    create_by            varchar(64)          null,
    update_by            varchar(64)          null,
    is_deleted           tinyint(1) default 0 null
)
    comment '称量中心称量任务表';

create table if not exists bm_weigh_execute_consume_record
(
    id                        bigint               not null comment '物理主键'
        primary key,
    task_id                   bigint               null comment '称量任务id',
    requirement_id            bigint               null comment '称量需求id',
    product_plan_id           bigint               null comment '生产计划id',
    storage_material_batch_id bigint               null comment '称量消耗物料批次id',
    storage_material_id       bigint               null comment '称量消耗物料件id',
    storage_material_no       varchar(100)         null comment '称量消耗物料件编号',
    consume_quantity          varchar(100)         null comment '消耗量',
    unit_id                   bigint               null comment '单位id',
    consume_time              datetime             null comment '消耗时间',
    create_time               datetime             null,
    update_time               datetime             null,
    create_by                 varchar(64)          null,
    update_by                 varchar(64)          null,
    is_deleted                tinyint(1) default 0 null
) comment '称量执行添加物料记录表';


create table if not exists bm_weigh_execute_weigh_record
(
    id                        bigint               not null comment '物理主键'
        primary key,
    task_id                   bigint               null comment '任务id',
    requirement_id            bigint               null comment '需求id',
    product_plan_id           bigint               null comment '生产计划id',
    tare_weight               varchar(100)         null comment '皮重',
    gross_weight              varchar(100)         null comment '毛重',
    net_weight                varchar(100)         null comment '净重',
    unit_id                   bigint               null comment '单位id',
    storage_material_batch_id bigint               null comment '称量产出物料批次id',
    storage_material_batch_no varchar(100)         null comment '称量产出物料批次号',
    storage_material_id       bigint               null comment '称量产出物料件id',
    storage_material_no       varchar(100)         null comment '称量产出物料件编号',
    weigh_type                int                  null comment '称量方式',
    weigh_mode                int                  null comment '称量模式',
    sign_status               int                  null comment '签名状态',
    weigher_id                varchar(100)         null comment '称量人id',
    re_checker_id             varchar(100)         null comment '复核人id',
    remark                    varchar(200)         null comment '备注',
    weigh_time                datetime             null comment '称量时间',
    container_name            varchar(255)         null comment '容器名称',
    material_position_name    varchar(255)         null comment '货位名称',
    create_time               datetime             null,
    update_time               datetime             null,
    create_by                 varchar(64)          null,
    update_by                 varchar(64)          null,
    is_deleted                tinyint(1) default 0 null
) comment '称量执行称量记录表';

drop table if exists bm_business_component_instance;
create table if not exists bm_business_component_instance
(
    id                      bigint               not null comment '物理主键'
        primary key,
    product_plan_id         bigint               null comment '生产计划id(用于确定生产计划)',
    procedure_step_model_id bigint               null comment '工序步骤模型id(用于确定流程模型)',
    procedure_step_id       bigint               null comment '工序步骤id',
    copy_version            bigint               null comment '拷贝版本(默认0 用于确定移动端临时复制记录)',
    component_id            bigint               null comment '组件id(用于确定组件类型)',
    reuse                   tinyint(1)           null comment '是否复用',
    weigh_requirement_key   varchar(100)         null comment '称量需求key(工步id_工步模型id（复用就是0))',
    component_config_json   longtext             null comment '组件配置json',
    component_type          varchar(100)         null comment '组件类型',
    component_name          varchar(100)         null comment '组件名称',
    batch_no                varchar(100)         null comment '生产批号',
    create_time             datetime             null,
    update_time             datetime             null,
    create_by               varchar(64)          null,
    update_by               varchar(64)          null,
    is_deleted              tinyint(1) default 0 null
)
    comment '业务组件实例';
