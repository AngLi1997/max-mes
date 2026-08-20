-- 1. 将 lm_sample.inspection_order_id 改为可空（稳定性整体取样无检验单）
ALTER TABLE lm_sample MODIFY COLUMN inspection_order_id BIGINT NULL COMMENT '检验单ID（稳定性整体取样时为空）';

-- 2. 新增 stability_plan_sample_id，用于标识该样品来源于稳定性整体取样
ALTER TABLE lm_sample
    ADD COLUMN stability_plan_sample_id BIGINT NULL COMMENT '稳定性考察计划样品ID（整体取样时关联 lm_stability_plan_sample.id）'
    AFTER inspection_order_id;
