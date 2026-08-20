use bmos_mes;
set names utf8mb4;
set foreign_key_checks = 0;

DROP TABLE IF EXISTS `bm_charge_recycle`;
CREATE TABLE `bm_charge_recycle` (
                                     `id` bigint NOT NULL,
                                     `component_id` bigint DEFAULT NULL COMMENT '组件id',
                                     `product_plan_id` bigint DEFAULT NULL COMMENT '生产计划id',
                                     `record_item_id` bigint DEFAULT NULL COMMENT '记录项id',
                                     `record_version_id` bigint DEFAULT NULL COMMENT '记录项版本呢id',
                                     `procedure_step_model_id` bigint DEFAULT NULL COMMENT '工序步骤模型id',
                                     `reuse` tinyint(1) DEFAULT NULL COMMENT '是否复用',
                                     `copy_version` int DEFAULT NULL COMMENT '复制版本',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                     `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
                                     `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
                                     `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `bm_storage_material_charge_recycle`;
CREATE TABLE `bm_storage_material_charge_recycle` (
                                                      `id` bigint NOT NULL,
                                                      `material_id` bigint DEFAULT NULL COMMENT '物料id',
                                                      `material_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料名称',
                                                      `material_merge_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料编码',
                                                      `specification` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料规格',
                                                      `material_batch_no` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料批号',
                                                      `material_batch_id` bigint DEFAULT NULL COMMENT '物料批次id',
                                                      `storage_material_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料件好',
                                                      `storage_material_id` bigint DEFAULT NULL COMMENT '物料件id',
                                                      `quantity` decimal(20,10) DEFAULT NULL COMMENT '物料量',
                                                      `unit_id` bigint DEFAULT NULL COMMENT '单位id',
                                                      `operation_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作类型',
                                                      `operator_id` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人id',
                                                      `charge_recycle_component_id` bigint DEFAULT NULL COMMENT 'charge_recycle_component表主键id',
                                                      `equipment_id` bigint DEFAULT NULL COMMENT '设备id',
                                                      `equipment_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备名称',
                                                      `equipment_code` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备编码',
                                                      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                      `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                                      `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
                                                      `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
                                                      `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `bm_output_finished_product`;
CREATE TABLE `bm_output_finished_product` (
                                              `id` bigint NOT NULL,
                                              `product_id` bigint DEFAULT NULL COMMENT '产品id',
                                              `product_merge_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '成品编码',
                                              `product_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '成品名称',
                                              `product_batch_no` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '成品编码',
                                              `specification` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '成品规格',
                                              `component_id` bigint DEFAULT NULL COMMENT '组件id',
                                              `product_plan_id` bigint DEFAULT NULL COMMENT '生产计划id',
                                              `record_item_id` bigint DEFAULT NULL COMMENT '记录项id',
                                              `record_version_id` bigint DEFAULT NULL COMMENT '记录项版本呢id',
                                              `procedure_step_model_id` bigint DEFAULT NULL COMMENT '工序步骤模型id',
                                              `reuse` tinyint(1) DEFAULT NULL COMMENT '是否复用',
                                              `copy_version` int DEFAULT NULL COMMENT '复制版本',
                                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                              `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                              `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
                                              `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
                                              `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `bm_output_finished_product_result`;
CREATE TABLE `bm_output_finished_product_result` (
                                                     `id` bigint NOT NULL,
                                                     `output_finished_product_id` bigint DEFAULT NULL COMMENT 'output_finished_product_id',
                                                     `product_id` bigint DEFAULT NULL COMMENT '产品id',
                                                     `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '成品名称',
                                                     `product_merge_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '成品编码',
                                                     `specification` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '成品规格',
                                                     `product_batch_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '成品批号',
                                                     `single_quantity` decimal(20,10) DEFAULT NULL COMMENT '单间量',
                                                     `unit_id` bigint DEFAULT NULL COMMENT '单位id',
                                                     `number` int DEFAULT NULL COMMENT '件数',
                                                     `operator_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作人id',
                                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                     `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                                     `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
                                                     `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
                                                     `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `bm_requisition_plan`;
CREATE TABLE `bm_requisition_plan` (
                                       `id` bigint NOT NULL,
                                       `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '领料单名称',
                                       `component_id` bigint DEFAULT NULL COMMENT '组件id',
                                       `product_plan_id` bigint DEFAULT NULL COMMENT '生产计划id',
                                       `batch_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生产批号',
                                       `record_item_id` bigint DEFAULT NULL COMMENT '记录项id',
                                       `reuse` tinyint(1) DEFAULT NULL COMMENT '是否复用',
                                       `procedure_step_model_id` bigint DEFAULT NULL COMMENT '工序步骤模型id',
                                       `serial_no` int DEFAULT NULL COMMENT '流水号',
                                       `requisition_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '领料类型',
                                       `completed_plan` tinyint(1) DEFAULT NULL COMMENT '是否完成向仓库发送领料计划',
                                       `send_status` tinyint(1) DEFAULT NULL COMMENT '仓库发料状态',
                                       `received_id` bigint DEFAULT NULL COMMENT '领料接收确认组件id',
                                       `record_version_id` bigint DEFAULT NULL COMMENT '记录项版本id',
                                       `completed_receive` tinyint(1) DEFAULT NULL COMMENT '是否完成领料接收',
                                       `copy_version` int DEFAULT NULL COMMENT '复制版本',
                                       `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                       `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                       `create_time` datetime DEFAULT NULL,
                                       `update_time` datetime DEFAULT NULL,
                                       `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='领料计划表';

DROP TABLE IF EXISTS `bm_requisition_plan_reserved`;
CREATE TABLE `bm_requisition_plan_reserved` (
                                                `id` bigint NOT NULL,
                                                `requisition_plan_id` bigint NOT NULL COMMENT '领料单id',
                                                `formula_material_id` bigint NOT NULL COMMENT '配方物料id',
                                                `material_batch_id` bigint DEFAULT NULL COMMENT '物料批次id',
                                                `material_batch_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料批号',
                                                `planned_quantity` decimal(20,10) DEFAULT NULL COMMENT '计划量',
                                                `theoretical_quantity` decimal(20,10) DEFAULT NULL COMMENT '理论量',
                                                `user_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '计划人id',
                                                `unit_id` bigint DEFAULT NULL COMMENT '单位id',
                                                `expired_date` datetime DEFAULT NULL COMMENT '有效日期',
                                                `merge_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料编码',
                                                `specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料规格',
                                                `wms_material_id` bigint DEFAULT NULL COMMENT 'wms物料id',
                                                `material_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'wms物料名称',
                                                `hydration` decimal(10,2) DEFAULT NULL COMMENT '水分',
                                                `no_hydration_content` decimal(10,2) DEFAULT NULL COMMENT '无水含量',
                                                `supplier` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '供应商',
                                                `producer` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生产商',
                                                `origin_batch_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '原产批号',
                                                `origin_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '原厂编码',
                                                `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                `create_time` datetime DEFAULT NULL,
                                                `update_time` datetime DEFAULT NULL,
                                                `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='领料单物料批次';

DROP TABLE IF EXISTS `bm_requisition_received`;
CREATE TABLE `bm_requisition_received` (
                                           `id` bigint NOT NULL,
                                           `component_id` bigint DEFAULT NULL COMMENT '组件id',
                                           `product_plan_id` bigint DEFAULT NULL COMMENT '生产计划id',
                                           `record_item_id` bigint DEFAULT NULL COMMENT '记录项id',
                                           `record_version_id` bigint DEFAULT NULL COMMENT '记录项版本呢id',
                                           `procedure_step_model_id` bigint DEFAULT NULL COMMENT '工序步骤模型id',
                                           `reuse` tinyint(1) DEFAULT NULL COMMENT '是否复用',
                                           `copy_version` int DEFAULT NULL COMMENT '复制版本',
                                           `requisition_id` bigint DEFAULT NULL COMMENT '绑定领料单id',
                                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                           `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                           `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
                                           `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
                                           `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `bm_requisition_received_batch`;
CREATE TABLE `bm_requisition_received_batch` (
                                                 `id` bigint NOT NULL,
                                                 `requisition_plan_id` bigint DEFAULT NULL COMMENT '领料单id',
                                                 `inventory_batch_id` bigint DEFAULT NULL COMMENT '仓库货品批次id',
                                                 `inventory_batch_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '仓库货品批次号',
                                                 `quantity` decimal(20,10) DEFAULT NULL COMMENT '发料总量',
                                                 `unit_id` bigint DEFAULT NULL COMMENT '单位id',
                                                 `factory_batch_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '原厂批号',
                                                 `produce_date` date DEFAULT NULL COMMENT '生产日期',
                                                 `expired_date` date DEFAULT NULL COMMENT '有效日期',
                                                 `hydration` decimal(20,10) DEFAULT NULL COMMENT '水分',
                                                 `no_hydration_content` decimal(20,10) DEFAULT NULL COMMENT '无水含量',
                                                 `report_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '报告单编号',
                                                 `licence_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '放行单编号',
                                                 `cargo_merge_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品混合编码',
                                                 `cargo_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品名称',
                                                 `formula_material_id` bigint DEFAULT NULL COMMENT '配方物料id',
                                                 `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                 `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                 `create_time` datetime DEFAULT NULL,
                                                 `update_time` datetime DEFAULT NULL,
                                                 `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='领料计划--代领批次';

DROP TABLE IF EXISTS `bm_requisition_received_material`;
CREATE TABLE `bm_requisition_received_material` (
                                                    `id` bigint NOT NULL,
                                                    `requisition_plan_id` bigint DEFAULT NULL COMMENT '领料单id',
                                                    `inventory_batch_id` bigint DEFAULT NULL COMMENT '货品批次id',
                                                    `platform_material_id` bigint DEFAULT NULL COMMENT '平台物料id',
                                                    `inventory_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料件号',
                                                    `quantity` decimal(20,10) DEFAULT NULL COMMENT '发放物料量',
                                                    `unit_id` bigint DEFAULT NULL COMMENT '单位id',
                                                    `cargo_position_id` bigint DEFAULT NULL COMMENT '暂存货位id',
                                                    `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                    `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                    `create_time` datetime DEFAULT NULL,
                                                    `update_time` datetime DEFAULT NULL,
                                                    `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                                                    `received_batch_id` bigint DEFAULT NULL COMMENT 'bm_requisition_received_batch表id',
                                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='领料计划 待领物料件';



set foreign_key_checks = 1;