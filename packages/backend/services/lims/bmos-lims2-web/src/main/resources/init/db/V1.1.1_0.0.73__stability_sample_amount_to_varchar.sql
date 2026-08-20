-- 稳定性方案检验计划：整体取样量改为字符串，支持灵活精度
ALTER TABLE `lm_stability_scheme_plan`
    MODIFY COLUMN `total_sample_amount` VARCHAR(50) DEFAULT NULL COMMENT '整体取样量';

-- 稳定性方案时间点：取样量改为字符串
ALTER TABLE `lm_stability_scheme_plan_timepoint`
    MODIFY COLUMN `sample_amount` VARCHAR(50) DEFAULT NULL COMMENT '取样量';

-- 稳定性考察计划样品：计划/实际取样量改为字符串
ALTER TABLE `lm_stability_plan_sample`
    MODIFY COLUMN `planned_sample_amount` VARCHAR(50) DEFAULT NULL COMMENT '计划取样量（来自方案检验计划的整体取样量）',
    MODIFY COLUMN `actual_sample_amount`  VARCHAR(50) DEFAULT NULL COMMENT '实际取样量';

-- 稳定性计划时间点任务：计划取样量改为字符串，并新增实际取样量字段
ALTER TABLE `lm_stability_plan_timepoint_task`
    MODIFY COLUMN `sample_amount` VARCHAR(50) DEFAULT NULL COMMENT '计划取样量（来自方案时间点配置，创建后不再更改）';

ALTER TABLE `lm_stability_plan_timepoint_task`
    ADD COLUMN `actual_sample_amount` VARCHAR(50) DEFAULT NULL COMMENT '实际取样量（取样提交时填写）' AFTER `sample_unit`,
    ADD COLUMN `actual_sample_unit`   VARCHAR(50) DEFAULT NULL COMMENT '实际取样量单位'               AFTER `actual_sample_amount`;
