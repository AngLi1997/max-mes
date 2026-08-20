create table bp_equipment_acquisition
(
    id                     bigint     default 0 not null comment '主键id'
        primary key,
    equipment_id           bigint               not null comment '设备id',
    equipment_code         varchar(128)         not null comment '设备编码',
    acquisition_point_id   bigint               not null comment '采集点id',
    acquisition_point_code varchar(128)         not null comment '采集点编码',
    create_by              bigint               not null comment '创建人',
    update_by              bigint               null comment '修改人',
    create_time            datetime             not null comment '创建时间',
    update_time            datetime             null comment '修改时间',
    is_deleted             tinyint(1) default 0 not null comment '是否删除'
)
    comment '设备-点位关联信息';

create table bp_equipment_category
(
    id          bigint  default 0 not null comment '主键id，设备分类的唯一标识'
        primary key,
    code        varchar(128)      not null comment '设备类别编码',
    name        varchar(128)      not null comment '设备类别名称',
    tree_code   varchar(255)      null,
    parent_id   bigint  default 0 not null comment '当前模型上级，若没有上级则为0',
    create_by   varchar(64)       null comment '创建人',
    update_by   varchar(64)       null comment '修改人',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    is_deleted  tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '设备分类表，记录设备的分类信息' charset = utf8mb4;

create table bp_equipment_info
(
    id             bigint  default 0 not null comment '主键id，设备基础信息的唯一标识'
        primary key,
    code           varchar(128)      not null comment '设备编码',
    name           varchar(128)      not null comment '设备名称',
    specifications varchar(128)      null comment '规格型号',
    position       varchar(255)      not null comment '设备地点',
    manufacturer   varchar(255)      null comment '设备厂商',
    purchase_date  date              null comment '购置日期',
    batch_no       varchar(64)       null comment '当前设备被占用时的生产批号',
    operate_log_id bigint            null comment '当前占用时绑定的某一个操作日志id',
    product_name   varchar(64)       null comment '产品名称',
    status         tinyint           null comment '设备状态(1-可用 2-不可用 3-故障 4-占用)',
    expire_date    date              null comment '设备状态有效期（设备小的所有设备状态的最小有效期）',
    category_id    bigint            null comment '设备类别id，关联到bp_equipment_category表中的id',
    enable         tinyint default 0 not null comment '启停状态',
    create_by      varchar(64)       null comment '创建人',
    update_by      varchar(64)       null comment '修改人',
    create_time    datetime          not null comment '创建时间',
    update_time    datetime          null comment '修改时间',
    is_deleted     tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '设备基础信息表，记录设备的基础信息' charset = utf8mb4;

create table bp_equipment_module
(
    id          bigint  default 0 not null comment '主键id，模型的唯一标识'
        primary key,
    code        varchar(128)      not null comment '模型编码，用于唯一、简洁地标识模型',
    name        varchar(128)      not null comment '模型名称，对模型的描述性文字',
    type        tinyint           not null comment '模型类型，用整数表示不同的模型类别',
    parent_id   bigint  default 0 not null comment '当前模型上级ID，若无上级则为0',
    tree_code   varchar(255)      not null comment '层级标识',
    sort        int               null comment '排序号',
    create_by   varchar(64)       null comment '创建人',
    update_by   varchar(64)       null comment '修改人',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    is_deleted  tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '设备工厂模型表，记录设备工厂相关的模块信息' charset = utf8mb4;

create table bp_equipment_operate_log
(
    id                  bigint            not null comment '主键id，设备操作日志记录的唯一标识'
        primary key,
    equipment_id        bigint            null comment '设备id，关联到bp_equipment_info表中的id',
    equipment_name      varchar(64)       null comment '设备名称',
    equipment_code      varchar(128)      null comment '设备编码',
    change_type         varchar(12)       null comment '变更类型 0- 手动变更 1-业务占用 ',
    position            varchar(255)      not null comment '设备地点',
    batch_no            varchar(128)      not null comment '生产批号',
    product_name        varchar(255)      not null comment '产品名称',
    begin_time          datetime          not null comment '使用开始时间',
    end_time            datetime          null comment '使用结束时间',
    begin_operator      varchar(64)       not null comment '开始操作人id',
    begin_operator_name varchar(64)       null comment '开始操作人姓名',
    end_operator        varchar(64)       null comment '结束操作人id',
    end_operator_name   varchar(64)       null comment '结束操作人姓名',
    create_by           varchar(64)       null comment '创建人',
    update_by           varchar(64)       null comment '修改人',
    create_time         datetime          not null comment '创建时间',
    update_time         datetime          not null comment '修改时间',
    is_deleted          tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '设备操作日志表，记录设备的操作日志信息' charset = utf8mb4;

create table bp_equipment_property_info
(
    id            bigint            not null comment '主键id，设备属性记录的唯一标识'
        primary key,
    equipment_id  bigint            not null comment '设备id，关联到bp_equipment_info表中的id',
    property_type tinyint           not null comment '属性类型，1-设备状态，2-设备属性',
    property_code varchar(64)       not null comment '属性code，用于唯一标识设备属性',
    name          varchar(128)      not null comment '属性名称',
    value         varchar(64)       null comment '当前设备属性的默认值',
    actual_value  varchar(64)       null comment '当前设备属性的实际值',
    finish_status tinyint           null comment '当前完成状态',
    embed         tinyint           not null comment '是否内置',
    required      tinyint           null comment '是否必填',
    create_by     varchar(64)       null comment '创建人',
    update_by     varchar(64)       null comment '修改人',
    create_time   datetime          not null comment '创建时间',
    update_time   datetime          not null comment '修改时间',
    is_deleted    tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '设备与属性表，记录设备的属性信息' charset = utf8mb4;

create table bp_equipment_station
(
    id          bigint  default 0 not null comment '主键id，工位的唯一标识'
        primary key,
    code        varchar(128)      not null comment '工位code，用于唯一标识工位',
    name        varchar(128)      not null comment '工位名称，对工位的描述性文字',
    description varchar(255)      null comment '工位描述，提供额外的工位信息',
    enable      tinyint default 1 not null comment '启停状态，表示工位是否启用',
    module_id   bigint            not null comment '模型id，关联到bp_equipment_module表中的id',
    module_name varchar(128)      not null comment '模型名称，若模型名称可被更改，则删除此字段',
    use_count   int     default 0 not null comment '使用次数',
    create_by   varchar(64)       null comment '创建人',
    update_by   varchar(64)       null comment '修改人',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    is_deleted  tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '设备工位表，记录设备工位的相关信息' charset = utf8mb4;

create table bp_equipment_station_info
(
    id           bigint            not null comment '主键id，工位与设备绑定关系的唯一标识'
        primary key,
    station_id   bigint            not null comment '工位id，关联到bp_equipment_station表中的id',
    equipment_id bigint            not null comment '设备id，关联到bp_equipment_info表中的id',
    create_by    varchar(64)       null comment '创建人',
    update_by    varchar(64)       null comment '修改人',
    create_time  datetime          not null comment '创建时间',
    update_time  datetime          not null comment '修改时间',
    is_deleted   tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '工位与设备绑定关系表，记录工位与设备之间的绑定关系' charset = utf8mb4;

create table bp_equipment_station_user
(
    id          bigint  default 0           not null comment '主键id，工位人员绑定关系的唯一标识'
        primary key,
    station_id  bigint                      not null comment '工位id，关联到bp_equipment_station表中的id',
    user_id     varchar(64)                 not null comment '用户id，标识与工位绑定的用户',
    create_by   varchar(64) charset utf8mb4 null comment '创建人',
    update_by   varchar(64) charset utf8mb4 null comment '修改人',
    create_time datetime                    not null comment '创建时间',
    update_time datetime                    not null comment '修改时间',
    is_deleted  tinyint default 0           not null comment '是否删除，0未删除，非0已删除'
)
    comment '工位人员绑定关系表，记录工位与用户之间的绑定关系';

create table bp_equipment_status_log
(
    id              bigint            not null comment '主键id，设备状态变更日志记录的唯一标识'
        primary key,
    equipment_id    bigint            null comment '设备id，关联到bp_equipment_info表中的id',
    equipment_code  varchar(64)       null comment '设备code',
    equipment_name  varchar(128)      null comment '设备名称',
    position        varchar(128)      null comment '设备地址',
    change_type     varchar(64)       null comment '变更类型',
    operate_name    varchar(64)       null comment '操作名称',
    pre_status_name varchar(64)       null comment '变更前状态名称',
    status_name     varchar(64)       null comment '变更后状态名称',
    expire_date     date              null comment '效期',
    operate_time    datetime          null comment '操作时间',
    operator        varchar(64)       null comment '操作人',
    operator_name   varchar(64)       null comment '操作人姓名',
    create_by       varchar(64)       null comment '创建人',
    update_by       varchar(64)       null comment '修改人',
    create_time     datetime          not null comment '创建时间',
    update_time     datetime          not null comment '修改时间',
    is_deleted      tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '设备状态变更日志表，记录设备状态的变更信息' charset = utf8mb4;

create table bp_equipment_tag
(
    id          bigint            not null comment '主键id，设备tag的唯一标识'
        primary key,
    code        varchar(64)       null comment '标签code',
    name        varchar(128)      not null comment 'tag名称',
    create_by   varchar(64)       null comment '创建人',
    update_by   varchar(64)       null comment '修改人',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    is_deleted  tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '设备tag表，记录设备的标签信息' charset = utf8mb4;

create table bp_equipment_tag_info
(
    id           bigint            not null comment '主键id，设备与标签关系的唯一标识'
        primary key,
    equipment_id bigint            not null comment '设备id，关联到bp_equipment_info表中的id',
    tag_id       bigint            not null comment '标签id，关联到bp_equipment_tag表中的id',
    create_by    varchar(64)       null comment '创建人',
    update_by    varchar(64)       null comment '修改人',
    create_time  datetime          not null comment '创建时间',
    update_time  datetime          not null comment '修改时间',
    is_deleted   tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '设备与标签表，记录设备与标签之间的关系' charset = utf8mb4;

create table bp_equipment_tag_property
(
    id            bigint auto_increment comment '主键id'
        primary key,
    code          varchar(64)       not null comment '属性code',
    name          varchar(64)       not null comment '属性名称',
    embed         tinyint           null comment '是否内置',
    property_type tinyint           not null comment '属性类型（1-设备状态，2-设备属性）',
    required      tinyint           null comment '是否必填',
    tag_id        bigint            not null comment '标签id',
    create_by     varchar(64)       null comment '创建人',
    update_by     varchar(64)       null comment '修改人',
    create_time   datetime          null comment '创建时间',
    update_time   datetime          null comment '修改时间',
    is_deleted    tinyint default 0 not null comment '是否删除（0-未删除，1-已删除）'
)
    comment '设备标签下的属性';

-- 内置的设备标签
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (1, 'CIP_10001', 'CIP系统单元', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (2, 'SIP_12002', 'SIP系统单元', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (3, 'TEMPERATURE_CONTROL_12003', '温控系统', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (4, 'AIR_CONDITIONING_12004', '空调系统', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (5, 'CLEANING_DEVICE_12005', '清洗设备', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (6, 'MICROBIOLOGICAL_DEVICE_12006', '灭菌设备', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (7, 'CHECK_DEVICE_12007', '检测设备', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (8, 'BROKEN_BAG_DEVICE_12008', '破袋设备', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (9, 'BLOOD_PRODUCTS_PRODUCTION_DEVICE_12009', '血液制品生产设备', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (10, 'CENTRIFUGE_12010', '离心机', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (11, 'MICROBIOLOGICAL_DEVICE_12011', '灭活设备', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (12, 'LAYER_EXTRACTION_SYSTEM_12012', '层析系统', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (13, 'OVERFILTER_SYSTEM_12013', '超滤系统', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (14, 'INJECTION_DEVICE_12014', '灌装设备', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (15, 'FREEZE_DRY_DEVICE_12015', '冻干设备', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (16, 'ROLL_COVER_DEVICE_12016', '轧盖设备', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (17, 'DRY_MICROBIOLOGICAL_DEVICE_12017', '干热灭活设备', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (18, 'AUTO_LIGHT_CHECK_DEVICE_12018', '全自动灯检设备', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (19, 'PRODUCT_PACKAGING_DEVICE_12019', '制品包装设备', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (20, 'WEIGHING_DEVICE_12020', '称具', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (21, 'CONTAINER_12021', '容器', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (22, 'PRINTER_12022', '打印机', '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag (id, code, name, create_by, update_by, create_time, update_time, is_deleted) VALUES (23, 'PAD_12023', 'PAD', '1', '1', now(), now(), 0);


-- 设备标签与属性之间的内置数据
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (1, 'DISINFECT_002', '消毒状态', 1, 1, 1, 5, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (2, 'DISINFECT_002', '消毒状态', 1, 1, 1, 6, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (3, 'DISINFECT_002', '消毒状态', 1, 1, 1, 7, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (4, 'DISINFECT_002', '消毒状态', 1, 1, 1, 8, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (5, 'DISINFECT_002', '消毒状态', 1, 1, 1, 9, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (6, 'DISINFECT_002', '消毒状态', 1, 1, 1, 10, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (7, 'DISINFECT_002', '消毒状态', 1, 1, 1, 11, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (8, 'DISINFECT_002', '消毒状态', 1, 1, 1, 12, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (9, 'DISINFECT_002', '消毒状态', 1, 1, 1, 13, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (10, 'DISINFECT_002', '消毒状态', 1, 1, 1, 14, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (11, 'DISINFECT_002', '消毒状态', 1, 1, 1, 15, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (12, 'DISINFECT_002', '消毒状态', 1, 1, 1, 17, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (13, 'DISINFECT_002', '消毒状态', 1, 1, 1, 18, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (14, 'CLEAN_001', '清洁状态', 1, 1, 1, 5, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (15, 'CLEAN_001', '清洁状态', 1, 1, 1, 6, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (16, 'CLEAN_001', '清洁状态', 1, 1, 1, 7, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (17, 'CLEAN_001', '清洁状态', 1, 1, 1, 8, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (18, 'CLEAN_001', '清洁状态', 1, 1, 1, 9, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (19, 'CLEAN_001', '清洁状态', 1, 1, 1, 10, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (20, 'CLEAN_001', '清洁状态', 1, 1, 1, 11, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (21, 'CLEAN_001', '清洁状态', 1, 1, 1, 12, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (22, 'CLEAN_001', '清洁状态', 1, 1, 1, 13, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (23, 'CLEAN_001', '清洁状态', 1, 1, 1, 14, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (24, 'CLEAN_001', '清洁状态', 1, 1, 1, 15, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (25, 'CLEAN_001', '清洁状态', 1, 1, 1, 16, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (26, 'CLEAN_001', '清洁状态', 1, 1, 1, 17, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (27, 'CLEAN_001', '清洁状态', 1, 1, 1, 18, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (28, 'CLEAN_001', '清洁状态', 1, 1, 1, 19, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (29, 'CLEAN_001', '清洁状态', 1, 1, 1, 20, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (30, 'CLEAN_001', '清洁状态', 1, 1, 1, 21, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (31, 'CALIBRATION_003', '校准状态', 1, 1, 1, 20, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (32, 'WEIGHING_UNIT_001', '称量单位', 1, 2, 1, 20, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (33, 'WEIGHING_ACCURACY_002', '称量精度', 1, 2, 1, 20, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (34, 'WEIGHING_RANGE_003', '称量范围', 1, 2, 1, 20, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (35, 'CONTAINER_WEIGHT_004', '容器皮重', 1, 2, 1, 21, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (36, 'CONTENT_VOLUME_005', '内容物体积', 1, 2, 0, 21, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (37, 'CONTENT_WEIGHT_006', '内容物重量', 1, 2, 0, 21, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (38, 'IP_ADDRESS_007', 'IP地址', 1, 2, 1, 22, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (39, 'PORT_008', '端口', 1, 2, 1, 22, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (40, 'ASSET_CODE_009', '资产编码', 1, 2, 1, 23, '1', '1', now(), now(), 0);
INSERT INTO bmos_platform.bp_equipment_tag_property (id, code, name, embed, property_type, required, tag_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (41, 'PAD_ADDRESS_010', 'PAD地址', 1, 2, 1, 23, '1', '1', now(), now(), 0);