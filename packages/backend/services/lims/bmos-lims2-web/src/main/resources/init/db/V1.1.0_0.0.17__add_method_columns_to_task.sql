/*
 * 描述: 为任务表增加方法相关字段（仅ELN执行方式使用）
 * 作者: yigaohui
 * 日期: 2025-11-04
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 在 lm_task 表新增 record_id、record_version_id 两个列
 */

-- 新增字段
ALTER TABLE `lm_task`
    ADD COLUMN `execute_method` varchar(20) DEFAULT NULL COMMENT '执行方式：LIMS/ELN' AFTER `is_reportable`;

ALTER TABLE `lm_task`
    ADD COLUMN `record_id` bigint(20) DEFAULT NULL COMMENT 'ELN方法ID' AFTER `execute_method`,
    ADD COLUMN `record_version_id` bigint(20) DEFAULT NULL COMMENT 'ELN方法生效版本ID' AFTER `record_id`;


