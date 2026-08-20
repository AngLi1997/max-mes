use bmos_mes;
set names utf8mb4;

alter table bm_ingredient_input_record
    add component_instance_id bigint null comment '称量组件实例id' after input_time;