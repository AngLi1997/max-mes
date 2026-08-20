create table audit_de_message
(
    id int auto_increment
        primary key,
    deployment_id varchar(64) null,
    message_key varchar(64) null,
    element_type varchar(64) null,
    element_key varchar(64) null
)
    collate=utf8mb4_general_ci;

create table audit_deployment
(
    id int auto_increment
        primary key,
    rev int null,
    version int null,
    name varchar(100) null,
    business_key varchar(64) null,
    category varchar(32) null,
    deployment_id varchar(64) null,
    deployment_version_id varchar(64) null,
    remark varchar(255) null,
    meta_info longtext collate utf8mb4_bin null,
    element_info longtext collate utf8mb4_bin null,
    deploy_by varchar(64) null,
    deploy_time datetime null,
    deploy_status tinyint(1) null,
    create_by varchar(64) null,
    create_time datetime null,
    update_by varchar(64) null,
    update_time datetime null
)
    comment '流程部署表' collate=utf8mb4_general_ci;

create table audit_execution_instance
(
    id int auto_increment
        primary key,
    element_key varchar(64) null,
    element_name varchar(255) null,
    element_type varchar(32) null,
    execution_id varchar(64) null,
    process_instance_id varchar(64) null,
    deployment_version_id varchar(64) null,
    state tinyint null,
    start_time datetime null,
    start_by varchar(64) null,
    end_time datetime null,
    end_by varchar(64) null,
    delete_reason varchar(64) null,
    remark varchar(255) null
)
    comment '每个节点的流程实例表' collate=utf8mb4_general_ci;

create table audit_hi_execution
(
    id int auto_increment
        primary key,
    element_name varchar(64) null,
    element_key varchar(64) null,
    element_type varchar(64) null,
    execution_id varchar(64) null,
    process_instance_id varchar(64) null,
    deployment_version_id varchar(64) null,
    state tinyint null,
    start_time datetime null,
    end_time datetime null,
    start_by varchar(64) null,
    end_by varchar(64) null,
    delete_reason varchar(255) null,
    remark varchar(64) null
)
    collate=utf8mb4_general_ci;

create table audit_hi_process_instance
(
    id int auto_increment
        primary key,
    name varchar(100) null,
    deployment_id varchar(64) null,
    deployment_version_id varchar(64) null,
    process_instance_id varchar(64) null,
    super_process_instance_id varchar(64) null,
    root_process_instance_id varchar(64) null,
    business_key varchar(64) null,
    category varchar(64) null,
    start_by varchar(64) null,
    start_time datetime null,
    end_time datetime null,
    process_state tinyint null,
    ext_field varchar(255) null
)
    collate=utf8mb4_general_ci;

create table audit_hi_task_instance
(
    id int auto_increment
        primary key,
    task_id varchar(64) null,
    execution_id varchar(64) null,
    process_instance_id varchar(64) null,
    deployment_version_id varchar(64) null,
    element_type varchar(16) null,
    element_key varchar(64) null,
    element_name varchar(64) null,
    owner varchar(64) null,
    assignee varchar(64) null,
    assignee_type varchar(16) null,
    state tinyint null,
    start_time datetime null,
    end_time datetime null,
    remark varchar(255) null,
    comment varchar(255) null,
    complete_by varchar(64) null,
    delete_reason varchar(64) null
)
    collate=utf8mb4_general_ci;

create table audit_hi_variable
(
    id int auto_increment
        primary key,
    name varchar(64) null,
    type varchar(64) null,
    category varchar(64) null,
    value varchar(2000) null,
    process_instance_id varchar(64) null,
    execution_id varchar(64) null,
    create_time datetime null,
    update_time datetime null,
    create_by varchar(64) null,
    update_by varchar(64) null
)
    collate=utf8mb4_general_ci;

create table audit_hi_variable_execution
(
    id bigint auto_increment
        primary key,
    var_name varchar(64) null,
    process_instance_id varchar(64) null,
    super_process_instance_id varchar(64) null,
    execution_id varchar(64) null,
    root_process_instance_id varchar(64) null
)
    collate=utf8mb4_general_ci;

create table audit_job_instance
(
    id int auto_increment
        primary key,
    type varchar(64) null,
    biz_id varchar(64) null,
    next_triggered_time bigint null,
    triggered_count int null,
    create_time datetime null,
    update_time datetime null,
    status tinyint null
)
    collate=utf8mb4_general_ci;

create table audit_process_instance
(
    id int auto_increment
        primary key,
    name varchar(100) null,
    deployment_id varchar(64) null,
    deployment_version_id varchar(64) null,
    process_instance_id varchar(64) null,
    category varchar(64) null,
    super_process_instance_id varchar(64) null,
    business_key varchar(64) null,
    root_process_instance_id varchar(64) null,
    start_by varchar(64) null,
    start_time datetime null,
    end_time datetime null,
    process_state tinyint null,
    ext_field varchar(255) null
)
    comment '整个流程实例表' collate=utf8mb4_general_ci;

create table audit_re_variable_execution
(
    id bigint auto_increment
        primary key,
    var_name varchar(64) null,
    process_instance_id varchar(64) null,
    super_process_instance_id varchar(64) null,
    execution_id varchar(64) null,
    root_process_instance_id varchar(64) null
)
    collate=utf8mb4_general_ci;

create table audit_ru_variable
(
    id int auto_increment
        primary key,
    name varchar(64) null,
    type varchar(64) null,
    category varchar(64) null,
    value varchar(2000) null,
    process_instance_id varchar(64) null,
    execution_id varchar(64) null,
    create_time datetime null,
    update_time datetime null,
    create_by varchar(64) null,
    update_by varchar(64) null
)
    collate=utf8mb4_general_ci;

create table audit_task_instance
(
    id int auto_increment
        primary key,
    task_id varchar(64) not null,
    execution_id varchar(64) not null,
    process_instance_id varchar(64) not null,
    deployment_version_id varchar(64) not null,
    element_type varchar(16) null,
    element_key varchar(64) null,
    element_name varchar(64) null,
    owner varchar(64) null,
    assignee varchar(64) not null,
    assignee_type varchar(16) not null,
    state tinyint not null,
    start_time datetime null,
    end_time datetime null,
    comment varchar(255) null,
    remark varchar(255) null,
    complete_by varchar(64) null,
    delete_reason varchar(64) null
)
    collate=utf8mb4_general_ci;

create index idx_assignee_assigness_type
	on audit_task_instance (assignee, assignee_type);

create index idx_execution_id
	on audit_task_instance (execution_id);

create index idx_process_instance_id
	on audit_task_instance (process_instance_id);

create index idx_task_id
	on audit_task_instance (task_id);

create table bp_active
(
    id int unsigned auto_increment
        primary key,
    active_code text not null
);


