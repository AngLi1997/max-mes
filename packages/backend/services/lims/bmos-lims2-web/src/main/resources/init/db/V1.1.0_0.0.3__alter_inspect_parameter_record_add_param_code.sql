/*
 * 描述: 为分析项-方法关联表新增 parameter_code 字段并移除 name 冗余
 * 作者: yigaohui
 * 日期: 2025-10-27
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 新增字段 parameter_code
 * 2. 删除字段 name
 */

ALTER TABLE lm_inspect_parameter_record
    ADD COLUMN parameter_code VARCHAR(100) NOT NULL COMMENT '分析项编码(冗余)' AFTER parameter_id;

ALTER TABLE lm_inspect_parameter_record
    DROP COLUMN name;


