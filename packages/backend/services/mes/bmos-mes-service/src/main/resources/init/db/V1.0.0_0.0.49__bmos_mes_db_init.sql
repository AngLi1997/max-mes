use bmos_mes;
-- 删除操作规程内置停用流程数据
DELETE FROM `bmos_mes`.`bm_flow_audit_category` WHERE id = '120020005';

DELETE FROM `bmos_mes`.`bm_flow_audit` WHERE id = '1740318445775486978';

DELETE FROM `bmos_mes`.`bm_flow_audit_version` WHERE id = '1740318446748598278';

DELETE FROM `bmos_mes`.`audit_deployment` WHERE id = '6';