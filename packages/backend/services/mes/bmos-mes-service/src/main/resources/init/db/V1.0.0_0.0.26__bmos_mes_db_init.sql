use bmos_mes;
set names utf8mb4;
set foreign_key_checks = 0;

DROP TABLE IF EXISTS `bm_weigh_log`;
CREATE TABLE `bm_weigh_log` (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `material_id` bigint DEFAULT NULL COMMENT '物料id',
                                `material_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料名称',
                                `material_merge_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料编码',
                                `material_type` tinyint(1) DEFAULT NULL COMMENT '物料类型',
                                `storage_material_id` bigint DEFAULT NULL COMMENT '物料件id',
                                `material_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料件号',
                                `material_batch_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料批号',
                                `material_batch_id` bigint DEFAULT NULL COMMENT '物料批次id',
                                `net_weight` decimal(20,10) DEFAULT NULL COMMENT '净重',
                                `tare_weight` decimal(20,10) DEFAULT NULL COMMENT '皮重',
                                `gross_weight` decimal(20,10) DEFAULT NULL COMMENT '毛重',
                                `unit_id` bigint DEFAULT NULL COMMENT '单位id',
                                `unit_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单位名称',
                                `weigh_type` tinyint DEFAULT NULL COMMENT '称量类型',
                                `weigher_id` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '称量人id',
                                `weigher_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '称量人名称',
                                `weigher_login_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '称量人登录名称',
                                `re_checker_id` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '复核人id',
                                `re_checker_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '复核人名称',
                                `re_checker_login_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '复核人登录名',
                                `weigh_time` datetime DEFAULT NULL COMMENT '称量时间',
                                `equipment_id` bigint DEFAULT NULL COMMENT '设备id',
                                `equipment_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备名称',
                                `equipment_code` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备编号·',
                                `equipment_status` tinyint(1) DEFAULT NULL COMMENT '设备状态',
                                `equipment_expire_date` date DEFAULT NULL COMMENT '校准日期',
                                `product_id` bigint DEFAULT NULL COMMENT '产品id',
                                `product_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品名称',
                                `product_merge_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品编码',
                                `product_batch_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生产批号',
                                `product_plan_id` bigint DEFAULT NULL COMMENT '生产批次id',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

set foreign_key_checks = 1;