-- 调整样品数量相关字段的精度为整数6位、小数5位（DECIMAL(11,5)）
-- 涵盖：计划取样量、实际取样量（样品数量）、分样数量（存储在子样品的样品数量上）

-- 检验取样信息表：计划取样量 planned_quantity
ALTER TABLE lm_inspection_sampling
    MODIFY COLUMN planned_quantity DECIMAL(11,5) NOT NULL COMMENT '计划取样量';

-- 样品表：计划取样量 plan_quantity、样品数量 quantity（保存实际取样量/分样后子样的数量）
ALTER TABLE lm_sample
    MODIFY COLUMN plan_quantity DECIMAL(11,5) NULL,
    MODIFY COLUMN quantity DECIMAL(11,5) NULL COMMENT '样品数量';

-- 样品台账：数量快照（保持一致的展示精度）
ALTER TABLE lm_sample_ledger
    MODIFY COLUMN quantity DECIMAL(11,5) NULL COMMENT '样品数量快照';

-- 回收相关：样品表与台账表的回收余量字段
ALTER TABLE lm_sample
    MODIFY COLUMN recycle_quantity DECIMAL(11,5) NULL COMMENT '回收余量';

ALTER TABLE lm_sample_ledger
    MODIFY COLUMN recycle_quantity DECIMAL(11,5) NULL COMMENT '回收量快照';

-- 删除重复的检验项目
drop index uk_detail_inspect_item on lm_inspection_scheme_sampling;


-- MySQL 8+ 语法
ALTER TABLE lm_inspection_entry_record
    MODIFY COLUMN value_number VARCHAR(100) NULL COMMENT '数值型结果（字符串）';

ALTER TABLE lm_inspection_entry_history
    MODIFY COLUMN old_value_number VARCHAR(100) NULL COMMENT '旧数值（字符串）',
    MODIFY COLUMN new_value_number VARCHAR(100) NULL COMMENT '新数值（字符串）';

