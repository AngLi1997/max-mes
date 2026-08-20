create table product_schedule_procedure_config
(
    id bigint null comment '主键id',
    process_id bigint null comment '工艺id',
    procedure_id bigint null comment '工序id',
    seq int null comment '顺序号'
)
    comment '生产进度配置';

