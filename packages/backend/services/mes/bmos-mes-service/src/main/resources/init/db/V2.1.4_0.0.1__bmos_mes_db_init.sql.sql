ALTER TABLE `inf_ru_execution` ADD COLUMN `is_deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识';
ALTER TABLE `inf_ru_process_instance` ADD COLUMN `is_deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识';
ALTER TABLE `inf_ru_task_instance` ADD COLUMN `is_deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识';
ALTER TABLE `inf_hi_execution` ADD COLUMN `is_deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识';
ALTER TABLE `inf_hi_task_instance` ADD COLUMN `is_deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识';
ALTER TABLE `inf_hi_process_instance` ADD COLUMN `is_deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识';
ALTER TABLE `bm_material_log` ADD COLUMN `quality_status` varchar(32) DEFAULT null COMMENT '质量批次状态';
ALTER TABLE `inf_hi_execution` ADD COLUMN `complete_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '完成人';
-- 换班班组表普通索引
ALTER TABLE `bm_product_change_team` ADD INDEX index_instruction_id(`product_instruction_team_id`);
ALTER TABLE `bm_product_instruction_team` ADD INDEX index_plan_id(`product_plan_id`);