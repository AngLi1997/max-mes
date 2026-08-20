use bmos_mes;
set names utf8mb4;
set foreign_key_checks = 0;

alter table bm_storage_material
    add container_id bigint null comment '容器id' after container;

set foreign_key_checks = 1;