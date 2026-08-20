use bmos_mes;
set names utf8mb4;
set foreign_key_checks = 0;
alter table bm_ingredient_weigh_process change procedure_step_id procedure_step_model_id bigint null comment '工序步骤模型id';
alter table bm_output_weigh_process change procedure_step_id procedure_step_model_id bigint null comment '工序步骤模型id';
set foreign_key_checks = 1;