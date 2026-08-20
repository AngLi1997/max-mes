use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

alter table bm_storage_material_batch
    add produce_date date null comment '生产日期' after receiver_id;

alter table bm_storage_material_batch
    add report_no varchar(100) null comment '报告单编号' after produce_date;

alter table bm_storage_material_batch
    add licence_no varchar(100) null comment '放行单编号' after report_no;

SET FOREIGN_KEY_CHECKS = 1;