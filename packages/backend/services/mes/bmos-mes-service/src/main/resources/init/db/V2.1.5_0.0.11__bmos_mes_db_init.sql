create table bm_process_dashboard_config_data
(
    id                  bigint            not null comment '物理主键'
        primary key,
    dashboard_config_id bigint            not null comment '工艺看板配置id bm_process_dashboard_config表的主键id',
    procedure_id        bigint            null comment '工序id',
    procedure_name      varchar(255)      null comment '工序名称',
    custom_name         varchar(255)      null comment '自定义名称',
    effect              tinyint           null comment '是否生效',
    model_code          varchar(64)       null comment '工序编码(大屏对应的模型编码)',
    sort                int               not null comment '大屏看板中对应的工序排序',
    create_time         datetime          null,
    update_time         datetime          null,
    create_by           varchar(100)      null,
    update_by           varchar(100)      null,
    is_deleted          tinyint default 0 null comment '是否删除'
)
    comment '工艺看板配置的具体配置信息';