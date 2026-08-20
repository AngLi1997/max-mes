use bmos_mes;
set names utf8mb4;

alter table bm_batch_record_item
    add page_number_style int null comment '页码样式' after first_different;