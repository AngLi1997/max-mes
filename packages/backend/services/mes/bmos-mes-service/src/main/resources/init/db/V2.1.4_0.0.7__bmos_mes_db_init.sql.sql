alter table bm_dataset_point_template_relation
    modify placeholder varchar(1024) null comment '占位符';

alter table bm_dataset_point_template_relation
    modify dataset_point_keys varchar(1024) null comment '数据点索引json';

alter table bm_dataset_point_template_relation
    modify dataset_keys varchar(1024) null comment '数据集索引json';