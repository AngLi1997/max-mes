-- 为检验项目表添加系统内置标识字段
ALTER TABLE lm_inspect_item
    ADD COLUMN is_system TINYINT(1) DEFAULT 0 COMMENT '是否为系统内置项目（0-否，1-是，系统内置项目不在前端列表显示）' AFTER remark;

-- 为检验单表添加留样相关字段
ALTER TABLE lm_inspection_order
    ADD COLUMN retention_required TINYINT(1) DEFAULT 0 COMMENT '是否需要留样（0-否，1-是）' AFTER modify_count,
    ADD COLUMN retention_expiry_date DATE NULL COMMENT '有效期至（留样时必填）' AFTER retention_required;

-- 插入留样检验项目（系统内置，不在前端显示）
-- 使用固定的雪花ID: 2000000000000000001
-- 参见常量定义: InspectItemConstants.RETENTION_INSPECT_ITEM_ID
-- 参见常量定义: InspectItemConstants.RETENTION_INSPECT_ITEM_CODE
-- 参见常量定义: InspectItemConstants.RETENTION_INSPECT_ITEM_NAME
INSERT INTO lm_inspect_item (id, code, name, remark, is_system, is_deleted, create_time, create_by, update_time, update_by)
SELECT
    2000000000000000001,
    'SAMPLE_RETENTION',
    '留样',
    '特殊检验项目：用于标识样品需要进行留样保存。此为系统内置项目，不在前端列表显示',
    1,  -- 标记为系统内置项目
    0,
    NOW(),
    'system',
    NOW(),
    'system'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM lm_inspect_item WHERE code = 'SAMPLE_RETENTION'
);