create table lm_document_config
(
    id bigint not null
        primary key,
    name varchar(255) not null comment '名称',
    remark varchar(255) null comment '备注信息',
    status int not null comment '启停状态',
    create_by varchar(64) charset utf8mb4 null comment '创建人',
    update_by varchar(64) charset utf8mb4 null comment '修改人',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    is_deleted tinyint(1) default 0 not null comment '是否删除 0-否 1-是'
)
    comment '请验单配置' collate=utf8mb4_general_ci;

create table lm_document_config_field
(
    id bigint not null
        primary key,
    config_id bigint not null comment '请验单配置表主键id',
    show_name varchar(255) not null comment '请验单展示数据名称',
    code varchar(255) not null comment '请验单数据code 内置数据code前端定义，若是字典数据code则为字典的value',
    data_name varchar(255) not null comment '请验单数据名称',
    required int null comment '是否必填',
    default_value varchar(255) null comment '默认值',
    sort int null comment '排序 同一个config_id下的请验单数据在前端的显示顺序',
    create_by varchar(64) charset utf8mb4 null comment '创建人',
    update_by varchar(64) charset utf8mb4 null comment '修改人',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    is_deleted tinyint(1) default 0 not null comment '是否删除 0-否 1-是'
)
    comment '请验单配置数据' collate=utf8mb4_general_ci;

create table lm_document_material
(
    config_id bigint not null comment '请验单配置id',
    product_id bigint not null comment '检品id'
)
    comment '请验单配置检品绑定表' collate=utf8mb4_general_ci;

create table lm_flow_audit
(
    id bigint not null comment '主键id'
        primary key,
    code varchar(64) not null comment '流程编码',
    name varchar(64) not null comment '流程名称',
    category_code varchar(64) not null comment '分类code',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    create_by varchar(64) charset utf8mb3 null comment '创建人',
    update_by varchar(64) charset utf8mb3 null comment '修改人',
    is_deleted tinyint default 0 null comment '是否删除',
    constraint index_name
        unique (name)
);

create table lm_flow_audit_category
(
    id bigint not null comment '主键id'
        primary key,
    name varchar(64) null comment '分类名称',
    code varchar(64) null comment '分类编码',
    parent_id bigint null comment '上级id',
    create_by varchar(64) null,
    update_by varchar(64) null,
    create_time datetime null,
    update_time datetime null,
    is_deleted tinyint(1) default 0 not null,
    tree_code varchar(255) null comment '层级code',
    tree_name varchar(255) null comment '层级名称'
)
    comment '流程配置分类表';

create table lm_flow_audit_message
(
    id bigint not null
        primary key,
    node_id varchar(64) not null,
    user_id varchar(64) not null,
    message_type varchar(16) not null,
    deployment_id varchar(64) not null,
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    create_by varchar(64) charset utf8mb3 null comment '创建人',
    update_by varchar(64) charset utf8mb3 null comment '修改人',
    is_deleted tinyint default 0 null comment '是否删除'
);

create table lm_flow_audit_operation_log
(
    id bigint not null
        primary key,
    business_id bigint not null comment '业务数据id',
    module varchar(64) not null comment '业务模块',
    operation_type varchar(32) not null comment '操作类型',
    remark varchar(255) null comment '备注',
    create_by varchar(64) null,
    update_by varchar(64) null,
    create_time datetime null,
    update_time datetime null,
    is_deleted tinyint(1) default 0 not null,
    node_name varchar(64) null comment '审批节点名称',
    comment varchar(255) null comment '审批节点名称',
    detail varchar(5000) null comment '历史详情'
);

create table lm_flow_audit_process
(
    process_id bigint not null comment '方案id 对应lm_process的主键id',
    code varchar(64) not null comment '流程编码 对应lm_flow_audit的code字段',
    category_code varchar(64) not null comment '对应的分类code ',
    primary key (category_code, process_id)
)
    comment '流程工艺绑定关系表';

create table lm_flow_audit_user
(
    id bigint not null comment '主键id'
        primary key,
    deployment_id varchar(64) not null comment '流程定义id',
    assignee bigint not null comment '处理人',
    assignee_type varchar(64) not null comment '处理人类型',
    node_id varchar(64) not null comment '节点key',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    create_by varchar(64) charset utf8mb3 null comment '创建人',
    update_by varchar(64) charset utf8mb3 null comment '修改人',
    is_deleted tinyint default 0 null comment '是否删除'
);

create table lm_flow_audit_version
(
    id bigint not null comment '主键id'
        primary key,
    flow_audit_id bigint not null comment '管理表id',
    history_version varchar(100) null comment '引用版本',
    version varchar(100) not null comment '版本号',
    state tinyint default 1 not null comment '状态，1：设计中；2：启用中；3：历史',
    remark varchar(64) null comment '备注',
    deployment_id varchar(64) null comment '流程部署id',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    create_by varchar(64) charset utf8mb3 null comment '创建人',
    update_by varchar(64) charset utf8mb3 null comment '修改人',
    is_deleted tinyint default 0 null comment '是否删除'
);

create table lm_inspect_item
(
    id bigint not null
        primary key,
    code varchar(64) not null comment '检验项目编码',
    name varchar(64) not null comment '检验项目名称',
    remark varchar(255) null comment '检验项目备注',
    create_by varchar(64) null comment '创建人',
    update_by varchar(64) null comment '修改人',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    is_deleted tinyint(1) default 0 not null comment '是否删除 0-否 1-是'
)
    comment '检验项目';

create table lm_inspect_material
(
    id bigint not null
        primary key,
    category_id bigint not null comment '物料分类id',
    principal_material_id bigint null comment '所属物料id',
    name varchar(64) not null comment '物料名称',
    code varchar(64) not null comment '编码',
    specification varchar(32) not null comment '规格',
    is_sub_material tinyint(1) not null comment '是否是成员物料',
    status tinyint(1) default 0 not null comment '启停状态 0-关闭 1-启用',
    merge_code varchar(60) not null comment '合并编码:分类合并编码+自身编码',
    description varchar(255) null comment '描述',
    package_count int default 0 not null comment '检品所配实验包数量',
    unit_id bigint not null comment '平台单位id',
    extend_unit_id bigint null comment '扩展单位id',
    platform_material_id bigint not null comment '平台物料关联id',
    expand_info text null comment '拓展信息 json字符串',
    remark varchar(255) null comment '备注',
    create_by varchar(64) null comment '创建人',
    update_by varchar(64) null comment '修改人',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    is_deleted tinyint(1) default 0 not null comment '是否删除 0-否 1-是',
    constraint uk_code
        unique (code)
)
    comment '检验物料-检品';

create index idx_category_id
	on lm_inspect_material (category_id);

