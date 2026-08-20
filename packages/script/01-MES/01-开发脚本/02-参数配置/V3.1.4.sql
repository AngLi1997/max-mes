-- =============================================
-- 业务参数配置 - MES对接LIMS配置
-- 描述:enabled是否对接LIMS，type取THIRD_PARTY(三方)/BMOS(自研)。
--      默认 {"enabled":false,"type":"THIRD_PARTY"} 上线即与现状一致，按需开启。
-- 创建时间:2026-06-10
-- =============================================
INSERT INTO bmos_platform.bp_business_parameter (`id`, `code`, `name`, `value`, `value_type`, `business_type`, `belong`, `description`, `sort`, `value_range`, `is_display`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (130004, 'inspect.lims.config', 'MES对接LIMS配置', '{"enabled":false,"type":"THIRD_PARTY"}', 'JSON', 'BUSINESS', 'MES', 'MES对接LIMS配置：enabled是否对接，type取THIRD_PARTY(三方)/BMOS(自研)', 130040, '', 1, '1', '1', NOW(), NOW(), 0) ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `value` = VALUES(`value`), `value_type` = VALUES(`value_type`), `business_type` = VALUES(`business_type`), `belong` = VALUES(`belong`), `description` = VALUES(`description`), `sort` = VALUES(`sort`), `value_range` = VALUES(`value_range`), `is_display` = VALUES(`is_display`), `update_by` = VALUES(`update_by`), `update_time` = NOW();
