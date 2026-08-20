/*
 * 描述: 方法版本生效时间字段
 * 作者: yigaohui
 * 日期: 2026-01-09
 * 环境: dev/test/prod
 * 变更内容:
 * 1. 为 bm_batch_record_version 表新增生效时间字段
 */

ALTER TABLE bm_batch_record_version
    ADD COLUMN  effective_time datetime NULL COMMENT '生效时间' AFTER state;