create index idx_platform_material_id
	on lm_inspect_material (platform_material_id);

create index idx_principal_material_id
	on lm_inspect_material (principal_material_id);

create table lm_inspect_material_category
(
    id bigint not null
        primary key,
    parent_id bigint default 0 null comment '父级id，默认0',
    code varchar(64) not null comment '编码',
    name varchar(64) not null comment '名称',
    merge_code varchar(60) not null comment '合并编码:父级合并编码+自身编码',
    category_type int null comment '业务分类(专属于lims 现阶段暂无)',
    platform_category_id bigint null comment '平台物料分类id',
    create_by varchar(64) null comment '创建人',
    update_by varchar(64) null comment '修改人',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    is_deleted tinyint(1) default 0 not null comment '是否删除 0-否 1-是'
)
    comment '检品分类';

create index idx_code
	on lm_inspect_material_category (code);

create index idx_parent_id
	on lm_inspect_material_category (parent_id);

create table lm_inspect_material_field
(
    id bigint not null
        primary key,
    field_type varchar(64) null comment '字典类型',
    field_type_name varchar(255) null comment '字典类型名称',
    field varchar(64) null comment '字段',
    field_name varchar(255) null comment '字段名称',
    field_value varchar(64) null comment '字段值',
    material_id bigint null comment '检品id',
    create_by varchar(64) null,
    update_by varchar(64) null,
    create_time datetime null,
    update_time datetime null,
    is_deleted tinyint(1) default 0 not null
)
    collate=utf8mb4_general_ci;

create table lm_inspect_package
(
    id bigint not null
        primary key,
    code varchar(64) not null comment '实验包编码',
    name varchar(64) not null comment '实验包名称',
    remark varchar(255) null comment '实验包描述',
    create_by varchar(64) null comment '创建人',
    update_by varchar(64) null comment '修改人',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    is_deleted tinyint(1) default 0 not null comment '是否删除 0-否 1-是'
)
    comment '实验包';

create table lm_inspect_parameter
(
    id bigint not null
        primary key,
    code varchar(64) not null comment '分析项编码',
    name varchar(64) not null comment '分析项名称',
    standard varchar(255) null comment '标准规定模版',
    create_by varchar(64) null comment '创建人',
    update_by varchar(64) null comment '修改人',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    is_deleted tinyint(1) default 0 not null comment '是否删除 0-否 1-是',
    constraint uk_code
        unique (code, is_deleted) comment '分析项编码唯一索引'
)
    comment '分析项表';

create table lm_inspect_parameter_data_point
(
    id bigint not null
        primary key,
    parameter_id bigint not null comment '分析项id',
    name varchar(64) not null comment '数据点名称',
    result_type varchar(20) not null comment '数据点类型',
    standard varchar(255) null comment '标准规定',
    report_display tinyint(1) default 1 not null comment '是否报告显示 0-否 1-是',
    create_by varchar(64) null comment '创建人',
    update_by varchar(64) null comment '修改人',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    is_deleted tinyint(1) default 0 not null comment '是否删除 0-否 1-是',
    constraint uk_parameter_name
        unique (parameter_id, name, is_deleted) comment '分析项id和数据点名称唯一索引'
)
    comment '分析项数据点表';

create table lm_inspect_parameter_option
(
    id bigint not null
        primary key,
    data_point_id bigint not null comment '数据点id',
    option_value varchar(255) not null comment '选项值',
    create_by varchar(64) null comment '创建人',
    update_by varchar(64) null comment '修改人',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    is_deleted tinyint(1) default 0 not null comment '是否删除 0-否 1-是',
    constraint uk_data_point_value
        unique (data_point_id, option_value, is_deleted) comment '数据点id和选项值唯一索引'
)
    comment '数据点选项表';

create table lm_inspect_parameter_trend
(
    id bigint not null
        primary key,
    data_point_id bigint not null comment '数据点id',
    range_name varchar(64) not null comment '范围名称',
    min_value decimal(10,2) null comment '最小值',
    min_operator varchar(20) null comment '最小值比较运算符',
    max_value decimal(10,2) null comment '最大值',
    max_operator varchar(20) null comment '最大值比较运算符',
    create_by varchar(64) null comment '创建人',
    update_by varchar(64) null comment '修改人',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    is_deleted tinyint(1) default 0 not null comment '是否删除 0-否 1-是',
    constraint uk_data_point_range_name
        unique (data_point_id, range_name, is_deleted) comment '数据点id和范围名称唯一索引'
)
    comment '数据点趋势线表';

create table lm_inspection_entry_history
(
    id bigint auto_increment comment '主键ID'
        primary key,
    entry_record_id bigint not null comment '关联的录入记录ID',
    task_id bigint not null comment '任务ID(冗余)',
    inspection_order_id bigint null comment '检验单ID(冗余)',
    inspection_order_no varchar(100) null comment '检验单号(冗余)',
    data_point_id bigint null comment '数据点ID(冗余)',
    data_point_config_id bigint null comment '数据点配置ID',
    scheme_id bigint null comment '方案ID(冗余)',
    scheme_version_id bigint null comment '方案版本ID(冗余)',
    package_id bigint null comment '方案实验包ID(冗余)',
    item_config_id bigint null comment '方案检验项目配置ID(冗余)',
    parameter_config_id bigint null comment '方案分析项配置ID(冗余)',
    inspect_item_id bigint null comment '检验项目ID(冗余)',
    inspect_item_code varchar(100) null comment '检验项目编码(冗余)',
    parameter_id bigint null comment '分析项ID(冗余)',
    parameter_code varchar(100) null comment '分析项编码(冗余)',
    data_point_name varchar(200) not null comment '数据点名称(冗余)',
    point_type varchar(20) null comment '数据点类型(冗余)',
    old_value_text text null comment '旧文本/选项值',
    old_value_number decimal(20,6) null comment '旧数值',
    new_value_text text null comment '新文本/选项值',
    new_value_number decimal(20,6) null comment '新数值',
    change_reason varchar(500) null comment '变更原因',
    operator_id varchar(64) null comment '操作人ID',
    operator_name varchar(100) null comment '操作人姓名',
    operate_time datetime default (CURRENT_TIMESTAMP) not null comment '操作时间',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    create_by varchar(50) not null comment '创建人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是',
    update_time datetime null,
    update_by varchar(255) null
)
    comment '检验数据点录入历史表' collate=utf8mb4_unicode_ci;

create index idx_data_point
	on lm_inspection_entry_history (data_point_id);

create index idx_entry_record_id
	on lm_inspection_entry_history (entry_record_id);

create index idx_operate_time
	on lm_inspection_entry_history (operate_time);

create index idx_scheme_data_point
	on lm_inspection_entry_history (data_point_config_id);

