alter table bm_storage_material_batch
    add expire_warning_flag tinyint(1) default 0 null comment '物料临期提醒标志' after expired_date ;
