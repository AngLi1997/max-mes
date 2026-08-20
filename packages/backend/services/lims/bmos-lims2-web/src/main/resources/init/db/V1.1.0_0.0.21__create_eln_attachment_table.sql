/*
 * 描述: 创建 ELN 执行附件表
 * 作者: yigaohui
 * 日期: 2025-10-31
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 创建表 lm_eln_attachment
 * 2. 创建必要索引
 */

CREATE TABLE IF NOT EXISTS `lm_eln_attachment` (
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `type` varchar(50) DEFAULT NULL COMMENT '类型',
    `path` varchar(500) DEFAULT NULL COMMENT '文件路径',
    `inspection_order_id` bigint(20) DEFAULT NULL COMMENT '请验单id',
    `inspection_order_no` varchar(64) DEFAULT NULL COMMENT '请验单编号',
    `batch_no` varchar(100) DEFAULT NULL COMMENT '批号',
    `record_id` bigint(20) DEFAULT NULL COMMENT '方法ID',
    `record_version_id` bigint(20) DEFAULT NULL COMMENT '方法版本ID',
    `scheme_id` bigint(20) DEFAULT NULL COMMENT '方案ID',
    `scheme_version_id` bigint(20) DEFAULT NULL COMMENT '方案版本ID',
    `parameter_config_id` bigint(20) DEFAULT NULL COMMENT '参数配置ID',
    `attachment_type` varchar(50) DEFAULT NULL COMMENT '附件类型',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注信息',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '更新时间',
    `create_by` varchar(50) NOT NULL COMMENT '创建人',
    `update_by` varchar(50) NOT NULL COMMENT '更新人',
    `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-否 1-是',
    PRIMARY KEY (`id`),
    KEY `idx_inspection_order_id` (`inspection_order_id`),
    KEY `idx_inspection_order_no` (`inspection_order_no`),
    KEY `idx_batch_no` (`batch_no`),
    KEY `idx_record_id` (`record_id`),
    KEY `idx_record_version_id` (`record_version_id`),
    KEY `idx_scheme_id` (`scheme_id`),
    KEY `idx_scheme_version_id` (`scheme_version_id`),
    KEY `idx_parameter_config_id` (`parameter_config_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ELN 执行附件表';


