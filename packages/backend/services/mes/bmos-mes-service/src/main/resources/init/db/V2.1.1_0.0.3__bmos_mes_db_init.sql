update bm_batch_record_item set docx_header  = null where docx_header = 'null';
update bm_batch_record_item set docx_footer  = null where docx_footer = 'null';
alter table bm_process_version add `effect_date` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生效时间';
alter table bm_process_version add `history_state` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '历史版本状态';
alter table bm_operation_log add `login_name` varchar(255) COLLATE utf8mb4_general_ci default NULL COMMENT '登录账号';