create index idx_task
	on lm_inspection_entry_history (task_id);

create table lm_inspection_entry_record
(
    id bigint auto_increment comment '主键ID'
        primary key,
    inspection_order_id bigint not null comment '检验单ID',
    inspection_order_no varchar(100) null comment '检验单号(冗余)',
    task_id bigint not null comment '分析项任务ID',
    scheme_id bigint null comment '方案ID(冗余)',
    scheme_version_id bigint null comment '方案版本ID(冗余)',
    package_id bigint null comment '方案实验包配置ID(冗余)',
    item_config_id bigint null comment '方案检验项目配置ID(冗余)',
    parameter_config_id bigint null comment '方案分析项配置ID(冗余)',
    data_point_config_id bigint not null comment '数据点配置ID',
    inspect_item_id bigint not null comment '检验项目ID(冗余)',
    inspect_item_code varchar(100) null comment '检验项目编码(冗余)',
    parameter_id bigint not null comment '分析项ID(冗余)',
    parameter_code varchar(100) null comment '分析项编码(冗余)',
    data_point_id bigint null comment '数据点ID',
    data_point_name varchar(200) not null comment '数据点名称(冗余)',
    point_type varchar(20) not null comment '数据点类型：NUMBER/TEXT/OPTION',
    value_text text null comment '文本值/选项值',
    value_number decimal(20,6) null comment '数值型结果',
    test_time datetime null comment '检验时间',
    operator_id varchar(64) null comment '录入人ID',
    operator_name varchar(100) null comment '录入人姓名',
    is_abnormal tinyint(1) default 0 null comment '是否判定异常(冗余，便于筛选)',
    remark varchar(500) null comment '备注',
    modify_reason varchar(512) null comment '修改原因',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    update_time datetime default (CURRENT_TIMESTAMP) not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by varchar(50) not null comment '创建人',
    update_by varchar(50) not null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是',
    constraint uk_task_scheme_data_point
        unique (task_id, data_point_config_id)
)
    comment '检验数据点录入记录表' collate=utf8mb4_unicode_ci;

create index idx_inspect_item
	on lm_inspection_entry_record (inspect_item_id);

create index idx_is_abnormal
	on lm_inspection_entry_record (is_abnormal);

create index idx_item_config
	on lm_inspection_entry_record (item_config_id);

create index idx_order
	on lm_inspection_entry_record (inspection_order_id);

create index idx_package_config
	on lm_inspection_entry_record (package_id);

create index idx_parameter
	on lm_inspection_entry_record (parameter_id);

create index idx_parameter_config
	on lm_inspection_entry_record (parameter_config_id);

create index idx_scheme_data_point
	on lm_inspection_entry_record (data_point_config_id);

create index idx_scheme_version
	on lm_inspection_entry_record (scheme_version_id);

create index idx_task
	on lm_inspection_entry_record (task_id);

create index idx_test_time
	on lm_inspection_entry_record (test_time);

create table lm_inspection_order
(
    id bigint auto_increment comment '主键ID'
        primary key,
    order_no varchar(50) not null comment '检验单号',
    material_id bigint not null comment '检品ID',
    scheme_version_id bigint not null comment '检验方案版本ID',
    order_status varchar(50) default 'pending_confirm' not null comment '单据状态：待确认、已确认、已终止（请验阶段）',
    batch_no varchar(100) null comment '批次号',
    production_date date null comment '生产日期',
    template_id bigint null comment '请验单模板ID',
    remark text null comment '备注',
    terminate_reason varchar(500) null comment '终止原因',
    terminate_time datetime null comment '终止时间',
    sample_audit_time datetime null comment '样品审核完成时间',
    sample_audit_process_instance_id varchar(64) null comment '样品审核流程实例ID',
    finished tinyint(1) default 0 null comment '请验是否完成',
    finished_time datetime null comment '请验完成时间',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    update_time datetime default (CURRENT_TIMESTAMP) not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by varchar(50) not null comment '创建人',
    update_by varchar(50) not null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是',
    constraint uk_order_no
        unique (order_no)
)
    comment '检验单表' collate=utf8mb4_unicode_ci;

create index idx_create_time
	on lm_inspection_order (create_time);

create index idx_material_id
	on lm_inspection_order (material_id);

create index idx_order_status
	on lm_inspection_order (order_status);

create index idx_sample_audit_process_instance_id
	on lm_inspection_order (sample_audit_process_instance_id);

create index idx_scheme_version_id
	on lm_inspection_order (scheme_version_id);

create table lm_inspection_order_custom_field
(
    id bigint auto_increment comment '主键ID'
        primary key,
    inspection_order_id bigint not null comment '检验单ID',
    field_code varchar(100) not null comment '字段代码',
    field_name varchar(100) not null comment '字段显示名称',
    field_value text null comment '字段值',
    required tinyint(1) default 0 null comment '是否必填：0-否，1-是',
    sort int default 0 null comment '排序',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    update_time datetime default (CURRENT_TIMESTAMP) not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by varchar(50) not null comment '创建人',
    update_by varchar(50) not null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是',
    constraint uk_order_field
        unique (inspection_order_id, field_code),
    constraint fk_custom_field_inspection_order
        foreign key (inspection_order_id) references lm_inspection_order (id)
            on delete cascade
)
    comment '检验单自定义字段值表' collate=utf8mb4_unicode_ci;

create index idx_create_time
	on lm_inspection_order_custom_field (create_time);

create index idx_field_code
	on lm_inspection_order_custom_field (field_code);

create index idx_inspection_order_id
	on lm_inspection_order_custom_field (inspection_order_id);

create table lm_inspection_sampling
(
    id bigint auto_increment comment '主键ID'
        primary key,
    inspection_order_id bigint not null comment '检验单ID',
    inspect_item_id bigint null comment '检验项目ID（可为空，代表整体取样）',
    planned_quantity decimal(10,3) not null comment '计划取样量',
    unit_id bigint not null comment '单位ID',
    sample_count int not null comment '计划取样份数',
    sampling_method varchar(100) null comment '取样方式',
    sampling_location varchar(200) null comment '取样地点',
    sampling_description text null comment '取样说明',
    remark text null comment '备注',
    sample_id bigint null comment '关联的样品ID（请验阶段生成样品后设置）',
    sample_no varchar(50) null comment '关联的样品编号（请验阶段生成样品后设置）',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    update_time datetime default (CURRENT_TIMESTAMP) not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by varchar(50) not null comment '创建人',
    update_by varchar(50) not null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是',
    constraint fk_sampling_inspection_order
        foreign key (inspection_order_id) references lm_inspection_order (id)
            on delete cascade
)
    comment '检验取样信息表' collate=utf8mb4_unicode_ci;

