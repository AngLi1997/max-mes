-- 修改execute_form_data中value字段类型
alter table `bm_execute_form_data` MODIFY COLUMN `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '填报值';