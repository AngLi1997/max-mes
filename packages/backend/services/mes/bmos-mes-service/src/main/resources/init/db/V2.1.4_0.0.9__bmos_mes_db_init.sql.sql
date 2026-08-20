alter table bm_material_trace_template_procedure_step
    add process_version varchar(100) null comment '工艺版本号' after process_name;