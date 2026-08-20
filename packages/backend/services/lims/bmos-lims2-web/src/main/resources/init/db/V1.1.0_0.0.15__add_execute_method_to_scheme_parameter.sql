/*
 * 描述: 为方案分析项配置表增加执行方式字段
 * 作者: yigaohui
 * 日期: 2025-10-28
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 新增列 execute_method，取值 LIMS/ELN
 */

ALTER TABLE `lm_inspection_scheme_parameter`
    ADD COLUMN `execute_method` varchar(20) DEFAULT NULL COMMENT '执行方式：LIMS/ELN' AFTER `final_expression`;

-- 可选：为历史数据设置默认值（若需要）
-- UPDATE `lm_inspection_scheme_parameter` SET `execute_method` = 'LIMS' WHERE `execute_method` IS NULL;


