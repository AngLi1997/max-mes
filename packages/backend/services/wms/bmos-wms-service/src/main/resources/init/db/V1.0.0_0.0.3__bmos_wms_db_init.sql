use bmos_wms;
set foreign_key_checks = 0;
alter table bw_cargo
    modify single_quantity varchar(255) null comment '单件量';
alter table bw_inventory
    modify init_quantity varchar(255) null comment '初始量';

alter table bw_inventory
    modify available_quantity varchar(255) null comment '可用量';

alter table bw_inventory
    modify consume_quantity varchar(255) null comment '消耗量';

alter table bw_inventory
    modify reserve_quantity varchar(255) null comment '预订量';

alter table bw_inventory_batch
    modify hydration varchar(255) null comment '水分(%)';

alter table bw_inventory_batch
    modify no_hydration_content varchar(255) null comment '无水含量(%)';
alter table bw_inventory_reserve
    modify reserve_quantity varchar(255) null comment '预定数量';

alter table bw_position_log
    modify quantity varchar(255) null comment '货品量';

alter table bw_send_out_order_item
    modify reserve_quantity varchar(255) null comment '预订量';

set foreign_key_checks = 1;