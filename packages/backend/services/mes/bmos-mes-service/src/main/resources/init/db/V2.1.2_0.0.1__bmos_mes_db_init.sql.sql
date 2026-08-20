-- 2.1.2 太行山版本新的sql写在这里

-- 删除旧的批签发/数据集表
drop table if exists bm_batch_release_template_dataset;
drop table if exists bm_dataset;
drop table if exists bm_dataset_data_point;
drop table if exists bm_dataset_data_point_group;
drop table if exists bm_dataset_data_point_handle;
drop table if exists bm_dataset_data_point_handle_group;
drop table if exists bm_dataset_version;

-- 创建新的批签发/数据集表

drop table if exists bm_dataset_category;
create table if not exists bm_dataset_category
(
    id          bigint primary key comment '物理主键',
    parent_id   bigint            null comment '上级分类id',
    name        varchar(100)      null comment '数据集名称',
    id_path     text              null comment '数据集分类id路径',
    create_time datetime          null,
    update_time datetime          null,
    create_by   varchar(100)      null,
    update_by   varchar(100)      null,
    is_deleted  tinyint default 0 null comment '是否删除'
) comment '数据集分类表';

drop table if exists bm_dataset;
create table if not exists bm_dataset
(
    id                  bigint            not null comment '物理主键'
        primary key,
    dataset_category_id bigint            null comment '数据集分类id',
    name                varchar(100)      null comment '数据集名称',
    type                varchar(100)      null comment '数据集类型 POINT 批记录数据(数据点) LOT_RELEASE_LINK 批签发引用 DYNAMIC_REPORT 动态数据填报',
    product_id          bigint            null comment '产品id',
    process_id          bigint            null comment '工艺id',
    dataset_key         varchar(100)      null comment '数据集key',
    create_time         datetime          null,
    update_time         datetime          null,
    create_by           varchar(100)      null,
    update_by           varchar(100)      null,
    is_deleted          tinyint default 0 null comment '是否删除'
) comment '数据集表';


