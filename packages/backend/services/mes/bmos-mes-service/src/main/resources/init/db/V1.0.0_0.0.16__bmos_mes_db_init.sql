use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

alter table bm_ingredient_input_record
    add device_name varchar(128) null comment '设备名称' after device_id;

alter table bm_ingredient_input_record
    add device_code varchar(64) null comment '设备编码' after device_name;

SET FOREIGN_KEY_CHECKS = 1;
