alter table bm_procedure_step
    add type varchar(32) not null;

alter table bm_procedure_step_model
    add step_type varchar(32) null comment '步骤类型' after node_id;

update bm_procedure_step set type = 'STEP' where type is null or type = '';

update bm_procedure_step_model set step_type = 'STEP' where step_type is null or step_type = '';