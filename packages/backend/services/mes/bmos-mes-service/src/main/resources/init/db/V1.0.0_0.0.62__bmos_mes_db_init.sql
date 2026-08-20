alter table bm_procedure_expression
    add procedure_step_model_id bigint null after step_task_id;

alter table bm_procedure_condition
    add procedure_step_model_id bigint null after id;