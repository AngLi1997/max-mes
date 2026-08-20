/*
 * 描述: 判定配置表增加数据点类型字段
 * 作者: yigaohui
 * 日期: 2026-01-12
 * 环境: dev/test/prod
 * 变更内容:
 * 1. lm_inspection_scheme_judgment 增加 point_type 字段，用于记录判定绑定数据点的类型
 */
ALTER TABLE `lm_inspection_scheme_judgment`
    ADD COLUMN `point_type` VARCHAR(32) NULL COMMENT '数据点类型：NUMBER/TEXT/OPTION/TIME' AFTER `data_point_id`;

