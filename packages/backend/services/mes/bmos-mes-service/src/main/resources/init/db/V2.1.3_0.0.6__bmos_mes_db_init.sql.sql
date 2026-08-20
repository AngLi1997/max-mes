alter table bm_batch_record_archive
    modify operator_id varchar(64) null comment '归档操作人ueseId';

alter table bm_batch_record_archive
    modify operator_name varchar(64) null comment '归档操作人用户名称';

alter table bm_batch_record_archive
    modify operator_login_name varchar(64) null comment '归档操作人登录名称';

alter table bm_batch_record_archive_log
    modify operator_id varchar(64) null comment '操作人ueseId';

alter table bm_batch_record_archive_log
    modify operator_login_name varchar(64) null comment '操作人登录名称';

alter table bm_batch_record_archive_log
    modify operator_name varchar(64) null comment '操作人用户名称';

