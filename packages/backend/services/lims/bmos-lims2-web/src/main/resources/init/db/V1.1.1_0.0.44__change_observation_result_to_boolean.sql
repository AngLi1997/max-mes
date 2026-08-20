-- 修改留样观察结果字段类型：从 VARCHAR 改为 TINYINT(1) 布尔值
-- true(1) = 符合, false(0) = 不符合

-- 修改留样观察任务表的观察结果字段
ALTER TABLE lm_retention_observation_task
    MODIFY COLUMN observation_result TINYINT(1) COMMENT '观察结果（0-不符合，1-符合）';

-- 修改留样观察台账表的观察结果字段
ALTER TABLE lm_retention_observation_ledger
    MODIFY COLUMN observation_result TINYINT(1) NOT NULL COMMENT '观察结果（0-不符合，1-符合）';
