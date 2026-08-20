alter table bm_execute_attachment
    add process_change_number int null comment '工艺换班次数' after procedure_step_id;

alter table bm_execute_attachment
    add procedure_change_number int null comment '工序换班次数' after process_change_number;

update bm_execute_attachment
set process_change_number = 0,
    procedure_change_number = 0;

alter table bm_operate_rule_version
    add upload_time varchar(64) null comment '文件上传时间' after url;

create table bm_inspect_config
(
    id               bigint               not null comment '主键'
        primary key,
    name             varchar(64)          not null comment '请验单名称',
    remark           varchar(255)         null comment '请验单的备注信息',
    enable           tinyint(1)           not null comment '是否启用',
    update_show_name varchar(64)          not null comment '最后修改人名称 username-logiNanme',
    create_by        varchar(64)          null,
    update_by        varchar(64)          null,
    create_time      datetime             null,
    update_time      datetime             null,
    is_deleted       tinyint(1) default 0 not null
)
    comment '请验单配置表' row_format = DYNAMIC;

create table bm_inspect_config_data
(
    id            bigint               not null comment '主键'
        primary key,
    config_id     bigint               not null comment '请验单配置表id bm_inspect_config表主键',
    code          varchar(64)          null comment '请验单数据code 内置数据code前端定义，若是字典数据code则为字典的value',
    show_name     varchar(64)          null comment '请验单数据展示名称',
    data_name     varchar(64)          not null comment '请验单数据名称',
    required      tinyint(1)           not null comment '是否必填',
    default_value varchar(255)         null comment '默认值',
    sort          int                  not null comment '排序 同一个config_id下的请验单数据在前端的显示顺序',
    create_by     varchar(64)          null,
    update_by     varchar(64)          null,
    create_time   datetime             null,
    update_time   datetime             null,
    is_deleted    tinyint(1) default 0 not null
)
    comment '请验单配置数据表' row_format = DYNAMIC;

create table bm_inspect_config_material
(
    config_id   bigint not null,
    material_id bigint not null,
    constraint uk_resource_dept
        unique (config_id, material_id) comment '请验单-物料唯一索引'
)
    comment '请验单与物料绑定关系表' row_format = DYNAMIC;

create table bm_inspect
(
    id                      bigint               not null comment '主键'
        primary key,
    inspect_no              varchar(64)          not null comment '请验单号',
    status                  tinyint              not null comment '请验状态 1-检验中 2-已完成 3-已退回',
    inspect_result          varchar(64)

        null comment '汇总检验结果',
    reason                  varchar(1024)        null comment '退回原因（请验单被lims退回后，所需要填写的退回原因）',
    inspector_id            varchar(64)          not null comment '请验人id',
    inspector               varchar(64)          not null comment '请验人用户名称-登陆名称',
    inspect_time            datetime             null comment '请验时间',
    procedure_model_id      bigint               not null comment '工序模型id (plan_id + procedure_model_id能够获取到当前工序所发起的请验单)',
    procedure_step_model_id bigint               null comment '工步模型id',
    process_change_number      int                  null comment '工艺换班次数',
    procedure_change_number    int                  null comment '工序换班次数',
    inspect_config_id       bigint               not null comment '请验单id (当前物料当时所绑定的请验单配置id)',
    formula_material_id     bigint               not null comment '请验的请验的配方物料id bm_product_formula_material表的主键id',
    material_id             bigint               null comment '请验的物料id',
    material_type           tinyint(1)           not null comment '请验的物料类型 bm_product_formula_material表的material_type',
    material_merge_code     varchar(255)         not null comment '请验的物料合并编码',
    material_name           varchar(255)         not null comment '请验的物料名称',
    material_batch_no       varchar(64)          null comment '请验的物料批号',
    plan_id                 bigint               not null comment '生产指令单id bm_product_plan表的主键id',
    product_name            varchar(64)          not null comment '产品名称  bm_prodcut_plan内的冗余内容',
    product_merge_code      varchar(64)          not null comment '产品合并编码 bm_prodcut_plan内的冗余内容',
    plan_no                 varchar(64)          not null comment '指令单编号 bm_prodcut_plan内的冗余内容',
    batch_no                varchar(64)          null comment '生产批号',
    batch_quantity          varchar(64)          not null comment '生产批量 bm_prodcut_plan内的冗余内容',
    unit_id                 bigint               not null comment '单位id bm_prodcut_plan内的冗余内容',
    create_by               varchar(64)          null,
    update_by               varchar(64)          null,
    create_time             datetime             null,
    update_time             datetime             null,
    is_deleted              tinyint(1) default 0 not null
)
    comment '请验单' row_format = DYNAMIC;



create table bm_inspect_info
(
    id                     bigint               not null comment '主键'
        primary key,
    inspect_id             bigint               not null comment '请验单主键id bm_inspect表主键',
    inspect_config_data_id bigint               not null comment '请验单配置数据id bm_inspect_config_data表主键',
    code                   varchar(64)          null comment '请验单数据code 内置数据code前端定义，若是字典数据code则为字典的value',
    show_name              varchar(64)          null comment '展示名称',
    data_name              varchar(64)          not null comment '请验单数据名称',
    required               tinyint(1)           not null comment '是否必填',
    value                  varchar(255)         null comment '所填的值',
    sort                   int                  not null comment '排序 请验详情时前端显示的排序',
    create_by              varchar(64)          null,
    update_by              varchar(64)          null,
    create_time            datetime             null,
    update_time            datetime             null,
    is_deleted             tinyint(1) default 0 not null
)
    comment '请验单信息表' row_format = DYNAMIC;

create table bm_inspect_result
(
    id                   bigint               not null comment '主键'
        primary key,
    inspect_id           bigint               not null comment '请验单主键id bm_inspect表主键',
    inspect_program_no   varchar(64)          null comment '检验项代码',
    inspect_dict_no      varchar(64)          null comment '字典对应的检验项目no',
    inspect_program_name varchar(64)          null comment '检验项名称',
    inspect_result       varchar(64)          null comment '检验项结果',
    inspect_conclusion   varchar(64)          null comment '检验结论',
    create_by            varchar(64)          null,
    update_by            varchar(64)          null,
    create_time          datetime             null,
    update_time          datetime             null,
    is_deleted           tinyint(1) default 0 not null
)
    comment '检验结论表' row_format = DYNAMIC;




