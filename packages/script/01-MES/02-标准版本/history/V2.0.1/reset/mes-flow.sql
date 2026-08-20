# 初始化mes流程配置信息
truncate table bmos_mes.bm_flow_audit_category;
select * from bmos_mes.bm_flow_audit_category order by id;

insert into bmos_mes.bm_flow_audit_category (id, name, code, parent_id) values (120020, '生产配置', 120020, 0);
insert into bmos_mes.bm_flow_audit_category (id, name, code, parent_id) values (120020001, '记录审批', 120020001, 120020);
insert into bmos_mes.bm_flow_audit_category (id, name, code, parent_id) values (120020002, '工艺审批', 120020002, 120020);
insert into bmos_mes.bm_flow_audit_category (id, name, code, parent_id) values (120030, '生产管理', 120030, 0);
insert into bmos_mes.bm_flow_audit_category (id, name, code, parent_id) values (120030001, '生产计划审批', 120030001, 120030);
insert into bmos_mes.bm_flow_audit_category (id, name, code, parent_id) values (120040, '批签发', 120040, 0);
insert into bmos_mes.bm_flow_audit_category (id, name, code, parent_id) values (120040001, '批签发审核', 120040001, 120040);

# 当前时间刷新
update bmos_mes.bm_flow_audit_category set create_time = now(), update_time = now(), create_by = 1, update_by = 1 where 1 = 1;
