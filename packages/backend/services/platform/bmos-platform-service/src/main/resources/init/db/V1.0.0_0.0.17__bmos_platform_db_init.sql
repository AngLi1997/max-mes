-- 称量任务编号
INSERT INTO bp_code_rule (id, code, name, create_time, update_time, create_by, update_by)
VALUES (1810564924288667648, 'weigh.task.serial', '称量任务编号', '2024-07-09 14:41:31', '2024-07-09 14:41:31', 1, 1);
INSERT INTO bp_code_rule_version (id, rule_code, version, description, reset_rule, create_time, update_time, create_by, update_by)
VALUES (1810564924359970816, 'weigh.task.serial', '1', '', '[1]', '2024-07-09 14:41:31', '2024-07-09 14:41:31', 1, 1);
INSERT INTO bp_code_rule_version_detail (id, code_rule_version_id, type, date_type, date_format, sort, create_time, update_time, create_by, update_by)
VALUES (1810564924401913856, 1810564924359970816, 'DATE', '4', 'yyyyMMdd', 1, '2024-07-09 14:41:31', '2024-07-09 14:41:31', 1, 1);
INSERT INTO bp_code_rule_version_detail (id, code_rule_version_id, type, start_no, max_length, step, fill_zero, sort, create_time, update_time, create_by, update_by)
VALUES (1810564924410302464, 1810564924359970816, 'SEQUENCE', 1, 3, 1, TRUE, 2, '2024-07-09 14:41:31', '2024-07-09 14:41:31', 1, 1);
-- 确认
UPDATE bp_code_rule_version SET version_status = 'CONFIRM' ,update_time = now() WHERE id = 1810564924359970816 AND is_deleted = false;
-- 启用
UPDATE bp_code_rule_version SET status = true, update_time = now() WHERE id = 1810564924359970816 AND is_deleted = false;

create table bp_user_sign
(
    id            bigint                               not null comment '主键'
        primary key,
    user_id       varchar(64)                          null comment '用户id',
    sign_url      varchar(64)                          null comment '签名url',
    sign_time     datetime                               null comment '签名时间',
    terminal_type varchar(255)                         null comment '终端类型 0-pc 1-pad',
    create_time   datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '创建时间',
    update_time   datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by     varchar(32)                          null comment '创建人',
    update_by     varchar(32)                          null comment '更新人',
    is_deleted    tinyint(1) default 0                 null comment '是否删除',
    constraint idx_user_id
        unique (user_id)
)
    comment '手写签名表' row_format = DYNAMIC;