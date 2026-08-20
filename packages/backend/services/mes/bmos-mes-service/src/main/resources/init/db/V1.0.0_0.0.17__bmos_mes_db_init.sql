use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
alter table bm_ingredient_input_record
    modify storage_material_id bigint null comment '物料件id';

alter table bm_ingredient_input_record
    add storage_material_no varchar(100) null comment '物料件编号' after storage_material_id;
SET FOREIGN_KEY_CHECKS = 1;
