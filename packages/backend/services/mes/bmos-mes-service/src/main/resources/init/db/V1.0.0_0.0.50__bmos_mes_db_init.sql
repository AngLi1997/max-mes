alter table bm_execute_form_data
    modify value_extension text collate utf8mb4_bin null comment 'value扩展字段(前端使用)';

alter table bm_execute_form_data
    add ext_info text null comment '扩展字段（后端使用）' after value_extension;

alter table bm_reserve_component_material
    ADD UNIQUE (storage_material_id);