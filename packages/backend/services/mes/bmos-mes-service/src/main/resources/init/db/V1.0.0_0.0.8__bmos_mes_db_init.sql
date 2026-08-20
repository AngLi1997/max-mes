use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
alter table bm_storage_material_batch
    modify original_batch_no varchar(100) null comment '原始编码';

alter table bm_storage_material_batch
    add factory_batch_no varchar(100) null comment '原厂批号' after expired_date;

alter table bm_storage_material_batch
    add hydration varchar(100) null comment '水分(%)' after factory_batch_no;

alter table bm_storage_material_batch
    add no_hydration_content varchar(100) null comment '无水含量(%)' after hydration;
SET FOREIGN_KEY_CHECKS = 1;
