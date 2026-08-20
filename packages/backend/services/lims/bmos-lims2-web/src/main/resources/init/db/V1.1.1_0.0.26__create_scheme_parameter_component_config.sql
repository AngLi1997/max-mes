/*
 * 描述: 创建工序步骤记录项配置表
 * 作者: yigaohui
 * 日期: 2025-11-10
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 新增表 lm_scheme_parameter_component_config
 * 2. 补充必要索引
 */

create table if not exists lm_scheme_parameter_component_config
(
    id bigint not null comment '主键ID'
        primary key,

    parameter_id bigint not null comment '工序步骤id（procedure_step_model的Id）',
    parameter_config_id bigint null comment '工序步骤模型id',
    node_id varchar(64) null comment '流程节点Id',
    scheme_id bigint not null comment '工艺id',
    scheme_version_id varchar(64) not null comment '工艺版本号',
    record_item_id bigint not null comment '记录项id',
    record_version_id bigint null comment '记录项版本id',

    config_info longtext collate utf8mb4_bin null comment '配置信息JSON',

    component_id bigint null comment '组件id',
    field_id bigint null comment 'field_id',

    create_time datetime null comment '创建时间',
    update_time datetime null comment '更新时间',
    create_by varchar(64) null comment '创建人',
    update_by varchar(64) null comment '更新人',
    is_deleted tinyint(1) default 0 not null comment '是否删除'
)
    comment '工序步骤记录项配置表';

create table bm_operate_rule
(
    id bigint not null comment '主键id'
        primary key,
    name varchar(100) not null comment '文件名称',
    code varchar(100) not null comment '文件编号',
    category_id bigint not null comment '分类id',
    create_time datetime not null comment '创建时间',
    update_time datetime not null comment '修改时间',
    create_by varchar(60) charset utf8mb3 null comment '创建人',
    update_by varchar(60) charset utf8mb3 null comment '修改人',
    is_deleted tinyint default 0 not null comment '是否删除'
)
    comment '操作规程主表';

create table bm_operate_rule_category
(
    id bigint not null comment '主键id'
        primary key,
    name varchar(60) charset utf8mb3 not null comment '分类名称',
    code varchar(255) null comment '编码',
    parent_id bigint default 0 null comment '上级id',
    create_time datetime not null comment '创建时间',
    update_time datetime not null comment '修改时间',
    create_by varchar(60) charset utf8mb3 null comment '创建人',
    update_by varchar(60) charset utf8mb3 null comment '修改人',
    is_deleted tinyint default 0 not null comment '是否删除',
    del_flag bigint default 0 not null
)
    comment '操作规程分类表';

create table bm_operate_rule_version
(
    id bigint not null comment '主键id'
        primary key,
    operate_id bigint not null comment '主表id',
    version varchar(60) not null comment '版本号',
    remark varchar(255) null comment '描述',
    history_state varchar(60) default 'edit' not null comment '历史状态',
    state varchar(60) default 'edit' not null comment '状态',
    url varchar(255) not null comment '文件上传地址',
    upload_time datetime null comment '文件上传时间',
    instance_id varchar(64) null comment '流程id',
    audit_type varchar(64) null comment '流类型',
    effect_date varchar(64) default '-' null comment '生效日期',
    file_effect_date varchar(64) null comment '线下文件生效日期(此为用户选择的文件生效日期)',
    create_time datetime not null comment '创建时间',
    update_time datetime not null comment '修改时间',
    create_by varchar(60) charset utf8mb3 null comment '创建人',
    update_by varchar(60) charset utf8mb3 null comment '修改人',
    is_deleted tinyint default 0 not null comment '是否删除'
);




alter table lm_inspection_scheme_judgment
    add min_time varchar(255) null;

alter table lm_inspection_scheme_judgment
    add max_time varchar(255) null;

