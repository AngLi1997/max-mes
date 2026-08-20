# UI审查将"-"调整为"/" by renjinguang 240513
UPDATE bmos_mes.bm_flow_audit_category SET tree_name = '生产配置/记录审批' WHERE id = 120020001;
UPDATE bmos_mes.bm_flow_audit_category SET tree_name = '生产配置/工艺审批' WHERE id = 120020002;
UPDATE bmos_mes.bm_flow_audit_category SET tree_name = '生产配置/配方审批' WHERE id = 120020003;
UPDATE bmos_mes.bm_flow_audit_category SET tree_name = '生产计划/生产计划审批' WHERE id = 120030001;
UPDATE bmos_mes.bm_flow_audit_category SET tree_name = '批签发/批签发审核' WHERE id = 120040001;
