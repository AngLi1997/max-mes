alter table bm_weigh_task
    add storage_material_batch_id bigint null comment '当前称量的物料批次' after remark;