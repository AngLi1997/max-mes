-- ***************************** 房间清场 *****************************
rename table bp_equipment_module to bp_factory_module;
alter table bp_factory_module
drop column sort;
alter table bp_factory_module
    modify type tinyint not null comment '模型类型，用整数表示不同的模型类别 2-产线类型分类 3-房间类型分类 4-工位类型分类';
update bp_factory_module set type = 4;
alter table bp_factory_module
drop column tree_code;
alter table bp_equipment_station
drop column module_name;

-- 表结构新增
create table bp_factory_line
(
    id           bigint  default 0 not null comment '主键id，唯一标识'
        primary key,
    code         varchar(128)      not null comment '产线编码',
    name         varchar(128)      not null comment '产线名称',
    enable       tinyint           not null comment '启停',
    description  varchar(255)      null comment '产线描述',
    module_id    bigint            not null comment '所属模型id',
    use_count    int default 0 not null comment '已与多少个组件进行了绑定',
    operate_id   varchar(64)       null comment '最后操作人id(更新基础数据的用户)',
    operator     varchar(64)       null comment '操作人姓名(login_name-user_name)',
    operate_time datetime          null comment '操作时间',
    create_by    varchar(64)       null comment '创建人',
    update_by    varchar(64)       null comment '修改人',
    create_time  datetime          not null comment '创建时间',
    update_time  datetime          not null comment '修改时间',
    is_deleted   tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '产线' charset = utf8mb4;

create table bp_factory_line_room
(
    id          bigint            not null comment '主键id，工位与设备绑定关系的唯一标识'
        primary key,
    line_id     bigint            not null comment '产线id，关联到bp_factory_line表中的id',
    room_id     bigint            not null comment '房间id，关联到bp_factory_room表中的id',
    create_by   varchar(64)       null comment '创建人',
    update_by   varchar(64)       null comment '修改人',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    is_deleted  tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '产线与房间的绑定关系' charset = utf8mb4;

create table bp_factory_line_station
(
    id          bigint            not null comment '主键id，工位与设备绑定关系的唯一标识'
        primary key,
    line_id     bigint            not null comment '产线id，关联到bp_factory_line表中的id',
    station_id  bigint            not null comment '工位id，关联到bp_factory_room表中的id',
    create_by   varchar(64)       null comment '创建人',
    update_by   varchar(64)       null comment '修改人',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    is_deleted  tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '产线与工位的直接绑定的关系' charset = utf8mb4;


create table bp_factory_room
(
    id           bigint  default 0 not null comment '主键id，模型的唯一标识'
        primary key,
    code         varchar(128)      not null comment '房间编码',
    name         varchar(128)      not null comment '房间名称',
    status       int               not null comment '房间状态 房间状态 1-暂用 2-待清场 3-已清场',
    enable       tinyint           not null,
    time_limit   bigint            not null comment '清洁时限(单位秒)',
    expire_time  datetime          null comment '房间清洁效期',
    description  varchar(255)      null comment '房间描述',
    use_count    int     default 0 not null comment '当前房间绑定了多少个业务配置',
    operator     varchar(64)       null comment '操作人姓名(login_name-user_name)',
    operate_time datetime          null comment '操作时间',
    operate_id   varchar(64)       null comment '最后操作人id(更新基础数据的用户)',
    module_id    bigint            not null comment '所属房间模型id',
    create_by    varchar(64)       null comment '创建人',
    update_by    varchar(64)       null comment '修改人',
    create_time  datetime          not null comment '创建时间',
    update_time  datetime          not null comment '修改时间',
    is_deleted   tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '房间' charset = utf8mb4;

create table bp_factory_room_clean_log
(
    id             bigint  default 0 not null comment '主键id，模型的唯一标识'
        primary key,
    room_id        bigint            null comment '房间id',
    room_code      varchar(128)      not null comment '房间编码',
    room_name      varchar(128)      not null comment '房间名称',
    type           varchar(24)       not null comment '清场类型 1-生产清场 2-人工清场',
    batch_no       varchar(128)      not null comment '生产批号',
    product_name   varchar(64)       null comment '产品名称',
    procedure_id   bigint            null comment '生产工序id',
    procedure_name varchar(128)      null comment '生产工序名称',
    begin_time     datetime          null comment '开始时间',
    end_time       datetime          null comment '结束时间',
    expire_time    datetime          null comment '清场有效期',
    operator_id    varchar(64)       null comment '清场操作人id',
    operator       varchar(64)       null comment '清场操作人姓名（账号名称-账号姓名）',
    verify_id      varchar(64)       null comment '复核人id',
    verifier       varchar(64)       null comment '复核人姓名',
    verify_time    datetime          null comment '复核时间',
    description    varchar(255)      null comment '当为人工清场时的清场描述',
    create_by      varchar(64)       null comment '创建人',
    update_by      varchar(64)       null comment '修改人',
    create_time    datetime          not null comment '创建时间',
    update_time    datetime          not null comment '修改时间',
    is_deleted     tinyint default 0 not null comment '是否删除，0未删除，非0已删除',
    column_name    int               null
)
    comment '房间清场日志' charset = utf8mb4;

create table bp_factory_room_status_log
(
    id             bigint  default 0 not null comment '主键id，模型的唯一标识'
        primary key,
    room_id        bigint            null comment '房间id',
    room_code      varchar(128)      not null comment '房间编码',
    room_name      varchar(128)      not null comment '房间名称',
    type           varchar(24)       not null comment '清场类型 1-业务流转 2-人工流转',
    pre_status     int               not null comment '变更前的状态',
    status         int               not null comment '变更状态',
    procedure_id   bigint            null comment '生产工序id',
    procedure_name varchar(128)      null comment '生产工序名称',
    batch_no       varchar(64)       null comment '批次号',
    product_id     bigint            null comment '产品id',
    product_name   varchar(64)       null comment '产品名称',
    operator_id    varchar(64)       null comment '操作人id',
    operator       varchar(64)       null comment '操作人姓名（账号名称-账号姓名）',
    verify_id      varchar(64)       null comment '复核人id',
    verifier       varchar(64)       null comment '复核人姓名',
    description    varchar(255)      null comment '当为人工流转时的描述',
    create_by      varchar(64)       null comment '创建人',
    update_by      varchar(64)       null comment '修改人',
    create_time    datetime          not null comment '创建时间',
    update_time    datetime          not null comment '修改时间',
    is_deleted     tinyint default 0 not null comment '是否删除，0未删除，非0已删除',
    column_name    int               null
)
    comment '房间状态变更日志' charset = utf8mb4;

create table bp_factory_room_station
(
    id          bigint            not null comment '主键id，工位与设备绑定关系的唯一标识'
        primary key,
    room_id     bigint            not null comment '房间id，关联到bp_factory_room表中的id',
    station_id  bigint            not null comment '工位id，关联到bp_equipment_station表中的id',
    create_by   varchar(64)       null comment '创建人',
    update_by   varchar(64)       null comment '修改人',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '修改时间',
    is_deleted  tinyint default 0 not null comment '是否删除，0未删除，非0已删除'
)
    comment '房间与工位的绑定关系' charset = utf8mb4;

create table bm_resource_permission
(
    resource_id bigint not null,
    dept_id     bigint not null,
    constraint uk_resource_dept
        unique (resource_id, dept_id) comment '资源-部门唯一索引'
)
    comment '数据权限表' row_format = DYNAMIC;











