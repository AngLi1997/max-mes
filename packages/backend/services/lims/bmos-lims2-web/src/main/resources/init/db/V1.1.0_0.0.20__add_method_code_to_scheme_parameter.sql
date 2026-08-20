/*
 * 描述: 方案参数表增加方法编码字段 record_code
 * 作者: yigaohui
 * 日期: 2025-10-31
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 在 lm_inspection_scheme_parameter 表新增 record_code 字段
 */

/*
 * 描述: 方案参数表增加方法ID
 * 作者: yigaohui
 * 日期: 2025-10-29
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 增加 record_id 字段
 * 2. 增加索引 idx_record_id
 */

ALTER TABLE `lm_inspection_scheme_parameter`
    ADD COLUMN `record_id` bigint(20) NULL COMMENT '分析方法ID' AFTER `parameter_id`;

ALTER TABLE `lm_inspection_scheme_parameter`
    ADD COLUMN `record_version_id` bigint(20) NULL COMMENT '分析方法ID' AFTER `parameter_id`;


ALTER TABLE `lm_inspection_scheme_parameter`
  ADD COLUMN `record_code` varchar(100) NULL COMMENT '分析方法编码' AFTER `record_id`;

ALTER TABLE `lm_inspection_scheme_parameter`
    ADD COLUMN `record_item_id` bigint(100);

-- 可选：根据使用场景添加索引
-- ALTER TABLE `lm_inspection_scheme_parameter`
--   ADD INDEX `idx_record_code` (`record_code`);


