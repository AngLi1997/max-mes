-- 创建操作历史表 bm_log_operation（如果不存在）
CREATE TABLE IF NOT EXISTS `bm_log_operation` (
  `id` bigint NOT NULL,
  `business_id` bigint NOT NULL COMMENT '业务数据id',
  `module` varchar(64) NOT NULL COMMENT '业务模块',
  `operation_type` varchar(32) NOT NULL COMMENT '操作类型',
  `remark` varchar(255) NULL COMMENT '备注',
  `create_by` varchar(64) NULL,
  `update_by` varchar(64) NULL,
  `create_time` datetime NULL,
  `update_time` datetime NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `node_name` varchar(64) NULL COMMENT '审批节点名称',
  `comment` varchar(255) NULL COMMENT '审批节点名称',
  `detail` varchar(5000) NULL COMMENT '历史详情',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作历史日志';


