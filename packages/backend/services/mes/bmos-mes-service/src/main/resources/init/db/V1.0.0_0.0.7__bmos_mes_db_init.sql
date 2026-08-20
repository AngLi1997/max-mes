use bmos_mes;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- 修改批记录文档配置长度为255
alter table bm_batch_record_item
    modify page_config varchar(255) default '{"pattern":1}' null comment '文档配置';
SET FOREIGN_KEY_CHECKS = 1;
