alter table bp_equipment_info
    add `apply_station_id` bigint null comment '设备占用的设备工委id'  after product_name;
alter table bp_equipment_operate_log
    add `apply_station_id` bigint null comment '设备占用的设备工委名称'  after product_name;
ALTER TABLE bp_equipment_station
    ALTER COLUMN `enable` SET DEFAULT 0;