create index idx_create_time
	on lm_inspection_sampling (create_time);

create index idx_inspect_item_id
	on lm_inspection_sampling (inspect_item_id);

create index idx_inspection_order_id
	on lm_inspection_sampling (inspection_order_id);

create index idx_sample_id
	on lm_inspection_sampling (sample_id);

create index idx_sample_no
	on lm_inspection_sampling (sample_no);

create table lm_inspection_scheme
(
    id bigint auto_increment comment '主键ID'
        primary key,
    name varchar(100) not null comment '方案名称',
    material_id bigint not null comment '关联的物料ID',
    material_code varchar(50) not null comment '关联的物料编码',
    package_id bigint not null comment '关联的实验包ID',
    package_code varchar(50) not null comment '关联的实验包编码',
    active_version_no varchar(64) null comment '当前生效版本号（冗余）',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    create_by bigint not null comment '创建人ID',
    update_time datetime null on update CURRENT_TIMESTAMP comment '更新时间',
    update_by bigint null comment '更新人ID',
    is_deleted tinyint(1) default 0 not null comment '是否删除',
    constraint uk_name
        unique (name)
)
    comment '检验方案表';

create index idx_create_time
	on lm_inspection_scheme (create_time);

create table lm_inspection_scheme_data_point
(
    id bigint auto_increment comment '主键ID'
        primary key,
    scheme_id bigint not null comment '关联的检验方案ID',
    version_id bigint not null comment '关联的方案版本ID',
    parameter_config_id bigint not null comment '关联的分析项配置ID',
    data_point_id bigint null comment '原始数据点ID',
    package_id bigint null comment '关联的实验包ID',
    inspect_item_id bigint null comment '关联的检验项目ID',
    parameter_id bigint not null comment '关联的分析项ID',
    name varchar(100) not null comment '数据点名称',
    point_type varchar(20) not null comment '数据点类型：NUMBER-数值类型, TEXT-文本类型, OPTION-选项类型',
    trend_line_config text null comment '趋势线配置(JSON)',
    options text null comment '选项配置(JSON)',
    report_display tinyint(1) default 1 null comment '是否在报告中显示',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    create_by bigint not null comment '创建人ID',
    update_time datetime null on update CURRENT_TIMESTAMP comment '更新时间',
    update_by bigint null comment '更新人ID',
    is_deleted tinyint(1) default 0 not null comment '是否删除'
)
    comment '检验方案数据点配置表';

create index idx_create_time
	on lm_inspection_scheme_data_point (create_time);

create index idx_data_point_id
	on lm_inspection_scheme_data_point (data_point_id);

create index idx_parameter_config_id
	on lm_inspection_scheme_data_point (parameter_config_id);

create table lm_inspection_scheme_item
(
    id bigint auto_increment comment '主键ID'
        primary key,
    scheme_id bigint not null comment '关联的检验方案ID',
    version_id bigint not null comment '关联的方案明细ID',
    inspect_item_id bigint not null comment '检验项目ID',
    package_id bigint null comment '关联的实验包ID',
    is_required tinyint(1) default 1 not null comment '是否必检',
    sort int default 0 null comment '排序',
    remark varchar(500) null comment '备注',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    update_time datetime default (CURRENT_TIMESTAMP) not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by varchar(50) not null comment '创建人',
    update_by varchar(50) not null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是',
    time_unit varchar(255) null comment '时长单位',
    duration int null comment '检验工时',
    constraint uk_detail_inspect_item
        unique (version_id, inspect_item_id)
)
    comment '检验方案检验项目配置表' collate=utf8mb4_unicode_ci;

create index idx_create_time
	on lm_inspection_scheme_item (create_time);

create index idx_inspect_item_id
	on lm_inspection_scheme_item (inspect_item_id);

create index idx_version_id
	on lm_inspection_scheme_item (version_id);

create table lm_inspection_scheme_item_teams
(
    id bigint not null comment 'id'
        primary key,
    inspect_item_id bigint null,
    item_config_id bigint null,
    create_by varchar(50) null,
    update_by varchar(50) null,
    create_time datetime null,
    update_time datetime null,
    is_deleted tinyint(1) default 0 null,
    scheme_id bigint null,
    version_id bigint null,
    package_id bigint null,
    team_id bigint null
)
    comment '检项执行班组';

create table lm_inspection_scheme_judgment
(
    id bigint auto_increment comment '主键ID'
        primary key,
    judgement_config_name varchar(255) null,
    scheme_id bigint not null comment '关联的检验方案ID',
    version_id bigint not null comment '关联的方案版本ID',
    parameter_config_id bigint not null comment '关联的分析项配置ID',
    data_point_config_id bigint null comment '数据点配置id',
    data_point_id bigint null comment '原始数据点ID',
    package_id bigint null comment '关联的实验包ID',
    inspect_item_id bigint null comment '关联的检验项目ID',
    parameter_id bigint not null comment '关联的分析项ID',
    judgment_type varchar(20) not null comment '判定类型：RANGE-范围判定, EQUAL-相等判定',
    min_value decimal(10,2) null comment '最小值',
    max_value decimal(10,2) null comment '最大值',
    max_operator varchar(255) null comment '最大值比较运算符',
    min_operator varchar(255) null comment '最小值比较运算符',
    standard_value text null comment '标准值',
    expression text not null comment '判定表达式',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    create_by bigint not null comment '创建人ID',
    update_time datetime null on update CURRENT_TIMESTAMP comment '更新时间',
    update_by bigint null comment '更新人ID',
    is_deleted tinyint(1) default 0 not null comment '是否删除'
)
    comment '检验方案判定配置表';

create index idx_create_time
	on lm_inspection_scheme_judgment (create_time);

create index idx_data_point_id
	on lm_inspection_scheme_judgment (data_point_id);

create index idx_parameter_config_id
	on lm_inspection_scheme_judgment (parameter_config_id);

create table lm_inspection_scheme_parameter
(
    id bigint auto_increment comment '主键ID'
        primary key,
    scheme_id bigint not null comment '关联的检验方案ID',
    version_id bigint not null comment '关联的方案版本ID',
    item_config_id bigint not null comment '关联的检验项目配置ID',
    package_id bigint null comment '关联的实验包ID',
    inspect_item_id bigint null comment '关联的检验项目ID',
    parameter_id bigint not null comment '分析项ID',
    standard_rule text null comment '标准规定',
    is_reportable tinyint(1) default 0 not null comment '是否报告项',
    is_executable tinyint(1) default 1 not null comment '是否可执行',
    final_expression varchar(500) null comment '最终表达式',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    create_by bigint not null comment '创建人ID',
    update_time datetime null on update CURRENT_TIMESTAMP comment '更新时间',
    update_by bigint null comment '更新人ID',
    is_deleted tinyint(1) default 0 not null comment '是否删除'
)
    comment '检验方案分析项配置表';

