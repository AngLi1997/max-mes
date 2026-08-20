-- lm_stability_inspect_plan_batch 新增 zero_month_order_id 字段（用户手动选择的0月检验单）
ALTER TABLE `lm_stability_inspect_plan_batch` ADD COLUMN `zero_month_order_id` BIGINT DEFAULT NULL COMMENT '用户手动选择的0月检验单ID（同批号存在多条常规检验单时由用户指定）' AFTER `sample_receive_date`;
