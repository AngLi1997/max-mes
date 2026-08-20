-- 增加账户锁定后的解锁时间字段以及账户锁定之前的账户状态字段
alter table bp_user
    add unlock_time datetime null comment '账户锁定后的解锁时间' after valid_time;
alter table bp_user
    add lock_preview_status int null comment '账户锁定之前的账户状态' after active_status;

alter table bp_equipment_status_log
    modify change_type varchar(64) null comment '变更类型 MANUAL-手动变更 BUSINESS-业务 EXPIRE-效期到期';

alter table bp_equipment_status_log
    modify operate_name varchar(64) null comment '操作名称 OPERATE-使用 DISINFECTION-消毒 CLEAN-清洁 CALIBRATION-校准';

alter table bp_equipment_status_log
    modify pre_status_name varchar(64) null comment '变更前状态名称 ALREADY_CLEAN-已清洁 ALREADY_DISINFECT-已消毒 ALREADY_CALIBRATION-已校准 NOT_CLEAN-未清洁 NOT_DISINFECT-未消毒 NOT_CALIBRATION-未校准 AVAILABLE-可用 UNAVAILABLE-不可用 OCCUPY-占用 FAULT-故障';

alter table bp_equipment_status_log
    modify status_name varchar(64) null comment '变更后状态名称 ALREADY_CLEAN-已清洁 ALREADY_DISINFECT-已消毒 ALREADY_CALIBRATION-已校准 NOT_CLEAN-未清洁 NOT_DISINFECT-未消毒 NOT_CALIBRATION-未校准 AVAILABLE-可用 UNAVAILABLE-不可用 OCCUPY-占用 FAULT-故障';

alter table bp_factory_room_clean_log
    modify type varchar(24) not null comment '清场类型 MANUAL_INPUT-生产清场 AUTO_RECOGNITION-人工清场';

alter table bp_factory_room_status_log
    modify type varchar(24) not null comment '清场类型 MANUAL_INPUT-生产清场 AUTO_RECOGNITION-人工清场';


alter table bp_login_log
    add description_param varchar(1024) null comment '描述参数' after description_code;
update bp_login_log set description_param = '8104026' where description_code = '8104007';


update bp_login_log set operation_action = 0 where operation_action = '登出';
update bp_login_log set operation_action = 1 where operation_action = '登录';
alter table bp_login_log
    modify operation_action int not null comment '操作动作';


update bmos_platform.bp_equipment_status_log set change_type = 'MANUAL' where change_type = '手动变更';
update bmos_platform.bp_equipment_status_log set change_type = 'BUSINESS' where change_type = '业务流转';
update bmos_platform.bp_equipment_status_log set change_type = 'EXPIRE' where change_type = '效期到期';



update bmos_platform.bp_equipment_status_log set operate_name = 'OPERATE' where operate_name = '使用';
update bmos_platform.bp_equipment_status_log set operate_name = 'DISINFECTION' where operate_name = '消毒';
update bmos_platform.bp_equipment_status_log set operate_name = 'CLEAN' where operate_name = '清洁';
update bmos_platform.bp_equipment_status_log set operate_name = 'CALIBRATION' where operate_name = '校准';



update bmos_platform.bp_equipment_status_log set pre_status_name = 'ALREADY_CLEAN' where pre_status_name = '已清洁';
update bmos_platform.bp_equipment_status_log set pre_status_name = 'ALREADY_DISINFECT' where pre_status_name = '已消毒';
update bmos_platform.bp_equipment_status_log set pre_status_name = 'ALREADY_CALIBRATION' where pre_status_name = '已校准';
update bmos_platform.bp_equipment_status_log set pre_status_name = 'NOT_CLEAN' where pre_status_name = '未清洁';
update bmos_platform.bp_equipment_status_log set pre_status_name = 'NOT_DISINFECT' where pre_status_name = '未消毒';
update bmos_platform.bp_equipment_status_log set pre_status_name = 'NOT_CALIBRATION' where pre_status_name = '未校准';
update bmos_platform.bp_equipment_status_log set pre_status_name = 'AVAILABLE' where pre_status_name = '可用';
update bmos_platform.bp_equipment_status_log set pre_status_name = 'UNAVAILABLE' where pre_status_name = '不可用';
update bmos_platform.bp_equipment_status_log set pre_status_name = 'OCCUPY' where pre_status_name = '占用';
update bmos_platform.bp_equipment_status_log set pre_status_name = 'FAULT' where pre_status_name = '故障';



