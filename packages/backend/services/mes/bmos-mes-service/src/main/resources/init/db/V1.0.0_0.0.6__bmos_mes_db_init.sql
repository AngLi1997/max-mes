use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 添加工序填报、工艺结论填报表
CREATE TABLE `bm_procedure_confirm` (
  `id` bigint NOT NULL COMMENT '主键id',
  `procedure_name` varchar(64) NOT NULL COMMENT '工序名称',
  `confirm_time` datetime DEFAULT NULL COMMENT '结论填报时间',
  `procedure_time` datetime DEFAULT NULL COMMENT '工序完成时间',
  `process_confirm_id` bigint NOT NULL COMMENT '工艺结论id',
  `process_id` bigint DEFAULT NULL COMMENT '工艺id',
  `confirm_opinion` varchar(64) DEFAULT NULL COMMENT '审批结论',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='工序审批结论表';

CREATE TABLE `bm_process_confirm` (
  `id` bigint NOT NULL COMMENT '主键id',
  `product_id` bigint NOT NULL COMMENT '产品id',
  `product_name` varchar(255) DEFAULT NULL COMMENT '产品名称',
  `product_code` varchar(1000) NOT NULL COMMENT '产品编码',
  `product_specification` varchar(100) DEFAULT NULL COMMENT '产品规格',
  `process_id` bigint NOT NULL COMMENT '工艺id',
  `process_name` varchar(100) DEFAULT NULL COMMENT '工艺名称',
  `plan_batch_no` varchar(64) DEFAULT NULL COMMENT '生产批号',
  `start_time` datetime DEFAULT NULL COMMENT '生产开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '生产结束时间时间',
  `confirm_time` datetime DEFAULT NULL COMMENT '填报结论时间',
  `instance_id` varchar(64) NOT NULL COMMENT '流程实例id',
  `confirm_opinion` varchar(64) DEFAULT NULL COMMENT '审批结论',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='工艺审核结论确定表';

SET FOREIGN_KEY_CHECKS = 1;
