-- 2.1.1 老君山版本新的sql写在这里
-- alter table bm_business_component_instance
--               add record_item_id bigint null comment '记录项id' after component_id;
-- alter table bm_business_component_instance
--               add record_version_id bigint null comment '记录项版本id' after record_item_id;
-- alter table bm_business_component_instance
--             add process_id bigint null comment '工艺id' after procedure_step_id;
-- alter table bm_business_component_instance
--             add process_version bigint null comment '工艺版本' after process_id;
-- alter table bm_weigh_input_record
--     add formula_material_id bigint null comment '配方物料id' after material_id;

create table if not exists bm_weigh_requirement_key
(
    weigh_requirement_key varchar(255)         null comment '称量需求key(工步id_工步模型id（复用就是0))',
    component_instance_id bigint               null comment '组件实例id'
) comment '称量中心需求key组件实例关联表';

-- alter table bm_business_component_instance
--     drop column weigh_requirement_key;

alter table bm_execute_attachment
              add attachment_type varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '附件类型';

alter table bm_weigh_requirement_key
  add finished boolean null comment '是否已完成投料';

-- alter table bm_weigh_requirement
--     drop column weigh_requirement_key;

-- alter table bm_weigh_requirement
--     add procedure_step_config_id bigint null comment '组件配置id' after id;

-- alter table bm_business_component_instance
--     add procedure_step_config_id bigint null comment '组件配置id' after id;

drop table bm_weigh_requirement_key;

create table if not exists bm_weigh_input_process
(
    id                    bigint               not null comment '物理主键'
        primary key,
    component_instance_id bigint               null comment '组件实例id',
    finished              tinyint              null comment '是否已完成',
    create_time           datetime             null,
    update_time           datetime             null,
    create_by             varchar(64)          null,
    update_by             varchar(64)          null,
    is_deleted            tinyint(1) default 0 null
) comment '称量物料投入流程表';

-- alter table bm_weigh_input_record
--     add requirement_id bigint null comment '称量需求id' after device_code;

-- alter table bm_weigh_input_record
--     drop column weigh_requirement_key;

-- 调整备注
alter table bm_weigh_requirement
    modify weigh_status int null comment '称量状态 0 未称量 1 称量中 2 已完成称量 3 已完成签名';
alter table bm_weigh_requirement
    modify weigh_process int null comment '称量阶段 1 物料称量 2 更换需求 3 余料称量 4 已完成称量 5 已完成签名';
alter table bm_weigh_requirement
-- 调整顺序
    modify requirement_status int null comment '需求状态 0 未规划 1 未称量 2 称量中 3 已完成 4 已失效' after program_time;
alter table bm_weigh_requirement
    modify weigh_process int null comment '称量阶段 1 物料称量 2 更换需求 3 余料称量 4 已完成称量 5 已完成签名' after weigh_status;

-- 物料临期提醒
alter table bm_material
    add dying_period int null comment '物料临期提醒天数(单位：天)' after status;

alter table bm_material
    add storage_condition varchar(255) null comment '保存条件' after status;

alter table bm_material
    add storage_date date null comment '保存日期' after status;

-- 手写签名
create table if not exists bp_user_sign
(
    id            bigint                               not null comment '主键'
  primary key,
    user_id       varchar(64)                          null comment '用户id',
    sign_url      varchar(64)                          null comment '签名url',
    sign_time     datetime                             null comment '签名时间',
    terminal_type varchar(255)                         null comment '终端类型 0-pc 1-pad',
    create_time   datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '创建时间',
    update_time   datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by     varchar(32)                          null comment '创建人',
    update_by     varchar(32)                          null comment '更新人',
    is_deleted    tinyint(1) default 0                 null comment '是否删除',
    constraint idx_user_id
        unique (user_id)
)
    comment '手写签名表' row_format = DYNAMIC;

-- 物料自定义字段
create table if not exists bm_material_field
(
    id                  bigint               not null primary key,
    field_type	        varchar(64)	         null comment '字典类型',
    field_type_name    	varchar(255)	     null comment '字典类型名称',
    field_code	            varchar(64)	         null comment '字段',
    field_name	        varchar(255)	     null comment '字段名称',
    field_value	        varchar(64)	         null comment '字段值',
    material_id	        bigint	             null comment '生产物料id 表bp_material的主键id',
    create_by           varchar(64)          null,
    update_by           varchar(64)          null,
    create_time         datetime             null,
    update_time         datetime             null,
    is_deleted          tinyint(1) default 0 not null
)
    row_format = DYNAMIC;

