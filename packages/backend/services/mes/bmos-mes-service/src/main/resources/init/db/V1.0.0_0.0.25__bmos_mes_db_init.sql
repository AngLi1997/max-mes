use bmos_mes;
set names utf8mb4;
set foreign_key_checks = 0;
alter table bm_ingredient_weigh_batch_process
    add weigh_process int null comment '称量阶段' after weigh_status;
set foreign_key_checks = 1;