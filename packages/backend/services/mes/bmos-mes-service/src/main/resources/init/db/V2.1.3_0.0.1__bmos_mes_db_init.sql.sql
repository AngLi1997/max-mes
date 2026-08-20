-- 2.1.3 小浪底版本新的sql写在这里

-- 皮重配置表
create table if not exists bm_tare_weigh_config
(
    id          bigint               not null comment '物理主键'
        primary key,
    tare_weigh  varchar(100)         null comment '皮重',
    unit        varchar(100)         null comment '皮重单位',
    unit_id     bigint               null comment '皮重单位id',
    describe_info  varchar(200)         null comment '描述',
    editor_id   varchar(100)         null comment '修订人',
    edit_time   datetime             null comment '修订时间',
    create_time datetime             null,
    update_time datetime             null,
    create_by   varchar(100)         null,
    update_by   varchar(100)         null,
    is_deleted  tinyint(1) default 0 null
) comment '皮重配置表';

-- 物料批次质量状态
ALTER TABLE `bm_storage_material_batch` ADD COLUMN `quality_status` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '批次质量状态' AFTER `available`;

-- 逻辑表达式表增加节点类型字段
ALTER TABLE `bm_procedure_expression` ADD COLUMN `expression_node_type` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表达式节点类型';
UPDATE `bmos_mes`.`bm_procedure_expression` SET `expression_node_type` = 'step_or_task' where expression_node_type is NULL;
-- 条件节点类型字段
ALTER TABLE `bm_procedure_condition` ADD COLUMN `condition_node_type` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '条件节点类型';
UPDATE `bmos_mes`.`bm_procedure_condition` SET `condition_node_type` = 'step_or_task' where condition_node_type is NULL;
-- 工作流流程实例表
ALTER TABLE `inf_ru_execution` ADD COLUMN `pause_tag` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '暂停标识';
-- 工作流任务表
ALTER TABLE `inf_ru_task_instance` ADD COLUMN `pause_tag` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '暂停标识';
-- 任务实例表
ALTER TABLE `bm_procedure_task_instance` ADD COLUMN `pause_tag` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '暂停标识';
-- 物料预定组件
ALTER TABLE `bm_reserve_component_material` ADD COLUMN `cancel_reserve` tinyint(1) DEFAULT NULL COMMENT '是否被取消预定';
UPDATE `bm_reserve_component_material` SET `cancel_reserve` = 0;

-- 修改班组表字段结构
ALTER TABLE bm_product_instruction_team MODIFY COLUMN team_ids text CHARACTER
SET utf8mb4 COLLATE utf8mb4_bin COMMENT '班组id列表';

-- 批记录记录项表结构拆分

DROP TABLE IF EXISTS bm_batch_record_parse;

CREATE TABLE `bm_batch_record_parse` (
  `id` bigint NOT NULL COMMENT '记录项id',
  `file_content` mediumblob COMMENT 'html字符串',
  `docx_header` longtext COLLATE utf8mb4_general_ci COMMENT '页眉',
  `docx_footer` longtext COLLATE utf8mb4_general_ci COMMENT '页脚',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='记录解析html表';


DROP TABLE IF EXISTS bm_batch_record_component_detail;

CREATE TABLE `bm_batch_record_component_detail` (
                                                    `id` bigint NOT NULL COMMENT '主键id',
                                                    `component_detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '组件详细内容',
                                                    `formula_field` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '公式实际参数字段JSON',
                                                    `formula_config` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '公式额外配置',
                                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                                    `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
                                                    `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
                                                    `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除',
                                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='记录组件表';