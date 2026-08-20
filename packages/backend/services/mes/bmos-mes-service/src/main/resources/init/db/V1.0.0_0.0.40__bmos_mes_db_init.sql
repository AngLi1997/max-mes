use bmos_mes;
set names utf8mb4;

alter table bm_ingredient_weigh_batch_process
    add procedure_step_model_id bigint null comment '工序步骤模型id' after storage_material_batch_id;

alter table bm_ingredient_weigh_batch_process
    add copy_version int null comment '复制版本' after procedure_step_model_id;

alter table bm_ingredient_weigh_batch_process
    add component_id bigint null comment '组件id' after copy_version;

alter table bm_ingredient_weigh_process
    drop column procedure_step_model_id;

alter table bm_ingredient_weigh_process
    drop column copy_version;

alter table bm_ingredient_weigh_process
    drop column component_id;