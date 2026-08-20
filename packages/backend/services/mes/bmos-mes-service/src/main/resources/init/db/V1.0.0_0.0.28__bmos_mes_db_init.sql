use bmos_mes;
set names utf8mb4;
set foreign_key_checks = 0;

DROP TABLE IF EXISTS `bm_procedure_model_room`;
CREATE TABLE `bm_procedure_model_room` (
                                           `procedure_model_id` bigint NOT NULL COMMENT '工序id',
                                           `room_id` bigint NOT NULL COMMENT '房间id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='工序绑定配方物料表';

DROP TABLE IF EXISTS `bm_procedure_equipment_info`;
CREATE TABLE `bm_procedure_equipment_info` (
                                               `id` bigint NOT NULL,
                                               `product_plan_id` bigint NOT NULL COMMENT '生产计划id',
                                               `batch_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '批次号',
                                               `process_id` bigint DEFAULT NULL COMMENT '工序模型id',
                                               `process_version` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工艺版本',
                                               `record_item_id` bigint DEFAULT NULL COMMENT '记录项id',
                                               `record_version_id` bigint DEFAULT NULL COMMENT '记录版本id',
                                               `procedure_step_id` bigint DEFAULT NULL COMMENT '工序步骤id',
                                               `procedure_step_model_id` bigint DEFAULT NULL COMMENT '工序步骤模型id',
                                               `component_id` bigint NOT NULL COMMENT '组件id',
                                               `equipment_id` bigint DEFAULT NULL COMMENT '设备id',
                                               `copy_version` bigint DEFAULT NULL COMMENT '复制版本',
                                               `create_by` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                               `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
                                               `create_time` datetime NOT NULL COMMENT '创建时间',
                                               `update_time` datetime NOT NULL COMMENT '更新时间',
                                               `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `bm_procedure_equipment_acquisition`;
CREATE TABLE `bm_procedure_equipment_acquisition` (
                                                      `id` bigint NOT NULL,
                                                      `product_plan_id` bigint NOT NULL COMMENT '生产计划id',
                                                      `batch_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '批次号',
                                                      `process_id` bigint DEFAULT NULL COMMENT '工序模型id',
                                                      `process_version` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工艺版本',
                                                      `record_item_id` bigint DEFAULT NULL COMMENT '记录项id',
                                                      `record_version_id` bigint DEFAULT NULL COMMENT '记录版本id',
                                                      `procedure_step_id` bigint DEFAULT NULL COMMENT '工序步骤id',
                                                      `procedure_step_model_id` bigint DEFAULT NULL COMMENT '工序步骤模型id',
                                                      `component_id` bigint NOT NULL COMMENT '组件id',
                                                      `equipment_id` bigint DEFAULT NULL COMMENT '设备id',
                                                      `copy_version` bigint DEFAULT NULL COMMENT '复制版本',
                                                      `acquisition_id` bigint DEFAULT NULL COMMENT '采集项id',
                                                      `acquisition_code` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '采集项编码',
                                                      `data_point_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '采集点位名称',
                                                      `data_point_value_time` datetime NOT NULL COMMENT '点位数据时间',
                                                      `data_point_value` varchar(500) COLLATE utf8mb4_general_ci NOT NULL COMMENT '点位数据',
                                                      `acquisition_time` datetime NOT NULL COMMENT '采集时间',
                                                      `input_type` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '录入类型',
                                                      `create_by` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                                      `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
                                                      `create_time` datetime NOT NULL COMMENT '创建时间',
                                                      `update_time` datetime NOT NULL COMMENT '更新时间',
                                                      `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

set foreign_key_checks = 1;