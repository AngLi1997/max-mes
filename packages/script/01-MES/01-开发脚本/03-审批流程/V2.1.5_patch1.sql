# 更新审批流程的id相关信息 by lilong 250227
UPDATE bmos_mes.bm_flow_audit_category SET id = 12002001, code = '12002001', parent_id = 0, tree_code = '12002001,12002000101,12002000201,12002000301' WHERE id = 120020;
UPDATE bmos_mes.bm_flow_audit_category SET id = 12003001, code = '12003001', parent_id = 0, tree_code = '12003001,12003000101' WHERE id = 120030;
UPDATE bmos_mes.bm_flow_audit_category SET id = 12004001, code = '12004001', parent_id = 0, tree_code = '12004001,12004000101' WHERE id = 120040;
UPDATE bmos_mes.bm_flow_audit_category SET id = 12005001, code = '12005001', parent_id = 0, tree_code = '12005001,12005000101' WHERE id = 120050;
UPDATE bmos_mes.bm_flow_audit_category SET id = 12002000101, code = '12002000101', parent_id = 12002001, tree_code = '12002000101' WHERE id = 120020001;
UPDATE bmos_mes.bm_flow_audit_category SET id = 12002000201, code = '12002000201', parent_id = 12002001, tree_code = '12002000201' WHERE id = 120020002;
UPDATE bmos_mes.bm_flow_audit_category SET id = 12002000301, code = '12002000301', parent_id = 12002001, tree_code = '12002000301' WHERE id = 120020003;
UPDATE bmos_mes.bm_flow_audit_category SET id = 12002000401, code = '12002000401', parent_id = 12002001, tree_code = '12002000401' WHERE id = 120020004;
UPDATE bmos_mes.bm_flow_audit_category SET id = 12003000101, code = '12003000101', parent_id = 12003001, tree_code = '12003000101' WHERE id = 120030001;
UPDATE bmos_mes.bm_flow_audit_category SET id = 12004000101, code = '12004000101', parent_id = 12004001, tree_code = '12004000101' WHERE id = 120040001;
UPDATE bmos_mes.bm_flow_audit_category SET id = 12005000101, code = '12005000101', parent_id = 12005001, tree_code = '12005000101' WHERE id = 120050001;