drop table if exists bm_dataset_point;
create table if not exists bm_dataset_point
(
    id                      bigint            not null comment '物理主键'
        primary key,
    dataset_id              bigint            null comment '数据集id',
    dataset_key             varchar(100)      null comment '数据集key(暂时用流水号)',
    name                    varchar(100)      null comment '数据点名称',
    dataset_point_key       varchar(100)      null comment '数据点key(暂时用流水号)',
    type                    varchar(100)      null comment '数据集类型 POINT 批记录数据(数据点) LOT_RELEASE_LINK 批签发引用 DYNAMIC_REPORT 动态数据填报',
    procedure_step_id       bigint            null comment '工步id',
    field_id                bigint            null comment '字段id',
    extra                   text              null comment '前端扩展字段(json)',
    component_id            bigint            null comment '组件id',
    component_name          varchar(100)      null comment '组件名称',
    component_number        bigint            null comment '组件关联表格最大下标值',
    record_item_id          bigint            null comment '记录项id',
    record_item_name        varchar(100)      null comment '记录项名称',
    lot_release_template_id bigint            null comment '批签发模板id',
    lot_release_version     varchar(100)      null comment '批签发版本',
    link_area               varchar(100)      null comment '批签发引用参数范围(p15:s19)',
    template_url            varchar(1024)     null comment '批签发引用模版url',
    dynamic_data_type       varchar(100)      null comment '动态填报数据类型  NUMBER 数值 TEXT文本 DATE日期',
    default_value           varchar(100)      null comment '动态填报默认值',
    create_time             datetime          null,
    update_time             datetime          null,
    create_by               varchar(100)      null,
    update_by               varchar(100)      null,
    is_deleted              tinyint default 0 null comment '是否删除'
)
    comment '数据点表';

    drop table if exists bm_lot_release_template;
    create table if not exists bm_lot_release_template
    (
        id                          bigint               not null comment '物理主键'
            primary key,
        category_id bigint       null comment '分类id',
        name        varchar(100) null comment '模板名称',
        effective_lot_release_id bigint null comment '生效的批签发id',
        create_time datetime     null,
        update_time datetime     null,
        create_by   varchar(100) null,
        update_by   varchar(100) null,
        is_deleted                  tinyint(1) default 0 null
    ) comment '批签发模板';


    drop table if exists bm_lot_release_template_category;
    create table if not exists bm_lot_release_template_category
    (
        id                          bigint               not null comment '物理主键'
            primary key,
        parent_id   bigint       null comment '上级分类id',
        name        varchar(100) null comment '数据集名称',
        id_path     varchar(100) null comment '数据集分类id路径',
        create_time datetime     null,
        update_time datetime     null,
        create_by   varchar(100) null,
        update_by   varchar(100) null,
        is_deleted                  tinyint(1) default 0 null
    ) comment '批签发模板分类';

    drop table if exists bm_lot_release_template_history;
    create table if not exists bm_lot_release_template_history
    (
        id                          bigint               not null comment '物理主键'
            primary key,
        template_version_id bigint       null comment '批签发版本id',
        operate_type        varchar(100) null comment '操作类型 CREATE 新增模板 CREATE_VERSION 新增版本 UPLOAD 上传 DOWNLOAD 下载 VALIDATE 验证 MAKE_DEFAULT 设为默认 MAKE_SURE 确认 SCRAP 作废',
        operate_user_id     varchar(100) null comment '操作人id',
        operate_user_name   varchar(100) null comment '操作人名称',
        operate_time        datetime     null comment '操作时间',
        operate_remark      varchar(200) null comment '操作备注',
        ext                 varchar(1024) null comment '扩展信息',
        comment             varchar(200)         null comment '操作摘要',
        node_name           varchar(100)         null comment '审核节点名称',
        create_time         datetime     null,
        update_time         datetime     null,
        create_by           varchar(100) null,
        update_by           varchar(100) null,
        is_deleted                  tinyint(1) default 0 null
    ) comment '批签发模板操作历史';

    drop table if exists bm_lot_release_template_process;
    create table if not exists bm_lot_release_template_process
    (
        id                          bigint               not null comment '物理主键'
            primary key,
        process_id              bigint null comment '工艺id',
        lot_release_template_id bigint null comment '批签发模版id'
    ) comment '批签发模板关联工艺';

    drop table if exists bm_lot_release_template_version;
    create table if not exists bm_lot_release_template_version
    (
        id                          bigint               not null comment '物理主键'
            primary key,
        template_id  bigint       null comment '模版id',
        name         varchar(100)         null comment '模版名称',
        version      varchar(100) null comment '版本',
        template_url varchar(1024) null comment '模板url',
        remark       varchar(200) null comment '备注',
        is_default   tinyint      null comment '是否默认',
        status       varchar(100) null comment '版本状态 EDIT 编辑 MAKE_SURE 确认 SCRAP 作废',
        create_time  datetime     null,
        update_time  datetime     null,
        create_by    varchar(100) null,
        update_by    varchar(100) null,
        is_deleted                  tinyint(1) default 0 null
    ) comment '批签发模板版本';

    drop table if exists bm_lot_release;
    create table if not exists bm_lot_release
    (
        id               bigint               not null comment '物理主键'
            primary key,
        no               varchar(100)         null comment '批签发编号',
        name             varchar(100)         null comment '模板名称',
        template_version varchar(100)         null comment '模板版本',
        template_id      bigint               null comment '模版id',
        process_id       bigint               null comment '工艺id',
        process_name     varchar(100)         null comment '产品名称',
        plan_id          bigint               null comment '生产计划id',
        batch_no         varchar(100)         null comment '批次号',
        product_id       bigint               null comment '产品id',
        product_name     varchar(100)         null comment '产品名称',
        product_merge_code varchar(100)         null comment '产品合并编码',
        specification          varchar(100)         null comment '规格',
        submitter_id     varchar(100)         null comment '提交审核人id',
        submitter_name   varchar(100)         null comment '提交审核人姓名',
        submitter_time   datetime             null comment '提交审核时间',
        audit_process_instance varchar(100)         null comment '审核流程实例id',
        generator_name   varchar(100)         null comment '生成人姓名',
        generator_id     varchar(100)         null comment '生成人id',
        generate_time    datetime             null comment '生成时间',
        effect_time      datetime             null comment '生效时间',
        status           varchar(100)         null comment '批签发状态 EDIT 编辑 PROCESSING 审批中 EFFECTIVE 生效 SCRAPED 作废',
        remark           varchar(200)         null comment '备注',
        file_url         varchar(1024)         null comment '文件地址',
        create_time      datetime             null,
        update_time      datetime             null,
        create_by        varchar(100)         null,
        update_by        varchar(100)         null,
        is_deleted       tinyint(1) default 0 null
    ) comment '批签发数据表';

    drop table if exists bm_dataset_point_template_relation;
    create table if not exists bm_dataset_point_template_relation
    (
        id                 bigint            not null comment '物理主键'
            primary key,
        template_url       varchar(1024)            null comment '模版url',
        placeholder        varchar(100)      null comment '占位符',
        key_size           varchar(100)      null comment '索引数量',
        dataset_point_keys varchar(100)      null comment '数据点索引json',
        dataset_keys       varchar(100)      null comment '数据集索引json',
        create_time        datetime          null,
        update_time        datetime          null,
        create_by          varchar(100)      null,
        update_by          varchar(100)      null,
        is_deleted         tinyint default 0 null comment '是否删除'
    )
        comment '模版数据点关联表';

