-- 2.1.1 老君山版本平台新的sql写在这里
alter table bp_operation_log add `login_name` varchar(255) COLLATE utf8mb4_general_ci default NULL COMMENT '登录账号';
