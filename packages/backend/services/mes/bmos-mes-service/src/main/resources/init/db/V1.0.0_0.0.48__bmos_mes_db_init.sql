use bmos_mes;
set names utf8mb4;

alter table bm_process_version
    ADD COLUMN `production_stage_code` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL AFTER `state`;

DROP TABLE IF EXISTS `bm_process_production_line`;
CREATE TABLE `bm_process_production_line` (
                                              `id` bigint NOT NULL,
                                              `process_version_id` bigint NOT NULL COMMENT '工艺版本id',
                                              `production_line_id` bigint NOT NULL COMMENT '产线id',
                                              `process_id` bigint NOT NULL COMMENT '工艺id',
                                              `process_version` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '工艺版本',
                                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;