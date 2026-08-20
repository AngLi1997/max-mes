alter table bm_business_component_instance
              add record_item_id bigint null comment '记录项id' after component_id;
alter table bm_business_component_instance
              add record_version_id bigint null comment '记录项版本id' after record_item_id;
alter table bm_business_component_instance
            add process_id bigint null comment '工艺id' after procedure_step_id;
alter table bm_business_component_instance
            add process_version bigint null comment '工艺版本' after process_id;
alter table bm_weigh_input_record
    add formula_material_id bigint null comment '配方物料id' after material_id;
alter table bm_business_component_instance
    add procedure_step_config_id bigint null comment '组件配置id' after id;