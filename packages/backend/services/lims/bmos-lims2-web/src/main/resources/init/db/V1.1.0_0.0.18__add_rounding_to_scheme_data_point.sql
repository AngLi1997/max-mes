/*
 * 描述: 为方案数据点表增加时长舍入配置
 * 作者: yigaohui
 * 日期: 2025-10-31
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 在 lm_inspection_scheme_data_point 表新增 rounding_up 列（tinyint(1)）
 */

ALTER TABLE `lm_inspection_scheme_data_point`
    ADD COLUMN `rounding_up` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '时间类型时长舍入：1-向上；0-向下' AFTER `date_style`;