-- 批记录
create table bm_batch_template_category
(
    id          bigint            not null comment '主键id'
        primary key,
    name        varchar(100)      null comment '分类名称',
    parent_id   bigint            not null comment '父级分类id',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '修改时间',
    create_by   varchar(64)       null comment '创建人',
    update_by   varchar(64)       null comment '修改人',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    comment '批记录模板分类' row_format = DYNAMIC;

create table bm_batch_template_info
(
    id          bigint            not null comment '主键id'
        primary key,
    name        varchar(100)      null comment '分类名称',
    category_id bigint            not null comment '分类id bm_batch_template_category表的主键id',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '修改时间',
    create_by   varchar(64)       null comment '创建人',
    update_by   varchar(64)       null comment '修改人',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    comment '批记录模板信息' row_format = DYNAMIC;


create table bm_batch_template_version
(
    id                     bigint            not null comment '主键id'
        primary key,
    version                varchar(100)      not null comment '版本号',
    path                   varchar(128)      null comment '模板上传到minio的路径',
    remark                 varchar(200)      null comment '模板备注',
    normal                 tinyint           null comment '是否设为默认',
    status                 int               not null comment '状态  830201-编辑 830202-确认 830203-生效 830204-作废',
    batch_template_info_id bigint            not null comment '批记录模板id  bm_batch_template_info表的主键id',
    create_time            datetime          null comment '创建时间',
    update_time            datetime          null comment '修改时间',
    create_by              varchar(64)       null comment '创建人',
    update_by              varchar(64)       null comment '修改人',
    is_deleted             tinyint default 0 null comment '是否删除'
)
    comment '批记录模板版本' row_format = DYNAMIC;

create table bm_batch_template_info_process
(
    batch_template_info_id bigint not null comment 'bm_batch_template_version 批记录模板版本表的主键id',
    process_id             bigint not null comment '工艺id',
    primary key (batch_template_info_id, process_id)
)
    comment '批记录模板信息版本与工艺的绑定关系' row_format = DYNAMIC;

create table bm_batch_template_operate_log
(
    id                        bigint            not null comment '主键id'
        primary key,
    batch_template_version_id bigint            not null comment 'bm_batch_template_version 批记录模板版本表的主键id',
    path                      varchar(64)       null comment 'minio上传路径',
    operate_type              int               not null comment '操作类型 830101-新增 830102-上传 830103-删除 830104-确认 830105-作废 830106-生效',
    remark                    varchar(255)      null comment '备注',
    operator_id               varchar(64)       not null comment '操作人ueseId',
    operator_login_name       varchar(64)       not null comment '操作人登录名称',
    operator_name             varchar(64)       not null comment '操作人用户名称',
    operate_time              datetime          null comment '创建时间',
    create_time               datetime          null comment '创建时间',
    update_time               datetime          null comment '修改时间',
    create_by                 varchar(64)       null comment '创建人',
    update_by                 varchar(64)       null comment '修改人',
    is_deleted                tinyint default 0 null comment '是否删除'
)
    comment '批记录模板信息版本与工艺的绑定关系' row_format = DYNAMIC;

create table bm_batch_record_archive_generate
(
    id                        bigint               not null
        primary key,
    batch_template_version_id bigint               not null comment '模板版本id bm_batch_template_version的主键id',
    plan_id                   bigint               null comment '生产计划id',
    complete                  tinyint(1) default 0 not null comment '是否生成完成',
    batch_record_archive_id   bigint               null comment '若为重新生成，则此值为bm_batch_record_archive表中的id',
    operate_type              int                  not null comment '操作类型 830301-重新生成 830307-批记录生成 830308-自动生成',
    user_id                   varchar(64)          null comment '操作人',
    path                      varchar(1024)        null comment '归档生成的path',
    create_by                 varchar(64)          null,
    update_by                 varchar(64)          null,
    create_time               datetime             null,
    update_time               datetime             null,
    is_deleted                tinyint(1) default 0 not null
)
    comment '归档生成的批记录的生成记录' row_format = DYNAMIC;

create table bm_batch_record_archive
(
    id                        bigint            not null comment '主键id'
        primary key,
    archive_no                varchar(64)       not null comment 'bm_batch_record_archive 批记录模板版本表的主键id',
    path                      varchar(1024)     not null comment 'minio上传路径',
    batch_template_info_id    bigint            not null comment '批记录模板id bm_batch_template_info表的主键id',
    batch_template_version_id bigint            not null comment '当前生成的批记录是由哪一个模板版本id生成的',
    template_name             varchar(64)       not null comment '模板名称',
    template_version          varchar(64)       not null comment '模板名称',
    plan_id                   bigint            not null comment '生产计划id',
    batch_no                  varchar(64)       not null comment '生产批号',
    product_name              varchar(64)       not null comment '产品名称',
    instance_id               varchar(64)       null comment '审核流实例id',
    status                    int               not null comment '状态 830401-编辑 830402-审批中 830403-生效 830404 - 作废',
    archive_time              datetime          not null comment '归档时间（生成时间）',
    effective_time            datetime          null comment '生效时间',
    remark                    varchar(255)      null comment '备注',
    auditor_id                varchar(64)       null comment '审核人id',
    auditor_name              varchar(64)       null comment '审核人名称',
    auditor_login_name        varchar(64)       null comment '审核人登录名称',
    operator_id               varchar(64)       not null comment '归档操作人ueseId',
    operator_name             varchar(64)       not null comment '归档操作人用户名称',
    operator_login_name       varchar(64)       not null comment '归档操作人登录名称',
    create_time               datetime          null comment '创建时间',
    update_time               datetime          null comment '修改时间',
    create_by                 varchar(64)       null comment '创建人',
    update_by                 varchar(64)       null comment '修改人',
    is_deleted                tinyint default 0 null comment '是否删除'
)
    comment '归档生成的批记录档案' row_format = DYNAMIC;

create table bm_batch_record_archive_log
(
    id                      bigint            not null comment '主键id'
        primary key,
    batch_record_archive_id bigint            not null comment 'bm_batch_record_archive 批记录模板版本表的主键id',
    path                    varchar(1024)     null comment 'minio上传路径',
    operate_type            int               not null comment '操作类型 830301-重新生成 830302-上传 830303-下载 830304-提交审批 830305-审批完成 830306-作废 830307-批记录生成',
    archive_time            datetime          null comment '档案生成的时间',
    effective_time          datetime          null comment '档案生效时间',
    remark                  varchar(255)      null comment '备注',
    audit_result            tinyint           null comment '审核结果',
    audit_opinion           varchar(255)      null comment '审核意见',
    instance_id             varchar(64)       null comment '审核流实例id',
    element_name            varchar(255)      null comment '审核节点名称',
    operator_id             varchar(64)       not null comment '操作人ueseId',
    operator_login_name     varchar(64)       not null comment '操作人登录名称',
    operator_name           varchar(64)       not null comment '操作人用户名称',
    operate_time            datetime          null comment '操作时间',
    create_time             datetime          null comment '创建时间',
    update_time             datetime          null comment '修改时间',
    create_by               varchar(64)       null comment '创建人',
    update_by               varchar(64)       null comment '修改人',
    is_deleted              tinyint default 0 null comment '是否删除'
)
    comment '归档生成的批记录的操作日志' row_format = DYNAMIC;

    create table bm_lot_release_history
    (
        id                bigint               not null comment '物理主键'
            primary key,
        lot_release_id    bigint               null comment '批签发id',
        operate_type      varchar(100)         null comment '操作类型 CREATE 新增模板 CREATE_VERSION 新增版本 UPLOAD 上传 DOWNLOAD 下载 VALIDATE 验证 MAKE_DEFAULT 设为默认 MAKE_SURE 确认 SCRAP 作废',
        operate_user_id   varchar(100)         null comment '操作人id',
        operate_user_name varchar(100)         null comment '操作人名称',
        operate_time      datetime             null comment '操作时间',
        operate_remark    varchar(100)         null comment '操作备注',
        ext               varchar(1024)         null comment '扩展信息',
        comment           varchar(200)         null comment '操作摘要',
        node_name         varchar(100)         null comment '审核节点名称',
        create_time       datetime             null,
        update_time       datetime             null,
        create_by         varchar(100)         null,
        update_by         varchar(100)         null,
        is_deleted        tinyint(1) default 0 null
    )
        comment '批签发操作历史';



-- 中间品产出修改 按件产出
alter table bm_output_weigh_record
    add quantity varchar(255) null comment '物料量' after storage_material_batch_id;
alter table bm_output_weigh_record
    add by_piece boolean null comment '是否按件产出' after quantity;
-- 修复数据
update bm_output_weigh_record set quantity = net_weight, by_piece = 0 where quantity is null;

-- 批次摘要
create table bm_lot_summary
(
    id           bigint auto_increment comment '物理主键'
        primary key,
    name         varchar(100)         null comment '批次摘要名称',
    product_id   bigint               null comment '产品id',
    product_name varchar(100)         null comment '产品名称',
    process_id   bigint               null comment '工艺id',
    process_name varchar(100)         null comment '工艺名称',
    create_time  datetime             null,
    update_time  datetime             null,
    create_by    varchar(64)          null,
    update_by    varchar(64)          null,
    is_deleted   tinyint(1) default 0 null
)
    comment '批次摘要表';


create table bm_lot_summary_item
(
    id               bigint auto_increment comment '物理主键'
        primary key,
    lot_summary_id   bigint               null comment '批次摘要id',
    label_name       varchar(100)         null comment '标题名称',
    dataset_point_id bigint               null comment '数据点名称',
    lot_summary_item_type varchar(100)         null comment '批次摘要项类型',
    create_time      datetime             null,
    update_time      datetime             null,
    create_by        varchar(64)          null,
    update_by        varchar(64)          null,
    is_deleted       tinyint(1) default 0 null
)
    comment '批次摘要表数据项表';

-- 流程绑定工艺
create table bm_flow_audit_process
(
    process_id    bigint      not null comment '工艺id 对应bm_process的主键id',
    code          varchar(64) not null comment '流程编码 对应bm_flow_audit的code字段',
    category_code varchar(64) not null comment '对应的分类code ',
    primary key (category_code, process_id)
)
    comment '流程工艺绑定关系表' row_format = DYNAMIC;

CREATE TABLE `bm_execute_subsidiary_record` (
                                                `id` bigint NOT NULL,
                                                `product_plan_id` bigint NOT NULL,
                                                `procedure_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                                `procedure_model_id` bigint NOT NULL,
                                                `reuse` tinyint NOT NULL,
                                                `procedure_step_id` bigint NOT NULL,
                                                `record_item_id` bigint NOT NULL,
                                                `record_version_id` bigint NOT NULL,
                                                `process_change_number` int DEFAULT NULL,
                                                `procedure_change_number` int DEFAULT NULL,
                                                `start_time` datetime DEFAULT NULL,
                                                `end_time` datetime DEFAULT NULL,
                                                `complete_user_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                `archive_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                                `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
                                                `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
                                                `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                                                `procedure_step_model_id` bigint NOT NULL,
                                                `procedure_step_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `bm_plan_template` (
                                    `id` bigint NOT NULL,
                                    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板名称',
                                    `confirmed` tinyint(1) NOT NULL COMMENT '确认状态',
                                    `state` tinyint(1) NOT NULL COMMENT '启停状态',
                                    `operator_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作人id',
                                    `operation_time` datetime NOT NULL COMMENT '操作时间',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
                                    `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
                                    `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Plan Template Table';

CREATE TABLE `bm_plan_template_batch` (
                                          `id` bigint NOT NULL,
                                          `plan_template_id` bigint NOT NULL COMMENT '关联生产计划模板id',
                                          `process_id` bigint NOT NULL COMMENT '工艺id',
                                          `process_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '工艺名称',
                                          `process_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工艺版本',
                                          `interval_duration` int NOT NULL COMMENT '间隔时长',
                                          `execution_duration` int NOT NULL COMMENT '执行时长',
                                          `production_line_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '产线名称',
                                          `production_line_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产线编码',
                                          `production_line_id` bigint NOT NULL COMMENT '产线id',
                                          `batch_quantity` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '生产批量',
                                          `unit_id` bigint DEFAULT NULL COMMENT '生产批量单位id',
                                          `reuse_batch_number` tinyint DEFAULT NULL COMMENT '是否沿用批号',
                                          `follow_batch_sort` int DEFAULT NULL COMMENT '沿用批号批次index',
                                          `relation_batch_sort_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关联模板批次sort集合',
                                          `procedure_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '工序执行时长配置',
                                          `sort` int NOT NULL COMMENT '批次排序',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                          `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
                                          `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
                                          `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除',
                                          `product_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品名称',
                                          `product_merge_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品编码',
                                          `product_specification` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品规格',
                                          `product_id` bigint DEFAULT NULL COMMENT '产品id',
                                          `inner_packing_specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '内包规格',
                                          `packing_specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '包装规格',
                                          `product_mark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '产品标识',
                                          `relation_processes_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '前端使用,关联工艺配置',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Your Table Description';

CREATE TABLE `bm_execute_exception` (
                                        `id` bigint NOT NULL,
                                        `exception_type` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '异常类型',
                                        `exception_description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '异常描述',
                                        `exception_status` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '异常状态',
                                        `record_mode` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '录入方式',
                                        `record_user_id` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录人id',
                                        `record_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录人名称',
                                        `record_time` datetime NOT NULL COMMENT '记录时间',
                                        `product_id` bigint DEFAULT NULL COMMENT '产品id',
                                        `product_full_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品名称',
                                        `product_plan_id` bigint DEFAULT NULL COMMENT '生产计划id',
                                        `batch_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生产批号',
                                        `process_id` bigint DEFAULT NULL COMMENT '工艺id',
                                        `process_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工艺名称',
                                        `process_version` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工艺版本',
                                        `procedure_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工序名称',
                                        `procedure_id` bigint DEFAULT NULL COMMENT '工序id',
                                        `procedure_model_id` bigint DEFAULT NULL COMMENT '工序模型id',
                                        `procedure_step_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工序步骤名称',
                                        `procedure_step_id` bigint DEFAULT NULL COMMENT '工序步骤id',
                                        `procedure_step_model_id` bigint DEFAULT NULL COMMENT '工序步骤模型id',
                                        `handle_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '处理人id',
                                        `handle_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '处理结果',
                                        `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
                                        `handle_user_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '处理人名称',
                                        `cancel_user_id` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作废人id',
                                        `cancel_user_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作废人名称',
                                        `cancel_reason` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作废原因',
                                        `cancel_time` datetime DEFAULT NULL COMMENT '作废时间',
                                        `execute_form_data_id` bigint DEFAULT NULL COMMENT '执行填报值表主键id',
                                        `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                        `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                        `create_time` datetime DEFAULT NULL,
                                        `update_time` datetime DEFAULT NULL,
                                        `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                                        `exception_type_code` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

alter table bm_batch_record_component
    add formula_config longtext null comment '公式额外配置' after date_type;

alter table bm_log_operation
    add detail longtext null comment '详细信息' after comment;

CREATE TABLE `bm_product_plan_no_info` (
                                           `product_plan_id` bigint NOT NULL,
                                           `plan_no_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '计划编号规则code',
                                           `batch_no_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '批号编号规则code',
                                           `plan_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '计划编号',
                                           `batch_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '批次编号',
                                           `plan_no_id` bigint DEFAULT NULL COMMENT '计划编号id',
                                           `batch_no_id` bigint DEFAULT NULL COMMENT '批次编号id',
                                           `fields` longtext COLLATE utf8mb4_general_ci COMMENT '规则编码参数json',
                                           PRIMARY KEY (`product_plan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

ALTER TABLE `bm_product_plan_relation` ADD COLUMN `source_product_plan_id` bigint DEFAULT NULL;

ALTER TABLE `bm_product_plan_relation`
DROP INDEX `uk_processId_batchNo`;
ALTER TABLE `bm_product_plan_relation` ADD UNIQUE `uk_processId_batchNo` (`product_plan_id` ASC, `relation_product_plan_id` ASC, `source_product_plan_id` ASC);


-- 换班班组信息表
CREATE TABLE `bm_product_change_team` (
                                          `id` bigint NOT NULL COMMENT '主键id',
                                          `product_instruction_team_id` bigint NOT NULL COMMENT '生产指令单确定班组信息表',
                                          `team_ids` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '班组id集合',
                                          `change_team_number` int NOT NULL COMMENT '换班次数',
                                          `change_team_type` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '换班类型',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                          `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
                                          `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
                                          `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='换班班次信息表';

-- 任务数据添加换班字段信息
alter table bm_procedure_task_instance
    add active_time datetime DEFAULT NULL COMMENT '激活时间';
alter table bm_procedure_task_instance
    add process_change_number int DEFAULT '0' COMMENT '工艺换班次数';
alter table bm_procedure_task_instance
    add procedure_change_number  int DEFAULT '0' COMMENT '工序换班次数';
alter table bm_procedure_task_instance
    add coerce_user varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '强制开启人';
alter table bm_procedure_task_instance
    add coerce_time datetime DEFAULT NULL COMMENT '强制开始时间';

alter table bm_procedure_task_instance_history
    add active_time datetime DEFAULT NULL COMMENT '激活时间';
alter table bm_procedure_task_instance_history
    add process_change_number int DEFAULT '0' COMMENT '工艺换班次数';
alter table bm_procedure_task_instance_history
    add procedure_change_number  int DEFAULT '0' COMMENT '工序换班次数';
alter table bm_procedure_task_instance_history
    add coerce_user varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '强制开启人';
alter table bm_procedure_task_instance_history
    add coerce_time datetime DEFAULT NULL COMMENT '强制开始时间';

-- 记录项复制添加字段
alter table bm_execute_record_copy
    add procedure_change_number int DEFAULT '0' COMMENT '工序换班次数';
alter table bm_execute_record_copy
    add process_change_number int DEFAULT '0' COMMENT '工艺换班次数';

-- 生产执行表
alter table bm_execute_form_data
    add procedure_change_number int DEFAULT '0' COMMENT '工序换班次数';
alter table bm_execute_form_data
    add process_change_number int DEFAULT '0' COMMENT '工艺换班次数';

-- 工作流审核表
alter table inf_ru_execution
    add change_user varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '换班人';
alter table inf_ru_execution
    add procedure_change_number int DEFAULT '0' COMMENT '工序换班次数';
alter table inf_ru_execution
    add process_change_number int DEFAULT '0' COMMENT '工艺换班次数';
alter table inf_ru_execution
    add change_type varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '换班类型';

alter table inf_hi_execution
    add change_user varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '换班人';
alter table inf_hi_execution
    add procedure_change_number int DEFAULT '0' COMMENT '工序换班次数';
alter table inf_hi_execution
    add process_change_number int DEFAULT '0' COMMENT '工艺换班次数';
alter table inf_hi_execution
    add change_type varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '换班类型';

-- 生产计划相关
CREATE TABLE `bm_production_plan` (
                                      `id` bigint NOT NULL COMMENT '主键id',
                                      `plan_name` varchar(60) COLLATE utf8mb4_general_ci NOT NULL COMMENT '计划名称',
                                      `plan_template_id` bigint NOT NULL COMMENT '生产计划模板id',
                                      `plan_type` varchar(60) COLLATE utf8mb4_general_ci NOT NULL COMMENT '指令单类型',
                                      `plan_first_date` date NOT NULL COMMENT '首批生成日期',
                                      `plan_number` int NOT NULL COMMENT '计划数量',
                                      `duration` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '间隔时长',
                                      `plan_state` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'SEND' COMMENT '状态',
                                      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                      `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                      `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
                                      `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
                                      `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `index_name` (`plan_name`) USING BTREE COMMENT '名称唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生产计划表';

CREATE TABLE `bm_production_plan_item` (
                                           `id` bigint NOT NULL COMMENT '主键id',
                                           `template_batch_id` bigint NOT NULL COMMENT '计划模板详情表id',
                                           `production_plan_id` bigint NOT NULL COMMENT '计划id',
                                           `start_time` date NOT NULL COMMENT '计划开始日期',
                                           `end_time` date NOT NULL COMMENT '计划结束日期',
                                           `production_line_code` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '产线编码',
                                           `production_line_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产线名称',
                                           `production_line_id` bigint NOT NULL COMMENT '产线id',
                                           `process_num` int DEFAULT NULL COMMENT '工序数量',
                                           `plan_no` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '生产指令单批号',
                                           `batch_no` varchar(60) COLLATE utf8mb4_general_ci NOT NULL COMMENT '生产批号',
                                           `batch_quantity` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '生产批量',
                                           `production_batch_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '关联批次信息',
                                           `procedure_list` text COLLATE utf8mb4_general_ci NOT NULL COMMENT '工序相关信息',
                                           `group_number` int NOT NULL COMMENT '分组信息',
                                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                           `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                           `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
                                           `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
                                           `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除',
                                           `related_batch_info` longtext COLLATE utf8mb4_general_ci COMMENT '前端使用关联批次信息',
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生产计划子表';

-- 生成指令单
alter table bm_product_plan
    add `production_plan_item_id` bigint DEFAULT NULL COMMENT '计划详情id';


-- 工序换班、工艺换班字段赋默认值
UPDATE `bmos_mes`.`inf_hi_execution`
SET `process_change_number` = 0,
`procedure_change_number` = 0
WHERE
	ISNULL( process_change_number )
	OR ISNULL(
	procedure_change_number);

UPDATE `bmos_mes`.`inf_ru_execution`
SET `process_change_number` = 0,
`procedure_change_number` = 0
WHERE
	ISNULL( process_change_number )
	OR ISNULL(
	procedure_change_number);

UPDATE `bmos_mes`.`bm_procedure_task_instance`
SET `process_change_number` = 0,
`procedure_change_number` = 0
WHERE
	ISNULL( process_change_number )
	OR ISNULL( procedure_change_number )
	AND is_deleted = 0;

UPDATE `bmos_mes`.`bm_procedure_task_instance_history`
SET `process_change_number` = 0,
`procedure_change_number` = 0
WHERE
	ISNULL( process_change_number )
	OR ISNULL( procedure_change_number )
	AND is_deleted = 0;

UPDATE `bmos_mes`.`bm_execute_record_copy`
SET `process_change_number` = 0,
`procedure_change_number` = 0
WHERE
	ISNULL( process_change_number )
	OR ISNULL( procedure_change_number )
	AND is_deleted = 0;

UPDATE `bmos_mes`.`bm_execute_form_data`
SET `process_change_number` = 0,
`procedure_change_number` = 0
WHERE
	ISNULL( process_change_number )
	OR ISNULL( procedure_change_number );