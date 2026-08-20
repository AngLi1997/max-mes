/*
 * 描述: 重命名 bm_execute_form_data 表中的检验项目/分析项相关字段，去掉 inspect 前缀
 * 作者: yigaohui
 * 日期: 2025-11-20
 * 环境: dev/test/prod
 * 变更内容:
 * 1. inspect_item_id -> item_id
 * 2. inspect_item_config_id -> item_config_id
 * 3. inspect_parameter_id -> parameter_id
 * 4. inspect_parameter_config_id -> parameter_config_id
 */

ALTER TABLE `bm_execute_form_data`
    RENAME COLUMN `inspect_item_id` TO `item_id`;

ALTER TABLE `bm_execute_form_data`
    RENAME COLUMN `inspect_item_config_id` TO `item_config_id`;

ALTER TABLE `bm_execute_form_data`
    RENAME COLUMN `inspect_parameter_id` TO `parameter_id`;

ALTER TABLE `bm_execute_form_data`
    RENAME COLUMN `inspect_parameter_config_id` TO `parameter_config_id`;


alter table bm_execute_form_data alter column rev set default 0;

alter table lm_eln_attachment
    add task_id bigint null comment '任务id' after path;

alter table lm_eln_attachment
    add file_name varchar(255) null;

