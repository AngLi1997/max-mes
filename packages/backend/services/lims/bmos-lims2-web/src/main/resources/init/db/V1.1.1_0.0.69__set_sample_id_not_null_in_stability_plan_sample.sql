-- lm_stability_plan_sample.sample_id 理论上在记录创建时即已关联 lm_sample，强制非空约束
-- 注意：执行前请确认不存在历史遗留的 sample_id 为 NULL 的数据
ALTER TABLE lm_stability_plan_sample
    MODIFY COLUMN sample_id BIGINT NOT NULL COMMENT '关联的 lm_sample 记录ID（整体取样时创建的父样品）';
