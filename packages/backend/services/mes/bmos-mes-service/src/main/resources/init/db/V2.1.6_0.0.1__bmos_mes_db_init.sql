alter table bm_storage_material
    add product_id bigint null comment '产品id （来源是物料称量，和上面的productPlanId不对应是正常的！！！）' after product_plan_id;

alter table bm_storage_material
    add batch_no varchar(255) null comment '生产批号 （来源是物料称量，和上面的productPlanId不一样是正常的！！！）' after product_id;

create table bm_free_weigh_history
(
    id                  bigint       not null comment '物理主键'
        primary key,
    storage_material_id bigint       null comment '物料件id',
    tare_weight         varchar(255) null comment '皮重',
    gross_weight        varchar(255) null comment '毛重',
    net_weight          varchar(255) null comment '净重',
    unit_id             bigint       null comment '单位id',
    weigher_id          varchar(255) null comment '称量人id',
    re_checker_id       varchar(255) null comment '复核人id',
    container_id        bigint       null comment '容器id',
    container_name      varchar(255) null comment '容器名称',
    position_id         bigint       null comment '货位id',
    position_name       varchar(255) null comment '货位名称',
    weigh_time          datetime     null comment '称量时间',
    weigh_mode          int          null comment '称量模式 1 秤具 2 手动',
    device_id           bigint       null comment '秤具设备id',
    create_time         datetime     null,
    update_time         datetime     null,
    create_by           varchar(64)  null,
    update_by           varchar(64)  null,
    is_deleted          tinyint(1)        default 0 not null
) comment ='自由称量历史记录';


alter table bm_plan_template_batch modify product_name varchar(100) not null comment '产品名称';

alter table bm_product_plan modify product_name varchar(100) not null comment '产品名称';

alter table bm_reserve_component_material add column `quantity` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作值';
