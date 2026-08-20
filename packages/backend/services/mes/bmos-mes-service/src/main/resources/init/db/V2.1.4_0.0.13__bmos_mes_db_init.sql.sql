create index bm_material_trace_history_source_product_plan_id_index
    on bm_material_trace_history (source_product_plan_id);

create index bm_material_trace_history_storage_material_batch_id_index
    on bm_material_trace_history (storage_material_batch_id);

create index bm_material_trace_history_trace_type_index
    on bm_material_trace_history (trace_type);