/*
 * 描述: 创建 ELN 执行表单数据-异常批注表
 * 作者: yigaohui
 * 日期: 2025-12-05
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 创建表 bm_execute_form_data_annotation
 * 2. 创建必要索引
 */

CREATE TABLE IF NOT EXISTS `bm_execute_form_data_annotation` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `value` varchar(2000) DEFAULT NULL COMMENT '批注值',
  `value_extension` longtext NULL COMMENT '批注值扩展',
  `ext_info` longtext NULL COMMENT '扩展字段 后端使用',
  `inspection_order_id` bigint(20) DEFAULT NULL COMMENT '请验单id',
  `batch_no` varchar(100) DEFAULT NULL COMMENT '批号',
  `scheme_id` bigint(20) DEFAULT NULL COMMENT '方案ID',
  `scheme_version_id` bigint(20) DEFAULT NULL COMMENT '方案版本ID',
  `record_item_id` bigint(20) DEFAULT NULL COMMENT '记录项id',
  `record_version_id` bigint(20) DEFAULT NULL COMMENT '记录版本id',
  `record_id` bigint(20) DEFAULT NULL COMMENT '记录id',
  `task_id` bigint(20) DEFAULT NULL COMMENT '任务ID',
  `item_id` bigint(20) DEFAULT NULL COMMENT '检验项目ID',
  `item_config_id` bigint(20) DEFAULT NULL COMMENT '检验项目配置ID',
  `parameter_id` bigint(20) DEFAULT NULL COMMENT '检验分析项ID',
  `parameter_config_id` bigint(20) DEFAULT NULL COMMENT '检验分析项配置ID',
  `field_id` bigint(20) DEFAULT NULL COMMENT '组件id',
  `component_type` varchar(64) DEFAULT NULL COMMENT '组件类型',
  `is_system_create` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否系统创建',
  `operation_type` varchar(32) DEFAULT NULL COMMENT '操作类型',
  `operation_time` datetime DEFAULT NULL COMMENT '操作时间',
  `operation_user` varchar(64) DEFAULT NULL COMMENT '操作人',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` varchar(50) NOT NULL COMMENT '创建人',
  `update_by` varchar(50) NOT NULL COMMENT '更新人',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`),
  KEY `idx_inspection_order_id` (`inspection_order_id`),
  KEY `idx_record_item_id` (`record_item_id`),
  KEY `idx_field_id` (`field_id`),
  KEY `idx_operation_time` (`operation_time`),
  KEY `idx_batch_no` (`batch_no`),
  KEY `idx_scheme_id` (`scheme_id`),
  KEY `idx_scheme_version_id` (`scheme_version_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_order_item_field_time` (`inspection_order_id`, `record_item_id`, `field_id`, `operation_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ELN 执行表单数据-异常批注';