create index idx_create_time
	on lm_inspection_scheme_parameter (create_time);

create index idx_item_config_id
	on lm_inspection_scheme_parameter (item_config_id);

create index idx_parameter_id
	on lm_inspection_scheme_parameter (parameter_id);

create table lm_inspection_scheme_sampling
(
    id bigint auto_increment comment '主键ID'
        primary key,
    scheme_id bigint not null comment '关联的检验方案ID',
    version_id bigint not null comment '关联的方案版本ID',
    inspect_item_id bigint null comment '检验项目ID',
    sampling_amount decimal(10,2) not null comment '取样量',
    sampling_unit varchar(20) not null comment '取样单位',
    sampling_count int not null comment '取样份数',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    create_by bigint not null comment '创建人ID',
    update_time datetime null on update CURRENT_TIMESTAMP comment '更新时间',
    update_by bigint null comment '更新人ID',
    is_deleted tinyint(1) default 0 not null comment '是否删除',
    constraint uk_detail_inspect_item
        unique (version_id, inspect_item_id, is_deleted)
)
    comment '检验方案取样配置表';

create index idx_create_time
	on lm_inspection_scheme_sampling (create_time);

create index idx_detail_id
	on lm_inspection_scheme_sampling (version_id);

create index idx_inspect_item_id
	on lm_inspection_scheme_sampling (inspect_item_id);

create table lm_inspection_scheme_version
(
    id bigint auto_increment comment '主键ID'
        primary key,
    scheme_id bigint not null comment '关联的检验方案ID',
    version_no varchar(50) not null comment '版本号',
    material_id bigint not null comment '关联的物料ID',
    material_code varchar(50) not null comment '关联的物料编码',
    package_id bigint not null comment '关联的实验包ID',
    package_code varchar(50) not null comment '关联的实验包编码',
    status varchar(20) not null comment '版本状态：EDITING-编辑中, APPROVING-审批中, ACTIVE-生效, INACTIVE-失效',
    description varchar(255) null comment '备注',
    effective_date datetime null comment '生效日期',
    process_instance_id varchar(64) null comment '关联的审批流程实例ID',
    parent_version_id bigint null comment '父版本ID',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    create_by bigint not null comment '创建人ID',
    update_time datetime null on update CURRENT_TIMESTAMP comment '更新时间',
    update_by bigint null comment '更新人ID',
    is_deleted tinyint(1) default 0 not null comment '是否删除',
    constraint uk_scheme_version
        unique (scheme_id, version_no)
)
    comment '检验方案版本表';

create index idx_create_time
	on lm_inspection_scheme_version (create_time);

create index idx_parent_version_id
	on lm_inspection_scheme_version (parent_version_id);

create index idx_scheme_id
	on lm_inspection_scheme_version (scheme_id);

create index idx_status
	on lm_inspection_scheme_version (status);

create table lm_inspection_team
(
    id bigint not null comment '主键',
    name varchar(255) not null comment '班组名称',
    code varchar(255) not null comment '班组编码',
    description varchar(255) null comment '班组描述',
    status int default 0 not null comment '启停状态',
    number int null comment '人数',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '更新时间',
    create_by varchar(32) null comment '创建人',
    update_by varchar(32) null comment '更新人',
    is_deleted tinyint(1) default 0 null comment '是否删除'
)
    collate=utf8mb4_general_ci;

create table lm_inspection_team_user
(
    inspection_team_id bigint not null,
    user_id varchar(64) not null,
    create_by varchar(32) null,
    update_by varchar(32) null,
    create_time datetime null,
    update_time datetime null,
    is_deleted tinyint(1) default 0 null,
    constraint uk_team_user
        unique (inspection_team_id, user_id)
)
    comment '检验班组用户关联表' collate=utf8mb4_general_ci;

create table lm_item_parameter
(
    id bigint not null
        primary key,
    inspect_item_id bigint not null comment '检验项id',
    inspect_parameter_id bigint not null comment '分析项id',
    create_by varchar(64) null comment '创建人',
    update_by varchar(64) null comment '修改人',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    is_deleted tinyint(1) default 0 not null comment '是否删除 0-否 1-是'
)
    comment '检验项与分析项关联表';

create table lm_package_item
(
    id bigint not null
        primary key,
    inspect_package_id bigint not null comment '实验包id',
    inspect_item_id bigint not null comment '检验项id',
    create_by varchar(64) null comment '创建人',
    update_by varchar(64) null comment '修改人',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    is_deleted tinyint(1) default 0 not null comment '是否删除 0-否 1-是'
)
    comment '实验包与检验项关联表';

create table lm_report_generate_task
(
    id bigint auto_increment comment '主键ID'
        primary key,
    report_no varchar(255) null,
    template_version_id bigint not null comment '报告模板版本ID',
    material_id bigint not null comment '检品ID',
    scheme_version_id bigint not null comment '检验方案版本ID',
    inspection_order_id bigint not null comment '检验单ID（样品审核已通过）',
    status varchar(30) not null comment '任务状态：PENDING/RUNNING/SUCCESS/FAILED',
    path varchar(500) null,
    message varchar(1000) null comment '状态说明/错误信息',
    lifecycle_status varchar(30) null comment '报告生命周期：PENDING_APPROVAL/APPROVING/EFFECTIVE/VOIDED',
    start_time datetime null comment '开始时间',
    end_time datetime null comment '结束时间',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    update_time datetime default (CURRENT_TIMESTAMP) not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by varchar(50) not null comment '创建人',
    update_by varchar(50) not null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是',
    report_approval_process_instance_id varchar(128) null,
    report_approved tinyint(1) default 0 null,
    report_approval_time datetime null
)
    comment '报告验证任务表' collate=utf8mb4_unicode_ci;

create index idx_inspection_order_id
	on lm_report_generate_task (inspection_order_id);

create index idx_status
	on lm_report_generate_task (status);

create index idx_template_version_id
	on lm_report_generate_task (template_version_id);

create table lm_report_operation_history
(
    id bigint auto_increment comment '主键ID'
        primary key,
    task_id bigint not null comment '报告生成任务ID',
    operation_type varchar(50) not null comment '操作类型：GENERATE/CONFIRM/VOID/APPROVE/REJECT',
    operator_id varchar(64) null comment '操作人ID',
    operator_name varchar(100) null comment '操作人姓名',
    operate_time datetime default (CURRENT_TIMESTAMP) not null comment '操作时间',
    path varchar(500) null comment '涉及路径（可选）',
    remark varchar(500) null comment '备注',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    update_time datetime default (CURRENT_TIMESTAMP) not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by varchar(50) not null comment '创建人',
    update_by varchar(50) not null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是'
)
    comment '报告操作历史表' collate=utf8mb4_unicode_ci;

