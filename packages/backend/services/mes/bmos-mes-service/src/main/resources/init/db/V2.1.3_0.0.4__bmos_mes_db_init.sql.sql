-- 新增批记录path是否被删除的字段
alter table bm_batch_record_archive_generate
    add delete_file_flag tinyint null comment '生成的文件是否在minio中否被删除' after complete;

update bm_batch_record_archive_generate set delete_file_flag = 0 where 1=1;