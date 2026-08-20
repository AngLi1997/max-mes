use bmos_mes;
set names utf8mb4;
set foreign_key_checks = 0;

DROP TABLE IF EXISTS `bm_ingredient_input_component_instance`;
CREATE TABLE `bm_ingredient_input_component_instance` (
                                                          `id` bigint NOT NULL,
                                                          `product_plan_id` bigint DEFAULT NULL COMMENT '生产计划id',
                                                          `procedure_step_model_id` bigint DEFAULT NULL COMMENT '工序步骤模型id',
                                                          `component_id` bigint DEFAULT NULL COMMENT '组件id',
                                                          `ingredient_plan_id` bigint DEFAULT NULL COMMENT '绑定的配料单id',
                                                          `copy_version` int DEFAULT NULL COMMENT '复制版本',
                                                          `record_item_id` bigint DEFAULT NULL COMMENT '记录项id',
                                                          `record_version_id` bigint DEFAULT NULL COMMENT '记录项版本id',
                                                          `reuse` tinyint(1) DEFAULT NULL COMMENT '是否复用',
                                                          `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                          `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                          `create_time` datetime DEFAULT NULL,
                                                          `update_time` datetime DEFAULT NULL,
                                                          `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

set foreign_key_checks = 1;