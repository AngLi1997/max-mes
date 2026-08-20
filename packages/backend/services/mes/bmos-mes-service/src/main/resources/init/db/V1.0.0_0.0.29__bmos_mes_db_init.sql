use bmos_mes;
set names utf8mb4;
set foreign_key_checks = 0;

-- 字段重命名
ALTER TABLE `bmos_mes`.`bm_batch_release_history` RENAME COLUMN `generated` to `release_generated`;

-- 字段添加
ALTER TABLE `bmos_mes`.`bm_ingredient_plan` ADD COLUMN `copy_version` int NULL DEFAULT NULL COMMENT '复制版本' AFTER `procedure_step_model_id`;
ALTER TABLE `bmos_mes`.`bm_ingredient_plan` ADD COLUMN `record_version_id` bigint DEFAULT NULL COMMENT '记录项版本id';

ALTER TABLE `bmos_mes`.`bm_process_version` ADD COLUMN `production_line_id` bigint NULL DEFAULT NULL COMMENT '产线id' AFTER `action_state`;

ALTER TABLE `bmos_mes`.`bm_product_instruction` ADD COLUMN `confirm_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指令单确认人id' AFTER `principal`;

ALTER TABLE `bmos_mes`.`bm_product_plan` ADD COLUMN `execute_paused` tinyint(1) NULL DEFAULT NULL COMMENT '生产计划执行已暂停' AFTER `unit_id`;

ALTER TABLE `bmos_mes`.`inf_hi_execution` ADD COLUMN `super_execution_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `execution_id`;

ALTER TABLE `bmos_mes`.`inf_hi_process_instance` ADD COLUMN `category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `id`;

ALTER TABLE `bmos_mes`.`inf_hi_process_instance` ADD COLUMN `business_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `category`;

ALTER TABLE `bmos_mes`.`inf_hi_process_instance` ADD COLUMN `super_execution_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `process_instance_id`;

set foreign_key_checks = 1;