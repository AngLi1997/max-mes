/*
 * 描述: 判定配置表增加数据点类型字段
 * 作者: yigaohui
 * 日期: 2026-01-12
 * 环境: dev/test/prod
 * 变更内容:
 * 1. lm_inspection_scheme_judgment 增加 point_type 字段，用于记录判定绑定数据点的类型
 */
drop index index_name_delete on bm_batch_record;

create unique index index_code_delete
    on bm_batch_record (is_deleted, code)
    comment '唯一索引';