create index idx_operate_time
	on lm_report_operation_history (operate_time);

create index idx_operation_type
	on lm_report_operation_history (operation_type);

create index idx_task_id
	on lm_report_operation_history (task_id);

create table lm_report_template
(
    id bigint auto_increment comment '主键ID'
        primary key,
    name varchar(200) not null comment '模板名称（全局唯一）',
    material_id bigint not null comment '检品ID',
    default_version_id bigint null comment '默认版本ID',
    effective_version_id bigint null comment '生效版本ID',
    effective_version_no varchar(50) null comment '生效版本号',
    remark varchar(500) null comment '备注',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    update_time datetime default (CURRENT_TIMESTAMP) not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by varchar(50) not null comment '创建人',
    update_by varchar(50) not null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是',
    constraint uk_report_template_name
        unique (name)
)
    comment '报告模板表' collate=utf8mb4_unicode_ci;

create index idx_material_id
	on lm_report_template (material_id);

create table lm_report_template_operation_history
(
    id bigint auto_increment comment '主键ID'
        primary key,
    template_version_id bigint not null comment '模板版本ID',
    operation_type varchar(50) not null comment '操作类型：CREATE/UPLOAD/DOWNLOAD/CONFIRM/SET_DEFAULT/VALIDATE/VOID',
    operator_id varchar(64) null comment '操作人ID',
    operator_name varchar(100) null comment '操作人姓名',
    operate_time datetime default (CURRENT_TIMESTAMP) not null comment '操作时间',
    path varchar(255) null,
    remark varchar(500) null comment '备注',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    create_by varchar(50) not null comment '创建人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是',
    update_time datetime null,
    update_by bigint null
)
    comment '报告模板版本操作历史表' collate=utf8mb4_unicode_ci;

create index idx_operate_time
	on lm_report_template_operation_history (operate_time);

create index idx_operation_type
	on lm_report_template_operation_history (operation_type);

create index idx_template_version_id
	on lm_report_template_operation_history (template_version_id);

create table lm_report_template_scheme_bind
(
    id bigint auto_increment comment '主键ID'
        primary key,
    template_id bigint not null comment '模板ID',
    scheme_id bigint not null comment '检验方案版本ID',
    remark varchar(500) null comment '备注',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    update_time datetime default (CURRENT_TIMESTAMP) not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by varchar(50) not null comment '创建人',
    update_by varchar(50) not null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是',
    constraint uk_template_scheme_version
        unique (template_id, scheme_id)
)
    comment '报告模板-检验方案版本绑定表' collate=utf8mb4_unicode_ci;

create index idx_scheme_version_id
	on lm_report_template_scheme_bind (scheme_id);

create index idx_template_id
	on lm_report_template_scheme_bind (template_id);

create table lm_report_template_version
(
    id bigint auto_increment comment '主键ID'
        primary key,
    template_id bigint not null comment '模板ID',
    version_no varchar(50) not null comment '版本号（同一模板下唯一）',
    status varchar(30) not null comment '版本状态：EDITING/CONFIRMED/VOIDED',
    is_default tinyint(1) default 0 not null comment '是否默认版本：0-否，1-是',
    path varchar(100) null comment '文件桶',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    update_time datetime default (CURRENT_TIMESTAMP) not null on update CURRENT_TIMESTAMP comment '更新时间',
    remark varchar(500) null,
    create_by varchar(50) not null comment '创建人',
    update_by varchar(50) not null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是',
    constraint uk_template_version_no
        unique (template_id, version_no)
)
    comment '报告模板版本表' collate=utf8mb4_unicode_ci;

create index idx_is_default
	on lm_report_template_version (is_default);

create index idx_status
	on lm_report_template_version (status);

create index idx_template_id
	on lm_report_template_version (template_id);

create table lm_report_validation_task
(
    id bigint auto_increment comment '主键ID'
        primary key,
    template_version_id bigint not null comment '报告模板版本ID',
    material_id bigint not null comment '检品ID',
    scheme_version_id bigint not null comment '检验方案版本ID',
    inspection_order_id bigint not null comment '检验单ID（样品审核已通过）',
    status varchar(30) not null comment '任务状态：PENDING/RUNNING/SUCCESS/FAILED',
    message varchar(1000) null comment '状态说明/错误信息',
    path varchar(500) null,
    start_time datetime null comment '开始时间',
    end_time datetime null comment '结束时间',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    update_time datetime default (CURRENT_TIMESTAMP) not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by varchar(50) not null comment '创建人',
    update_by varchar(50) not null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是'
)
    comment '报告验证任务表' collate=utf8mb4_unicode_ci;

create index idx_inspection_order_id
	on lm_report_validation_task (inspection_order_id);

create index idx_status
	on lm_report_validation_task (status);

create index idx_template_version_id
	on lm_report_validation_task (template_version_id);

create table lm_resource_permission
(
    resource_id bigint not null,
    dept_id bigint not null,
    module varchar(64) null comment '模块',
    constraint uk_resource_dept
        unique (resource_id, dept_id) comment '资源-部门唯一索引'
)
    comment '数据权限表' collate=utf8mb4_general_ci;

create table lm_sample
(
    id bigint auto_increment comment '主键ID'
        primary key,
    inspection_order_id bigint not null comment '检验单ID',
    sample_no varchar(50) not null comment '样品编号',
    sample_name varchar(100) null comment '样品名称',
    sub_sampled tinyint(1) default 0 null,
    sampled tinyint(1) default 0 not null comment '是否已取样：0-否，1-是',
    received tinyint(1) default 0 not null comment '是否已接收：0-否，1-是',
    divided tinyint(1) default 0 not null comment '是否已分样：0-否，1-是',
    collected tinyint(1) default 0 not null comment '是否已领取：0-否，1-是',
    discarded tinyint(1) default 0 not null comment '是否作废：0-否，1-是',
    recycled tinyint(1) default 0 not null comment '是否已回收：0-否，1-是',
    recycled_time datetime null comment '回收时间',
    recycled_by varchar(50) null comment '回收人',
    recycle_quantity decimal(18,6) null comment '回收余量',
    recycle_unit_id bigint null comment '回收余量单位ID',
    recycle_remark varchar(500) null comment '回收备注',
    processed tinyint(1) default 0 not null comment '是否已处理：0-否，1-是',
    process_time datetime null comment '处理时间',
    process_by varchar(50) null comment '处理人',
    process_method varchar(200) null comment '处理方式',
    process_remark varchar(500) null comment '处理备注',
    inspect_item_id bigint null comment '检验项目ID',
    parent_sample_id bigint null comment '父样品ID（分样时使用）',
    parent_sample_no varchar(255) null comment '父样品（分样时使用）',
    plan_quantity decimal(10,3) null,
    quantity decimal(10,3) null comment '样品数量',
    unit_id bigint null comment '单位ID',
    sampler_id varchar(255) null,
    sampler_name varchar(50) null comment '取样人',
    sampling_time datetime null comment '取样时间',
    receiver_id varchar(255) null,
    receiver_name varchar(50) null comment '接收人',
    receive_time datetime null comment '接收时间',
    divider_name varchar(50) null comment '分样人',
    collector_id varchar(50) null comment '领取人ID',
    divide_time datetime null comment '分样时间',
    collector_name varchar(50) null comment '领取人',
    collect_time datetime null comment '领取时间',
    discarded_by varchar(50) null comment '作废人',
    discarded_time datetime null comment '作废时间',
    discarded_reason varchar(500) null comment '作废原因',
    remark text null comment '备注',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    update_time datetime default (CURRENT_TIMESTAMP) not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by varchar(50) not null comment '创建人',
    update_by varchar(50) not null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是',
    constraint uk_sample_no
        unique (sample_no),
    constraint fk_sample_inspection_order
        foreign key (inspection_order_id) references lm_inspection_order (id)
            on delete cascade
)
    comment '样品表' collate=utf8mb4_unicode_ci;