create table if not exists bm_material_batch_field
(
    id                  bigint               not null primary key,
    field_type	        varchar(64)	         null comment '字典类型',
    field_type_name    	varchar(255)	     null comment '字典类型名称',
    field_code          varchar(64)	         null comment '字段',
    field_name	        varchar(255)	     null comment '字段名称',
    field_value	        varchar(64)	         null comment '字段值',
    material_batch_id	        bigint	     null comment '物料批次id 表bm_storage_material_batch的主键id',
    create_by           varchar(64)          null,
    update_by           varchar(64)          null,
    create_time         datetime             null,
    update_time         datetime             null,
    is_deleted          tinyint(1) default 0 not null
)
    row_format = DYNAMIC;



CREATE TABLE if not exists `bm_liquid_preparation_plan` (
                                              `id` bigint NOT NULL,
                                              `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配液单名称',
                                              `product_plan_id` bigint NOT NULL COMMENT '生产计划id',
                                              `batch_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生产批号',
                                              `record_item_id` bigint NOT NULL COMMENT '记录项id',
                                              `record_version_id` bigint NOT NULL COMMENT '记录项版本id',
                                              `reuse` tinyint(1) NOT NULL COMMENT '是否复用',
                                              `procedure_step_model_id` bigint NOT NULL COMMENT '工序步骤模型id',
                                              `copy_version` int NOT NULL COMMENT '复制版本',
                                              `serial_no` int NOT NULL COMMENT '流水号',
                                              `component_id` bigint NOT NULL COMMENT '组件id',
                                              `completed` tinyint(1) DEFAULT NULL COMMENT '该配液单是否已完成',
                                              `config_json` text COLLATE utf8mb4_general_ci COMMENT '配置信息',
                                              `actual_target_volume` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '目标体积',
                                              `liquid_measure_instance_id` bigint DEFAULT NULL COMMENT '配液量取组件实例id',
                                              `unit_id` bigint DEFAULT NULL COMMENT '目标体积单位id',
                                              `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                              `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                              `create_time` datetime DEFAULT NULL,
                                              `update_time` datetime DEFAULT NULL,
                                              `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='配液计划表';

CREATE TABLE if not exists `bm_liquid_preparation_plan_material_batch` (
                                                             `id` bigint NOT NULL,
                                                             `liquid_preparation_plan_id` bigint NOT NULL COMMENT '配液计划id',
                                                             `material_batch_id` bigint NOT NULL COMMENT '物料批次id',
                                                             `material_batch_no` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                             `formula_material_id` bigint NOT NULL COMMENT '配方物料id',
                                                             `preparation_quantity` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配液量',
                                                             `unit_id` bigint NOT NULL COMMENT '配方单位id',
                                                             `material_order` int NOT NULL DEFAULT '0' COMMENT '排序',
                                                             `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                             `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                             `create_time` datetime DEFAULT NULL,
                                                             `update_time` datetime DEFAULT NULL,
                                                             `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='配液计划批次表';

CREATE TABLE if not exists `bm_liquid_preparation_measure_instance` (
                                                          `id` bigint NOT NULL,
                                                          `product_plan_id` bigint NOT NULL COMMENT '生产计划id',
                                                          `reuse` tinyint(1) NOT NULL COMMENT '是否复用',
                                                          `procedure_step_model_id` bigint NOT NULL COMMENT '工序步骤模型id',
                                                          `copy_version` int NOT NULL COMMENT '复制版本',
                                                          `component_id` bigint NOT NULL COMMENT '组件id',
                                                          `liquid_preparation_plan_id` bigint DEFAULT NULL COMMENT '配液计划id',
                                                          `pre_measurer_id` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '量取人id',
                                                          `pre_re_checker_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '复核人id',
                                                          `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
                                                          `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                          `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                          `create_time` datetime DEFAULT NULL,
                                                          `update_time` datetime DEFAULT NULL,
                                                          `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='配液量取组件实例表';

CREATE TABLE if not exists `bm_liquid_preparation_measure_batch` (
                                                       `id` bigint NOT NULL,
                                                       `liquid_preparation_plan_batch_id` bigint NOT NULL COMMENT '配液批次id',
                                                       `measure_instance_id` bigint NOT NULL COMMENT '量取实例id',
                                                       `material_batch_id` bigint NOT NULL COMMENT '物料批次id',
                                                       `liquid_preparation_plan_id` bigint DEFAULT NULL COMMENT '配液单id',
                                                       `measure_status` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '量取状态',
                                                       `measure_stage` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '量取阶段',
                                                       `put_quantity` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '投入总量',
                                                       `measurer_id` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '量取人id',
                                                       `re_checker_id` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '复核人id',
                                                       `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
                                                       `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                       `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                       `create_time` datetime DEFAULT NULL,
                                                       `update_time` datetime DEFAULT NULL,
                                                       `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='配液量取批次表';

CREATE TABLE if not exists `bm_liquid_preparation_measure_record` (
                                                        `id` bigint NOT NULL,
                                                        `measure_instance_id` bigint NOT NULL COMMENT '量取组件实例id',
                                                        `liquid_preparation_plan_id` bigint NOT NULL COMMENT '配液计划id',
                                                        `measure_batch_id` bigint NOT NULL COMMENT '量取批次id',
                                                        `material_id` bigint NOT NULL COMMENT '物料id',
                                                        `material_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料名称',
                                                        `formula_material_id` bigint DEFAULT NULL COMMENT '配方物料id',
                                                        `material_merge_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料编码',
                                                        `quantity` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '量取量',
                                                        `unit_id` bigint NOT NULL COMMENT '单位id',
                                                        `storage_material_id` bigint NOT NULL COMMENT '物料件id',
                                                        `storage_material_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '物料件号',
                                                        `storage_material_batch_id` bigint NOT NULL COMMENT '物料批次id',
                                                        `storage_material_batch_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '物料批次号',
                                                        `material_position_id` bigint DEFAULT NULL COMMENT '货位id',
                                                        `material_position` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货位编码-货位名称',
                                                        `container_id` bigint DEFAULT NULL COMMENT '容器id',
                                                        `container_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '容器编码-容器名称',
                                                        `sign_status` int DEFAULT NULL COMMENT '签名状态',
                                                        `measurer_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '量取人id',
                                                        `re_checker_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '复核人id',
                                                        `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
                                                        `measure_time` datetime NOT NULL COMMENT '量取时间',
                                                        `measure_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '量取类型',
                                                        `measure_mode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '量取模式',
                                                        `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                        `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                        `create_time` datetime DEFAULT NULL,
                                                        `update_time` datetime DEFAULT NULL,
                                                        `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='配液量取记录表';

CREATE TABLE if not exists `bm_liquid_preparation_measure_log` (
                                                     `id` bigint NOT NULL,
                                                     `measure_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '量取类型',
                                                     `measure_quantity` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '量取量',
                                                     `unit_id` bigint NOT NULL COMMENT '单位id',
                                                     `unit_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '单位名称',
                                                     `measurer_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '量取人id',
                                                     `measurer_login_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '量取人账号',
                                                     `measurer_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '量取人名称',
                                                     `re_checker_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '复核人id',
                                                     `re_checker_login_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '复核人账号',
                                                     `re_checker_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '复核人名称',
                                                     `storage_material_id` bigint DEFAULT NULL COMMENT '物料件id',
                                                     `material_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料件号',
                                                     `measure_time` datetime NOT NULL COMMENT '量取时间',
                                                     `material_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料名称',
                                                     `material_merge_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料编码',
                                                     `material_id` bigint DEFAULT NULL COMMENT '物料id',
                                                     `material_type` tinyint(1) DEFAULT NULL COMMENT '物料类型',
                                                     `material_batch_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料批次号',
                                                     `material_batch_id` bigint DEFAULT NULL COMMENT '物料批次id',
                                                     `product_id` bigint DEFAULT NULL COMMENT '产品id',
                                                     `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品名称',
                                                     `product_merge_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品编码',
                                                     `product_batch_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品批号',
                                                     `product_plan_id` bigint NOT NULL COMMENT '生产计划id',
                                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='配液量取日志表';

alter table bm_product_plan
    add `modify_count` int DEFAULT '0' COMMENT '修订数量' after archive_status;
