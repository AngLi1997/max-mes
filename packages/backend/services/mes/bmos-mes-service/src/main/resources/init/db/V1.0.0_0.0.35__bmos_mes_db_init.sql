use bmos_mes;
set names utf8mb4;

alter table bm_weigh_log
    modify net_weight varchar(64) null comment '净重';

alter table bm_weigh_log
    modify tare_weight varchar(64) null comment '皮重';

alter table bm_weigh_log
    modify gross_weight varchar(64) null comment '毛重';