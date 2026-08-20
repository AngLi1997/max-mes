use bmos_mes;
set names utf8mb4;

alter table bm_material
    ADD COLUMN `product_mark` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品标识' AFTER `expand_info`;

alter table bm_product_plan
    ADD COLUMN `production_line_id` bigint NOT NULL COMMENT '产线id' AFTER `execute_paused`;

alter table bm_procedure_model_room
    ADD COLUMN `room_id_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '产线id-房间id' AFTER `room_id`;

alter table bm_process_version
    DROP COLUMN `production_line_id`;

DROP TABLE IF EXISTS `bm_reserve_component_instance`;
CREATE TABLE `bm_reserve_component_instance` (
                                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理主键',
                                                 `product_plan_id` bigint DEFAULT NULL COMMENT '生产计划id',
                                                 `batch_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生产批号',
                                                 `component_id` bigint DEFAULT NULL COMMENT '组件id',
                                                 `procedure_step_model_id` bigint DEFAULT NULL COMMENT '工序步骤模型id',
                                                 `record_item_id` bigint DEFAULT NULL COMMENT '记录项id',
                                                 `record_version_id` bigint DEFAULT NULL COMMENT '记录项版本id',
                                                 `reuse` tinyint(1) DEFAULT NULL COMMENT '是否复用',
                                                 `copy_version` int DEFAULT NULL COMMENT '复制版本',
                                                 `create_time` datetime DEFAULT NULL,
                                                 `update_time` datetime DEFAULT NULL,
                                                 `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                 `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                 `is_deleted` tinyint(1) DEFAULT '0',
                                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `bm_reserve_component_material`;
CREATE TABLE `bm_reserve_component_material` (
                                                 `id` bigint NOT NULL,
                                                 `instance_id` bigint NOT NULL,
                                                 `storage_material_id` bigint NOT NULL,
                                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `bm_team_production_line`;
CREATE TABLE `bm_team_production_line` (
                                           `id` bigint NOT NULL,
                                           `team_id` bigint NOT NULL COMMENT '班组id',
                                           `production_line_id` bigint NOT NULL COMMENT '产线id',
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

