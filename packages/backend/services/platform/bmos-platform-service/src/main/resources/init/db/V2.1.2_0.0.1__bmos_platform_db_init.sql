-- 太行山版本sql
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by,
                                                     update_by, create_time, update_time, is_deleted)
VALUES (42, 'PRINTER_DPI_011', '打印机dpi', 1, 2, 1, 22, '1', '1', '2024-08-12 17:43:48', '2024-08-12 17:43:50', 0);


alter table bp_equipment_tag
    add parent_id bigint not null comment '父级id' after id;

alter table bp_equipment_tag_property modify property_type tinyint not null comment '属性类型（1-设备状态，2-设备属性，3-设备数据）';

alter table bp_equipment_tag
    add embed tinyint(1) null comment '是否是内置' after name;

update bp_equipment_tag set embed=1 where embed is null ;

alter table bp_equipment_tag
    add description varchar(500) null comment '描述' after name;




create table bp_equipment_tag_use_template
(
    id bigint not null comment '主键id',
    tag_id bigint not null comment '类型id',
    operate_name varchar(255) not null comment '操作名称',
    template varchar(500) not null comment '模板内容',
    create_by     varchar(64)       null comment '创建人',
    update_by     varchar(64)       null comment '修改人',
    create_time   datetime          null comment '创建时间',
    update_time   datetime          null comment '修改时间',
    is_deleted    tinyint default 0 not null comment '是否删除（0-未删除，1-已删除）',
    constraint bp_equipment_tag_use_template_pk
        primary key (id)
)
    comment '设备类型使用日志模板表';


alter table bp_equipment_info
drop column specifications;

alter table bp_equipment_info
drop column position;

alter table bp_equipment_info
drop column manufacturer;

alter table bp_equipment_info
drop column purchase_date;


alter table bp_equipment_operate_log
drop column position;

alter table bp_equipment_info
    add description varchar(500) null comment '描述' after enable;

alter table bp_equipment_property_info modify value varchar(64) null comment '状态类的配置：这个值为有效时长，目前可以设置天、时、分、秒，以","隔开
信息类的配置：这个值为信息字段输入的值
数据类的配置：这个值对应采集点位id';

alter table bp_acquisition_point
    add equipment_data_code varchar(255) null comment '设备数据编码' after description;

alter table bp_equipment_info modify expire_date datetime null comment '设备状态有效期（设备小的所有设备状态的最小有效期）';
alter table bp_equipment_info change expire_date expire_date_time datetime null comment '设备状态有效期（设备小的所有设备状态的最小有效期）';
alter table bp_equipment_status_log change expire_date expire_date_time datetime null comment '效期';

alter table bp_acquisition_point change equipment_data_code equipment_tag_data_code varchar(255) null comment '设备数据编码';

alter table bp_equipment_operate_log
    add reviewer bigint null comment '复核人' after end_operator_name;

alter table bp_equipment_operate_log
    add reviewer_name varchar(255) null comment '复核人名称' after reviewer;


-- 数据集流水号
INSERT INTO bp_code_rule (id, code, name, create_time, update_time, create_by, update_by)
VALUES (1813393798311579649, 'dataset.key.serial', '数据集流水号', '2024-08-26 14:41:31', '2024-08-26 14:41:31', 1, 1);
INSERT INTO bp_code_rule_version (id, rule_code, version, description, reset_rule, create_time, update_time, create_by, update_by)
VALUES (1813393798378688513, 'dataset.key.serial', '1', '', '[1]', '2024-08-26 14:41:31', '2024-08-26 14:41:31', 1, 1);
INSERT INTO bp_code_rule_version_detail (id, code_rule_version_id, type, start_no, max_length, step, fill_zero, sort, create_time, update_time, create_by, update_by)
VALUES (1815216923386974211, 1813393798378688513, 'SEQUENCE', 1, 3, 1, TRUE, 2, '2024-08-26 14:41:31', '2024-08-26 14:41:31', 1, 1);
-- 确认
UPDATE bp_code_rule_version SET version_status = 'CONFIRM' ,update_time = now() WHERE id = 1813393798378688513 AND is_deleted = false;
-- 启用
UPDATE bp_code_rule_version SET status = true, update_time = now() WHERE id = 1813393798378688513 AND is_deleted = false;

-- 数据点流水号
INSERT INTO bp_code_rule (id, code, name, create_time, update_time, create_by, update_by)
VALUES (1813393798311579650, 'dataset.point.key.serial', '数据点流水号', '2024-08-26 14:41:31', '2024-08-26 14:41:31', 1, 1);
INSERT INTO bp_code_rule_version (id, rule_code, version, description, reset_rule, create_time, update_time, create_by, update_by)
VALUES (1813393798378688514, 'dataset.point.key.serial', '1', '', '[1]', '2024-08-26 14:41:31', '2024-08-26 14:41:31', 1, 1);
INSERT INTO bp_code_rule_version_detail (id, code_rule_version_id, type, start_no, max_length, step, fill_zero, sort, create_time, update_time, create_by, update_by)
VALUES (1815216923386974212, 1813393798378688514, 'SEQUENCE', 1, 3, 1, TRUE, 2, '2024-08-26 14:41:31', '2024-08-26 14:41:31', 1, 1);
-- 确认
UPDATE bp_code_rule_version SET version_status = 'CONFIRM' ,update_time = now() WHERE id = 1813393798378688514 AND is_deleted = false;
-- 启用
UPDATE bp_code_rule_version SET status = true, update_time = now() WHERE id = 1813393798378688514 AND is_deleted = false;

alter table bp_equipment_operate_log
    add operate_content varchar(1000) null comment '操作内容' after reviewer_name;

alter table bp_equipment_operate_log modify batch_no varchar(128) null comment '生产批号';
-- 流水号版本修复
update bp_code_rule_version_detail set fill_zero = 'TRUE' where fill_zero = '1';


