/*
 * 描述: 为任务表新增样品审核人与审核时间字段
 * 作者: yigaohui
 * 日期: 2025-10-28
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 新增列 sample_audit_by VARCHAR(50)
 * 2. 新增列 sample_audit_time DATETIME
 */

-- 幂等添加列：样品审核人
ALTER TABLE `lm_task`
ADD COLUMN `sample_audit_by` varchar(50) DEFAULT NULL COMMENT '样品审核人ID' AFTER `reviewed_time`;

-- 幂等添加列：样品审核时间
ALTER TABLE `lm_task`
ADD COLUMN `sample_audit_time` datetime DEFAULT NULL COMMENT '样品审核时间' AFTER `sample_audit_by`;