create index idx_collected
	on lm_sample (collected);

create index idx_create_time
	on lm_sample (create_time);

create index idx_discarded
	on lm_sample (discarded);

create index idx_divided
	on lm_sample (divided);

create index idx_inspect_item_id
	on lm_sample (inspect_item_id);

create index idx_inspection_order_id
	on lm_sample (inspection_order_id);

create index idx_parent_sample_id
	on lm_sample (parent_sample_id);

create index idx_received
	on lm_sample (received);

create index idx_sampled
	on lm_sample (sampled);

create table lm_sample_ledger
(
    id bigint not null
        primary key,
    inspection_order_id bigint not null,
    sample_id bigint not null,
    sample_no varchar(50) null comment '样品编号快照',
    quantity decimal(10,3) null comment '样品数量快照',
    recycle_quantity decimal(10,3) null comment '回收量快照',
    unit_id bigint null comment '样品单位ID快照',
    material_id bigint null comment '物料id',
    material_code varchar(64) null comment '检品编码快照',
    operation_type varchar(64) not null,
    operation_time datetime not null,
    operator_id bigint null,
    operator_name varchar(100) null,
    remark varchar(255) null,
    create_time datetime null,
    update_time datetime null,
    create_by varchar(64) null,
    update_by varchar(64) null,
    is_deleted tinyint(1) default 0 null
);

create index idx_sample_ledger_oper_time
	on lm_sample_ledger (operation_time);

create index idx_sample_ledger_order_id
	on lm_sample_ledger (inspection_order_id);

create index idx_sample_ledger_sample_id
	on lm_sample_ledger (sample_id);

create table lm_task
(
    id bigint auto_increment comment '主键ID'
        primary key,
    scheme_version_id bigint null comment '检验方案版本id',
    inspection_order_id bigint not null comment '检验单ID',
    inspect_item_id bigint not null comment '检验项目ID',
    inspect_item_code varchar(100) not null comment '检验项目编码',
    item_config_id bigint null comment '方案检验项目配置ID',
    parameter_id bigint not null comment '分析项ID',
    parameter_code varchar(100) not null comment '分析项编码',
    parameter_config_id bigint null comment '方案分析项配置ID',
    is_executable tinyint(1) default 1 not null comment '是否可执行',
    is_reportable tinyint(1) default 0 not null comment '是否可报告',
    status varchar(50) default 'PENDING_ASSIGNMENT' not null comment '任务状态',
    entry_status varchar(30) null comment '录入状态：WAITING, IN_PROGRESS, TO_REVIEW',
    judged_result tinyint(1) null comment '判定结果：1-通过，0-不通过',
    judged_abnormal tinyint(1) null comment '是否异常(根据表达式计算)',
    judged_time datetime null comment '判定时间',
    reviewed_abnormal_reason varchar(500) null comment '复核不通过/异常原因',
    reviewed_by varchar(50) null comment '复核人ID',
    reviewed_time datetime null comment '复核时间',
    test_time datetime null comment '检验时间',
    owner_id bigint null comment '任务所有人ID',
    owner_name varchar(100) null comment '任务所有人姓名',
    assigner_id bigint null comment '分配人ID',
    assigner_name varchar(100) null comment '分配人姓名',
    assign_time datetime null comment '分配时间',
    claim_time datetime null comment '领取时间',
    complete_time datetime null comment '完成时间',
    terminate_time datetime null comment '终止时间',
    terminate_reason varchar(500) null comment '终止原因',
    return_reason varchar(500) null comment '退回原因',
    reject_reason varchar(500) null comment '审批不通过原因',
    remark varchar(500) null comment '备注',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    update_time datetime default (CURRENT_TIMESTAMP) not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by varchar(50) not null comment '创建人',
    update_by varchar(50) not null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是'
)
    comment '任务表' collate=utf8mb4_unicode_ci;

create index idx_create_time
	on lm_task (create_time);

create index idx_inspect_item_id
	on lm_task (inspect_item_id);

create index idx_inspection_order_id
	on lm_task (inspection_order_id);

create index idx_owner_id
	on lm_task (owner_id);

create index idx_parameter_id
	on lm_task (parameter_id);

create index idx_status
	on lm_task (status);

create index idx_test_time
	on lm_task (test_time);

create table lm_task_status_history
(
    id bigint auto_increment comment '主键ID'
        primary key,
    task_id bigint not null comment '任务ID',
    operation_type varchar(50) not null comment '操作类型',
    from_status varchar(50) null comment '操作前状态',
    to_status varchar(50) null comment '操作后状态',
    operator_id bigint not null comment '操作人ID',
    operator_name varchar(100) not null comment '操作人姓名',
    operate_time datetime not null comment '操作时间',
    reason varchar(500) null comment '操作理由/备注',
    description varchar(500) null comment '操作说明',
    detail json null comment '操作详情(JSON)',
    create_time datetime default (CURRENT_TIMESTAMP) not null comment '创建时间',
    update_time datetime default (CURRENT_TIMESTAMP) not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by varchar(50) not null comment '创建人',
    update_by varchar(50) not null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除：0-否，1-是'
)
    comment '任务状态变更历史表' collate=utf8mb4_unicode_ci;

create index idx_create_time
	on lm_task_status_history (create_time);

create index idx_operate_time
	on lm_task_status_history (operate_time);

create index idx_task_id
	on lm_task_status_history (task_id);

