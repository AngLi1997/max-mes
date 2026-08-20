INSERT INTO bmos_platform.bp_message_template (id, title_template, content_template, message_type, create_by, update_by, create_time, update_time, is_deleted) VALUES (1, '设备故障告警：#{#equipmentCode}-#{#equipmentName}', null, 'EQUIPMENT_DEFAULT_WARNING', '1', '1', '2025-01-13 10:31:45', '2025-01-13 10:31:47', 0);
INSERT INTO bmos_platform.bp_message_template (id, title_template, content_template, message_type, create_by, update_by, create_time, update_time, is_deleted) VALUES (2, '物料批次近效期预警：#{#materialCode}-#{#materialName}，物料批号：#{#batchNo}', null, 'MATERIAL_EXPIRE_FORE_WARNING', null, null, null, null, 0);
INSERT INTO bmos_platform.bp_message_template (id, title_template, content_template, message_type, create_by, update_by, create_time, update_time, is_deleted) VALUES (5, '#{#applyUserName}#{#auditNodeName}：#{#businessText}', '审核意见：#{#auditContent}
备注：#{#auditRemark}', 'AUDIT', '1', '1', '2025-01-13 11:51:00', '2025-01-13 11:51:03', 0);
INSERT INTO bmos_platform.bp_message_template (id, title_template, content_template, message_type, create_by, update_by, create_time, update_time, is_deleted) VALUES (3, '生产修订异常告警：#{#abnormalDescription}
', '
生产批号：#{#batchNo}
异常节点：#{#processName}-#{#procedureName}-#{#procedureStepName}', 'PRODUCT_MODIFY_ABNORMAL_WARNING', null, null, null, null, 0);
INSERT INTO bmos_platform.bp_message_template (id, title_template, content_template, message_type, create_by, update_by, create_time, update_time, is_deleted) VALUES (4, '数据超限异常告警：#{#abnormalDescription}', '生产批号：#{#batchNo}
异常节点：#{#processName}-#{#procedureName}-#{#procedureStepName}', 'DATA_OUT_LIMIT_WARNING', null, null, null, null, 0);