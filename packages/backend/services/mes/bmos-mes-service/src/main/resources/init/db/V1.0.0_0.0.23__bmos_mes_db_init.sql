use bmos_mes;
set names utf8mb4;
set foreign_key_checks = 0;
alter table bm_ingredient_weigh_process
    add pre_weigher_id varchar(100) null comment '称量人id' after component_id;

alter table bm_ingredient_weigh_process
    add pre_re_checker_id varchar(100) null comment '复核人id' after pre_weigher_id;

alter table bm_ingredient_weigh_process
    add remark varchar(200) null comment '备注' after pre_re_checker_id;
set foreign_key_checks = 1;