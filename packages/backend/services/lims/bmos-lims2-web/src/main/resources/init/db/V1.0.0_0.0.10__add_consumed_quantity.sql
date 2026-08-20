-- 为样品与样品台账增加消耗量字段
ALTER TABLE lm_sample
    ADD COLUMN consumed_quantity VARCHAR(64) NULL COMMENT '消耗量（字符串）' AFTER recycle_remark;

ALTER TABLE lm_sample_ledger
    ADD COLUMN consumed_quantity VARCHAR(64) NULL COMMENT '消耗量快照（字符串）' AFTER recycle_quantity;


