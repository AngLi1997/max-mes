use bmos_mes;
set names utf8mb4;
set foreign_key_checks = 0;
alter table bm_ingredient_weigh_record
    add container_name varchar(100) null comment '容器名称' after container_id;
set foreign_key_checks = 1;