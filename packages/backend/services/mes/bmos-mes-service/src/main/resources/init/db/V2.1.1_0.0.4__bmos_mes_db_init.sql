alter table bm_material_field
    change field_code field varchar(64) null comment '字段';

alter table bm_material_batch_field
    change field_code field varchar(64) null comment '字段';