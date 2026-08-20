use bmos_mes;
set names utf8mb4;

alter table bm_material_log
    add supplier varchar(255) null comment '供应商' after report_no;

alter table bm_material_log
    add producer varchar(255) null comment '生产商' after supplier;
