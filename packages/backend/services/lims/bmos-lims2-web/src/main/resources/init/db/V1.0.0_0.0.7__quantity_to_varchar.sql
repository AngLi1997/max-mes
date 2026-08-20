-- 目的：将样品数量类字段由 DECIMAL 改为 VARCHAR，避免整数返回时带多余的尾随0
-- 规则：存储为字符串，接口层限制“整数最多6位，小数最多5位”；计算时转为 BigDecimal

-- 注意：若历史数据存在 DECIMAL 到字符串的直接转换，MySQL 会按默认格式转为字符，
-- 如需去除尾随0，请在应用层格式化；本迁移脚本保持无损转换。

-- 1) 检验取样计划表 lm_inspection_sampling：planned_quantity -> VARCHAR
ALTER TABLE lm_inspection_sampling
    MODIFY COLUMN planned_quantity VARCHAR(32) NOT NULL COMMENT '计划取样量(字符串)';

-- 2) 样品表 lm_sample：plan_quantity / quantity / recycle_quantity -> VARCHAR
ALTER TABLE lm_sample
    MODIFY COLUMN plan_quantity VARCHAR(32) NULL COMMENT '计划取样量(字符串)',
    MODIFY COLUMN quantity VARCHAR(32) NULL COMMENT '样品数量(字符串)',
    MODIFY COLUMN recycle_quantity VARCHAR(32) NULL COMMENT '回收余量(字符串)';

-- 3) 样品台账 lm_sample_ledger：quantity / recycle_quantity -> VARCHAR
ALTER TABLE lm_sample_ledger
    MODIFY COLUMN quantity VARCHAR(32) NULL COMMENT '样品数量快照(字符串)',
    MODIFY COLUMN recycle_quantity VARCHAR(32) NULL COMMENT '回收量快照(字符串)';

-- 4) 录入记录相关(如 value_number 已在 V1.0.0_0.0.5 迁移为 VARCHAR)，此处无需重复

-- 备注：如存在依赖 DECIMAL 的索引或函数，需要配套调整。
drop index uk_template_scheme_version on lm_report_template_scheme_bind;



