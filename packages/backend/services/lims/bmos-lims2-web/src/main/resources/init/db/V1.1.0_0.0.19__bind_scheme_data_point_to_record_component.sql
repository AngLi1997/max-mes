/*
 * 描述: 方案数据点绑定记录组件，新增记录id、记录版本id、组件id 三字段
 * 作者: yigaohui
 * 日期: 2025-10-31
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 为 lm_inspection_scheme_data_point 表新增 record_id、record_version_id、component_id
 */

-- 新增字段：记录id
ALTER TABLE lm_inspection_scheme_data_point
    ADD COLUMN  record_id BIGINT NULL COMMENT '记录id';

-- 新增字段：记录版本id
ALTER TABLE lm_inspection_scheme_data_point
    ADD COLUMN  record_version_id BIGINT NULL COMMENT '记录版本id';

-- 新增字段：记录组件id（bm_batch_record_component.id）
ALTER TABLE lm_inspection_scheme_data_point
    ADD COLUMN  component_id BIGINT NULL COMMENT '记录组件id';

-- 新增字段：fieldId（记录组件的字段id）
ALTER TABLE lm_inspection_scheme_data_point
    ADD COLUMN field_id BIGINT NULL COMMENT '记录字段id(fieldId)';

-- 新增字段：记录项id（bm_batch_record_item.id）
ALTER TABLE lm_inspection_scheme_data_point
    ADD COLUMN record_item_id BIGINT NULL COMMENT '记录项id';

-- 可选：联合索引提升按记录/版本查询性能
CREATE INDEX  idx_scheme_dp_record_version
    ON lm_inspection_scheme_data_point (record_id, record_version_id);


