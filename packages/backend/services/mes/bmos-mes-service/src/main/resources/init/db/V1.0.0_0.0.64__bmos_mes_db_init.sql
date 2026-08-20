alter table bm_procedure_task_instance change task_id procedure_step_model_id bigint not null comment '步骤模型id';
alter table bm_procedure_task_instance_history change task_id procedure_step_model_id bigint not null comment '步骤模型id';
alter table bm_procedure_condition_instance change step_task_id procedure_step_model_id bigint null comment '步骤模型id';
alter table bm_procedure_expression drop column step_task_id;
