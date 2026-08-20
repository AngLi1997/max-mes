use bmos_mes;
set names utf8mb4;

alter table bm_process_record_order
    add procedure_step_model_id bigint null comment '工序步骤模型id' after reusable;