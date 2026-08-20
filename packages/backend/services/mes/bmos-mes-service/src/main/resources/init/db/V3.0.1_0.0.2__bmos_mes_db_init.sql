-- 检验单表添加自研LIMS检验方案字段
alter table bm_inspect
    add scheme_id bigint null comment '检验方案id（自研LIMS路径）' after unit_id;
alter table bm_inspect
    add scheme_version_id bigint null comment '检验方案版本id（自研LIMS路径）' after scheme_id;
alter table bm_inspect modify inspect_no varchar(64) null comment '请验单号';
