-- 为样品台账增加状态字段
ALTER TABLE lm_sample_ledger
    ADD COLUMN status TINYINT NULL COMMENT '样品状态（0-未取样，1-取样，2-接收，3-分样，4-领取，5-回收，6-处理）' AFTER unit_id;


