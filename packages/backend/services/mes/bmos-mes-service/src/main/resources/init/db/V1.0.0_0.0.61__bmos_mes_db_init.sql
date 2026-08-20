alter table inf_ru_execution
    add active_state tinyint(1) DEFAULT NULL COMMENT '是否激活，激活状态==true;未激活：false';
alter table inf_ru_execution
    add active_time datetime DEFAULT NULL COMMENT '激活时间';
alter table inf_ru_execution
    add active_user varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '激活用户';