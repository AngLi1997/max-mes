-- 在留样销毁台账表中添加销毁原因字段
ALTER TABLE `lm_retention_destruction_ledger`
    ADD COLUMN `destruction_reason` VARCHAR(500) DEFAULT NULL COMMENT '销毁原因' AFTER `unit_id`;
