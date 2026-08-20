use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
alter table bm_storage_material
    add sign_status int null comment '签名状态' after container;
alter table bm_storage_material
    add source varchar(100) null comment '物料来源' after sign_status;
alter table bm_ingredient_weigh_record
    add weigh_time datetime null comment '称量时间' after remark;
SET FOREIGN_KEY_CHECKS = 1;
