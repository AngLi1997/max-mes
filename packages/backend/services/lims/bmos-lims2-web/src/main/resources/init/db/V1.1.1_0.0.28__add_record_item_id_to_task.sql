/*
 * 描述: 为任务表添加 record_item_id 列（ELN方法项ID）
 * 作者: yigaohui
 * 日期: 2025-11-20
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 在 lm_task 表新增字段 record_item_id
 */

ALTER TABLE `lm_task`
ADD COLUMN `record_item_id` bigint(20) DEFAULT NULL COMMENT 'ELN方法项ID' AFTER `record_version_id`;

