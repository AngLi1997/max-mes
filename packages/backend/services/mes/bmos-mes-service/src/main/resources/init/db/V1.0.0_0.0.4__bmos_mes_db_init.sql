use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 工序步骤配置表修改
ALTER TABLE `bmos_mes`.`bm_procedure_step_config` ADD COLUMN `procedure_step_model_id` bigint NOT NULL COMMENT '工序步骤实例信息id' AFTER `procedure_step_id`;
ALTER TABLE `bmos_mes`.`bm_procedure_step_config` ADD COLUMN `field_id` bigint NOT NULL COMMENT '组件标识:field_id 前端使用' AFTER `component_id`;
-- 工艺流程字段长度修改
ALTER TABLE `bmos_mes`.`bm_procedure_step_model` MODIFY COLUMN `name`  varchar(255) NOT NULL comment '步骤名称';
ALTER TABLE `bmos_mes`.`bm_procedure_step` MODIFY COLUMN `name`  varchar(255) NOT NULL comment '步骤名称';
ALTER TABLE `bmos_mes`.`bm_product_instruction_team` MODIFY COLUMN `procedure_step_model_name`  varchar(255) NOT NULL comment '生产工序步骤名称';
ALTER TABLE `bmos_mes`.`inf_ru_execution` MODIFY COLUMN `element_name` varchar(255) DEFAULT NULL;
ALTER TABLE `bmos_mes`.`inf_hi_task_instance` MODIFY COLUMN `element_name` varchar(255) DEFAULT NULL;
ALTER TABLE `bmos_mes`.`inf_ru_task_instance` MODIFY COLUMN `element_name` varchar(255) DEFAULT NULL;

SET FOREIGN_KEY_CHECKS = 1;
