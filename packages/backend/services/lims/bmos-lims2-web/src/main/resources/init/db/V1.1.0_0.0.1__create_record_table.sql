create table if not exists bmos_lims2.bm_batch_record
(
    id bigint not null comment '主键id'
    primary key,
    name varchar(64) not null comment '记录名称',
    category_id bigint not null comment '分类id',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    create_by varchar(64) null comment '创建人',
    update_by varchar(64) null comment '修改人',
    is_deleted tinyint default 0 not null comment '是否删除',
    constraint index_name_delete
    unique (name, is_deleted) comment '唯一索引'
    )
    comment '批记录信息' charset=utf8mb3;

create table if not exists bmos_lims2.bm_batch_record_category
(
    id bigint not null comment '主键id'
    primary key,
    name varchar(60) not null comment '分类名称',
    code varchar(255) null,
    parent_id bigint default 0 null comment '上级id',
    sort bigint null comment '排序号',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    create_by varchar(60) null comment '创建人',
    update_by varchar(60) null comment '修改人',
    is_deleted tinyint default 0 null comment '是否删除',
    del_flag bigint default 0 not null
    )
    comment '记录配置分类表' charset=utf8mb3;

create table if not exists bmos_lims2.bm_batch_record_component
(
    id bigint not null comment '主键id'
    primary key,
    record_item_id bigint not null comment '记录项id',
    record_version_id bigint null comment '版本id',
    record_version varchar(64) null comment '批记录版本号',
    record_id bigint null comment '批记录id',
    component_type varchar(64) null comment '组件类型',
    component_name varchar(64) null comment '组件名称',
    field_id bigint null comment '空格标识',
    component_number bigint null comment '组件关联表格最大下标值',
    formula_precision bigint null comment '精度',
    component_detail longtext collate utf8mb4_bin null comment '公式详细内容',
    is_result tinyint null comment '标记该组件是否是一个计算结果（0否1是，默认0）',
    formula_id bigint null comment '公式id',
    formula_field longtext collate utf8mb4_bin null comment '公式实际参数字段JSON',
    formula_expression varchar(60) null comment '公式表达式',
    formula_type varchar(60) null comment '公式类型',
    round_code varchar(60) null comment '修约公式code',
    parent_id bigint default 0 null comment '父级id',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    create_by varchar(64) charset utf8mb3 null comment '创建人',
    update_by varchar(64) charset utf8mb3 null comment '修改人',
    is_deleted tinyint default 0 null comment '是否删除',
    used tinyint(1) null comment '是否使用',
    date_type varchar(60) null,
    formula_config longtext null comment '公式额外配置'
    )
    comment '记录组件表';

create index idx_record_item_version_field_id
	on bmos_lims2.bm_batch_record_component (record_item_id, record_version_id, field_id);

create table if not exists bmos_lims2.bm_batch_record_component_detail
(
    id bigint not null comment '主键id'
    primary key,
    component_detail longtext collate utf8mb4_bin null comment '组件详细内容',
    formula_field longtext collate utf8mb4_bin null comment '公式实际参数字段JSON',
    formula_config longtext null comment '公式额外配置',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    create_by varchar(64) charset utf8mb3 null comment '创建人',
    update_by varchar(64) charset utf8mb3 null comment '修改人',
    is_deleted tinyint default 0 null comment '是否删除'
    )
    comment '记录组件表';

create table if not exists bmos_lims2.bm_batch_record_expression
(
    record_id bigint not null,
    expression_id bigint not null
)
    comment '记录与公式绑定关系';

create table if not exists bmos_lims2.bm_batch_record_item
(
    id bigint not null comment '主键id'
    primary key,
    name varchar(100) null,
    item_id bigint not null comment '业务id',
    item_path varchar(255) null comment '上传单个记录项指令集地址',
    item_type varchar(255) null comment '0:大纲内容false，1：页眉页脚内容true',
    sort int null comment '排序字段',
    file_content mediumblob null comment '记录项内容',
    file_path varchar(1024) null comment '文件路径',
    max_number int null comment '文档最大下标',
    version varchar(64) null comment '版本号',
    page_config varchar(255) default '{"pattern":1}' null comment '文档配置',
    record_version_id bigint null comment '记录版本表id',
    docx_header longtext null comment '页眉',
    docx_footer longtext null comment '页脚',
    first_different tinyint(1) null comment '首页不同',
    page_number_style int null comment '页码样式',
    page_starting_number int null comment '页码起始值',
    odd_and_even_different tinyint(1) null comment '奇偶不同',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    create_by varchar(60) charset utf8mb3 null comment '创建人',
    update_by varchar(60) charset utf8mb3 null comment '修改人',
    is_deleted tinyint default 0 null comment '是否删除'
    )
    comment '记录项表';

create index idx_record_item_id_version
	on bmos_lims2.bm_batch_record_item (item_id, record_version_id);

create table if not exists bmos_lims2.bm_batch_record_parse
(
    id bigint not null comment '记录项id'
    primary key,
    file_content mediumblob null comment 'html字符串',
    docx_header longtext null comment '页眉',
    docx_footer longtext null comment '页脚',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    create_by varchar(60) charset utf8mb3 null comment '创建人',
    update_by varchar(60) charset utf8mb3 null comment '修改人',
    is_deleted tinyint default 0 null comment '是否删除'
    )
    comment '记录解析html表';

create table if not exists bmos_lims2.bm_batch_record_product
(
    id bigint not null comment '主键id'
    primary key,
    record_id bigint not null comment '批记录id',
    product_id bigint not null comment '产品id',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    create_by varchar(60) charset utf8mb3 null comment '创建人',
    update_by varchar(60) charset utf8mb3 null comment '修改人',
    is_deleted tinyint default 0 null comment '是否删除'
    )
    comment '记录关联产品表';

create table if not exists bmos_lims2.bm_batch_record_version
(
    id bigint not null comment '主键id'
    primary key,
    record_id bigint not null comment '记录管理表id',
    version varchar(32) not null comment '版本号',
    state varchar(32) default '1' not null comment '状态：1：可编辑；2：审核；3：确定：4：作废',
    instance_id varchar(64) null comment '流程实例id',
    file_path varchar(255) null comment '存放文件地址',
    remark varchar(255) null,
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    create_by varchar(64) charset utf8mb3 null comment '创建人',
    update_by varchar(64) charset utf8mb3 null comment '修改人',
    is_deleted tinyint default 0 null comment '是否删除',
    constraint idex_record_id_version
    unique (record_id, version) comment '批记录id与版本号唯一索引'
    )
    comment '记录版本表';

-- 增量脚本：分析项数据点 TIME 类型支持与显示格式

-- 1) 为分析项数据点表增加 time_format 字段（若不存在）
ALTER TABLE lm_inspect_parameter_data_point
    ADD COLUMN date_style varchar(64) NULL COMMENT '时间类型显示格式' AFTER standard;


-- 1) 为方案数据点表增加 time_format 字段（若不存在）
ALTER TABLE lm_inspection_scheme_data_point
    ADD COLUMN date_style varchar(64) NULL COMMENT '时间类型显示格式' AFTER options;