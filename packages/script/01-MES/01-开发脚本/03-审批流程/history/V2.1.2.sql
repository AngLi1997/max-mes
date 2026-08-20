# 批记录审批 by lilong 240822
INSERT INTO bmos_mes.bm_flow_audit_category (id, name, code, parent_id, create_by, update_by, create_time, update_time, is_deleted, tree_code, tree_name) VALUES (120050, '批记录', '120050', 0, '1', '1', now(), now(), 0, '120050,120050001', '批记录');
INSERT INTO bmos_mes.bm_flow_audit_category (id, name, code, parent_id, create_by, update_by, create_time, update_time, is_deleted, tree_code, tree_name) VALUES (120050001, '批记录审核', '120050001', 120050, '1', '1', now(), now(), 0, '120050001', '批记录/批记录审核');
