use bmos_mes;
set names utf8mb4;

alter table bm_procedure_step_config
    add record_version_id bigint null comment '批记录版本id' after record_item_id;