alter table bm_material_trace_history
    add process_version varchar(100) null comment '工艺版本' after process_id;

update bm_material_trace_history set process_version = (select process_version from bm_product_plan where id = bm_material_trace_history.product_plan_id);