alter table bm_procedure_step_model
    add `sort` int DEFAULT NULL COMMENT '排序号';
alter table bm_procedure_model
    add `sort` int DEFAULT NULL COMMENT '排序号';