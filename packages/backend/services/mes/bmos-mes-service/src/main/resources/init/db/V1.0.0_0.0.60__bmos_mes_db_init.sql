alter table bm_procedure_condition_instance
    add condition_id bigint null comment '条件配置的id';
alter table bm_procedure_condition_instance_history
    add condition_id bigint null comment '条件配置的id';