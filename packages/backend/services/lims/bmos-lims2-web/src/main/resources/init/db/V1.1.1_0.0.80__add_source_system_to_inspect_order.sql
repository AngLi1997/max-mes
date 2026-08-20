-- lm_inspection_order 新增 source_system 字段（上游来源系统：MES / WMS）
-- 设计：specs/2026-06-15-bmos-wms-lims-integration-design.md §6
-- 说明：默认 'MES' 兼容历史数据；audit 流程结束后据此选择回调到 MES 或 WMS。

ALTER TABLE `lm_inspection_order`
    ADD COLUMN `source_system` VARCHAR(16) DEFAULT 'MES' COMMENT '上游来源系统：MES / WMS（默认 MES，兼容旧数据）'
    AFTER `scheme_source`;

UPDATE `lm_inspection_order` SET `source_system` = 'MES' WHERE `source_system` IS NULL;
