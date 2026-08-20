# 生产操作规程启用审核流程 by lilong 240628
INSERT INTO bmos_mes.bm_flow_audit_category (id, name, code, parent_id, create_by, update_by, create_time, update_time, is_deleted, tree_code, tree_name) VALUES (120020004, '操作规程启用审核', '120020004', 120020, '1', '1', now(), now(), 0, '120020004', '生产配置/操作规程启用审核');
# 更新配方为生产BOM by lilong 240628
UPDATE bmos_mes.bm_flow_audit_category SET name = '生产BOM审批', tree_name = '生产配置/生产BOM审批' WHERE id = 120020003;

# 更新生产指令单和操作规程的流程命名 by lilong 240704
UPDATE bmos_mes.bm_flow_audit_category SET name = '生产指令单', tree_name = '生产指令单' WHERE id = 120030;
UPDATE bmos_mes.bm_flow_audit_category SET name = '指令单审批', tree_name = '生产指令单/指令单审批' WHERE id = 120030001;
UPDATE bmos_mes.bm_flow_audit_category SET name = '操作规程' WHERE id = 120020004;

# 更新操作规程审批命名 by lilong 240709
UPDATE bmos_mes.bm_flow_audit_category SET name = '操作规程审批' WHERE id = 120020004;