update bmos_platform.bp_equipment_status_log set status_name = 'ALREADY_CLEAN' where status_name = '已清洁';
update bmos_platform.bp_equipment_status_log set status_name = 'ALREADY_DISINFECT' where status_name = '已消毒';
update bmos_platform.bp_equipment_status_log set status_name = 'ALREADY_CALIBRATION' where status_name = '已校准';
update bmos_platform.bp_equipment_status_log set status_name = 'NOT_CLEAN' where status_name = '未清洁';
update bmos_platform.bp_equipment_status_log set status_name = 'NOT_DISINFECT' where status_name = '未消毒';
update bmos_platform.bp_equipment_status_log set status_name = 'NOT_CALIBRATION' where status_name = '未校准';
update bmos_platform.bp_equipment_status_log set status_name = 'AVAILABLE' where status_name = '可用';
update bmos_platform.bp_equipment_status_log set status_name = 'UNAVAILABLE' where status_name = '不可用';
update bmos_platform.bp_equipment_status_log set status_name = 'OCCUPY' where status_name = '占用';
update bmos_platform.bp_equipment_status_log set status_name = 'FAULT' where status_name = '故障';



update bmos_platform.bp_factory_room_clean_log set type = 'MANUAL_INPUT' where type = '生产清场';
update bmos_platform.bp_factory_room_clean_log set type = 'AUTO_RECOGNITION' where type = '人工清场';





alter table bp_factory_room
    add floor_id bigint null comment '楼层id' after module_id;
alter table bp_factory_room
    add clean_level varchar(255) null comment '洁净等级' after floor_id;
alter table bp_factory_room
    add tenement_id bigint null comment '楼栋id' after module_id;
alter table bp_factory_room
    add three_d_model_id varchar(255) null comment '3D模型id' after floor_id;

ALTER TABLE bp_factory_line_room CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE bp_factory_line_station CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE bp_factory_room  CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE bp_factory_room_clean_log CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE bp_factory_room_status_log    CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE bp_factory_room_station CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE bp_equipment_property_info CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE bp_factory_line CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- auto-generated definition
create table bp_factory_tenement
(
    id          bigint            not null comment 'id'
        primary key,
    code        varchar(255)      not null comment '楼宇编码',
    name        varchar(500)      not null comment '楼宇名称',
    parent_id   bigint            null comment '父级id',
    create_by   varchar(255)      null comment '创建人',
    update_by   varchar(255)      null comment '更新人',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    comment '楼宇';


-- auto-generated definition
create table bp_factory_tenement_floor
(
    id          bigint               not null comment '主键id'
        primary key,
    tenement_id bigint               null comment '楼栋id',
    code        varchar(255)         null comment '编码',
    name        varchar(255)         null comment '楼层名称',
    status      varchar(255)         null comment '状态',
    create_by   varchar(255)         null comment '创建人',
    update_by   varchar(255)         null comment '修改人',
    create_time datetime             null comment '创建时间',
    update_time datetime             null comment '更新时间',
    is_deleted  tinyint(1) default 0 null comment '是否删除'
)
    comment '楼宇楼层表';


-- auto-generated definition
create table bp_factory_room_env_property
(
    id                           bigint            not null comment 'id'
        primary key,
    room_id                      bigint            not null comment '房间id',
    equipment_id                 bigint            not null,
    env_property_code            varchar(255)      null comment '环境参数编码',
    equipment_data_property_code varchar(255)      null comment '设备数据编码',
    create_time                  datetime          null comment '创建时间',
    update_time                  datetime          null comment '更新时间',
    create_by                    varchar(255)      null comment '创建人',
    update_by                    varchar(255)      null comment '更新人',
    is_deleted                   tinyint default 0 null comment '是否删除'
);

create index bp_factory_room_env_property_room_id_index
    on bp_factory_room_env_property (room_id);

