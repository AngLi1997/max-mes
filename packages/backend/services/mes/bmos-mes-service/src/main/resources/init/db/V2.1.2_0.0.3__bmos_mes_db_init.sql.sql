-- 工作流表添加完成人字段
alter table inf_ru_execution
    add `complete_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '完成人';