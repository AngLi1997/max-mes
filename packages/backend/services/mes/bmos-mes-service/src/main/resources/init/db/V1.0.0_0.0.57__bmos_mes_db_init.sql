alter table bm_procedure_condition_instance
    add step_task_id bigint null comment '任务或者工步id';

alter table bm_procedure_condition_instance_history
    add step_task_id bigint null comment '任务或者工步id';

