-- =============================================
-- 业务参数配置 - 检验记录作业空值符号
-- 描述:用于配置检验记录作业中空值的显示符号
-- 创建时间:2024-10-22
-- =============================================
INSERT INTO bmos_platform.bp_business_parameter (`id`, `code`, `name`, `value`, `value_type`, `business_type`, `belong`, `description`, `sort`, `value_range`, `is_display`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (130002, 'lims.record.empty-data', '检验记录作业空值符号', 'N/A', 'STRING', 'BUSINESS', '检验', '检验记录作业空值符号', 130020, '', 1, '1', '1', NOW(), NOW(), 0) ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `value` = VALUES(`value`), `value_type` = VALUES(`value_type`), `business_type` = VALUES(`business_type`), `belong` = VALUES(`belong`), `description` = VALUES(`description`), `sort` = VALUES(`sort`), `value_range` = VALUES(`value_range`), `is_display` = VALUES(`is_display`), `update_by` = VALUES(`update_by`), `update_time` = NOW();

-- =============================================
-- 业务参数配置 - 方法管理记录页边距
-- 描述:方法管理记录页边距，单位：毫米
-- 创建时间:2024-10-22
-- =============================================
INSERT INTO bmos_platform.bp_business_parameter (`id`, `code`, `name`, `value`, `value_type`, `business_type`, `belong`, `description`, `sort`, `value_range`, `is_display`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (130003, 'lims.record.margin', '方法管理记录页边距', '{"left":"10", "right":"10", "top":"10", "bottom":"10"}', 'JSON', 'BUSINESS', '检验', '方法管理记录页边距，单位：毫米', 130030, '', 1, '1', '1', NOW(), NOW(), 0) ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `value` = VALUES(`value`), `value_type` = VALUES(`value_type`), `business_type` = VALUES(`business_type`), `belong` = VALUES(`belong`), `description` = VALUES(`description`), `sort` = VALUES(`sort`), `value_range` = VALUES(`value_range`), `is_display` = VALUES(`is_display`), `update_by` = VALUES(`update_by`), `update_time` = NOW();

-- =============================================
-- 业务参数配置 - 结论判定选项
-- 描述:用于配置检验结论判定的可选项
-- 创建时间:2024-10-22
-- =============================================
INSERT INTO bmos_platform.bp_business_parameter (`id`, `code`, `name`, `value`, `value_type`, `business_type`, `belong`, `description`, `sort`, `value_range`, `is_display`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (130001, 'lims.conclusion.options', '结论判定选项', '[{"label":"不符合规定","value":false},{"label":"符合规定","value":true}]', 'JSON', 'BUSINESS', '检验', '结论判定选项', 130010, '', 1, '1', '1', NOW(), NOW(), 0) ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `value` = VALUES(`value`), `value_type` = VALUES(`value_type`), `business_type` = VALUES(`business_type`), `belong` = VALUES(`belong`), `description` = VALUES(`description`), `sort` = VALUES(`sort`), `value_range` = VALUES(`value_range`), `is_display` = VALUES(`is_display`), `update_by` = VALUES(`update_by`), `update_time` = NOW();