/*
 * 描述: 为 lm_inspect_parameter_record 新增记录绑定字段及索引
 * 作者: yigaohui
 * 日期: 2025-10-27
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 新增字段 record_id 记录绑定的批记录ID
 * 2. 新增索引 idx_record_id(record_id)
 */

ALTER TABLE lm_inspect_parameter_record
    ADD COLUMN record_id BIGINT NULL COMMENT '批记录ID -> bm_batch_record.id' AFTER id;

ALTER TABLE lm_inspect_parameter_record
    ADD KEY idx_record_id (record_id);


