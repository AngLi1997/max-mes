alter table bm_weigh_requirement
    modify weigh_process int null comment '称量阶段 1 物料称量 2 更换物料批次 3 余料称量 4 已完成称量 5 已完成签名 6 更换余料批次';