alter table bm_material_trace_history
    add source_product_plan_id bigint null comment '产出计划id' after trace_type;

alter table bm_material_trace_history
    add source_batch_no varchar(100) null comment '产出生产批号' after source_product_plan_id;
