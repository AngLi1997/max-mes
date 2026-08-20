use bmos_mes;
set names utf8mb4;

alter table bm_batch_record_item
    add docx_header longtext null comment '页眉' after record_version_id;

alter table bm_batch_record_item
    add docx_footer longtext null comment '页脚' after docx_header;

alter table bm_batch_record_item
    add first_different boolean null comment '首页不同' after docx_footer;

alter table bm_batch_record_item
    add odd_and_even_different boolean null comment '奇偶不同' after first_different;