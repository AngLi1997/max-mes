alter table bm_batch_record_item
    add file_path varchar(1024) null comment '文件路径' after file_content;
