use bmos_mes;
set names utf8mb4;

alter table bm_output_finished_product_result
modify single_quantity varchar(64) null comment '单件量';