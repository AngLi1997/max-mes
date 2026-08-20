use bmos_mes;
set names utf8mb4;
set foreign_key_checks = 0;
alter table bm_ingredient_weigh_record
    modify tare_weight varchar(255) null comment '皮重(基本单位量)';

alter table bm_ingredient_weigh_record
    modify gross_weight varchar(255) null comment '毛重(基本单位量)';

alter table bm_ingredient_weigh_record
    modify net_weight varchar(255) null comment '净重(基本单位量)';
set foreign_key_checks = 1;