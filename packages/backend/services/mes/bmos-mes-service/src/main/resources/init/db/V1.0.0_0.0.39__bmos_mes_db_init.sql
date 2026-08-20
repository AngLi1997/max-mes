use bmos_mes;
set names utf8mb4;

alter table bm_log_operation
    modify operation_type varchar(32) not null comment '操作类型';