# 补充流程配置分类层级信息 by renjinguang 240301
UPDATE bmos_mes.bm_flow_audit_category SET name = '记录配置', code = '120020', parent_id = 0, create_by = '1', create_time = now(),  is_deleted = 0, tree_code = '120020,120020001,120020002', tree_name = '记录配置' WHERE id = 120020;
UPDATE bmos_mes.bm_flow_audit_category SET name = '生产计划', code = '120030', parent_id = 0, create_by = '1', create_time = now(),  is_deleted = 0, tree_code = '120030,120030001', tree_name = '生产计划' WHERE id = 120030;
UPDATE bmos_mes.bm_flow_audit_category SET name = '批签发', code = '120040', parent_id = 0, create_by = '1', create_time = now(),  is_deleted = 0, tree_code = '120040,120040001', tree_name = '批签发' WHERE id = 120040;
UPDATE bmos_mes.bm_flow_audit_category SET name = '记录审批', code = '120020001', parent_id = 120020, create_by = '1', create_time = now(),  is_deleted = 0, tree_code = '120020001', tree_name = '记录配置-记录审批' WHERE id = 120020001;
UPDATE bmos_mes.bm_flow_audit_category SET name = '工艺审批', code = '120020002', parent_id = 120020, create_by = '1', create_time = now(),  is_deleted = 0, tree_code = '120020002', tree_name = '记录配置-工艺审批' WHERE id = 120020002;
UPDATE bmos_mes.bm_flow_audit_category SET name = '生产计划审批', code = '120030001', parent_id = 120030, create_by = '1', create_time = now(),  is_deleted = 0, tree_code = '120030001', tree_name = '生产计划-生产计划审批' WHERE id = 120030001;
UPDATE bmos_mes.bm_flow_audit_category SET name = '批签发审核', code = '120040001', parent_id = 120040, create_by = '1', create_time = now(),  is_deleted = 0, tree_code = '120040001', tree_name = '批签发-批签发审核' WHERE id = 120040001;

# 增加配方审批的审批流分类 by lilong 240307
INSERT INTO bmos_mes.bm_flow_audit_category (id, name, code, parent_id, create_by, update_by, create_time, update_time, is_deleted, tree_code, tree_name) VALUES (120020003, '配方审批', '120020003', 120020, '1', null, now(), null, 0, '120020003', '生产配置-配方审批');
UPDATE bmos_mes.bm_flow_audit_category SET name = '生产配置', tree_code = '12002,120020001,120020002,120020003', tree_name = '生产配置' WHERE id = 120020;
UPDATE bmos_mes.bm_flow_audit_category SET tree_name = '生产配置-记录审批' WHERE id = 120020001;
UPDATE bmos_mes.bm_flow_audit_category SET tree_name = '生产配置-工艺审批' WHERE id = 120020002;
