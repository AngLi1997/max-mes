ALTER TABLE `bm_plan_template_batch` ADD COLUMN `process_key` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '前端使用key' AFTER `sort`;

ALTER TABLE inf_ru_execution
DROP INDEX uk_execution;

ALTER TABLE `inf_ru_execution` ADD UNIQUE `uk_execution` (`execution_id` ASC, `id` ASC);