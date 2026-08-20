-- 设备占用 日志字段更新

alter table bp_equipment_operate_log
    modify batch_no varchar(128) null comment '生产批号';

alter table bp_equipment_operate_log
    modify product_name varchar(255) null comment '产品名称';