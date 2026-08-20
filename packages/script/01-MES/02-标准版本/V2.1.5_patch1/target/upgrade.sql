# 检验菜单
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100, '检验管理', '120100', 120, 0, 1, 0, null, 130170, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100001, '请验单配置', '120100001', 120100, 0, 1, 0, null, 130170110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100001000001, '新增请验单', '120100001000001', 120100001, 0, 0, 0, null, 120100001110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100001000002, '编辑请验单', '120100001000002', 120100001, 0, 0, 0, null, 120100001120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100001000003, '删除请验单', '120100001000003', 120100001, 0, 0, 0, null, 120100001130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100001000004, '查看请验单', '120100001000004', 120100001, 0, 0, 0, null, 120100001140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100001000005, '绑定物料', '120100001000005', 120100001, 0, 0, 0, null, 120100001150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100001000006, '启停', '120100001000006', 120100001, 0, 0, 0, null, 120100001160, now(), now(), '1', '1', 0, null);

# 查看检验结果按钮
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030007000007, '检验结果', '120030007000007', 120030007, 0, 0, 0, null, 120030007121, now(), now(), '1', '1', 0, null);
# 检验结果组件配置权限 by lilong 250218
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020001002026, '检验结果', '120020001002026', 120020001002, 0, 0, 0, null, 120020001120026, now(), now(), '1', '1', 0, null);

# 调整楼宇管理权限错误 by lilong 250312
UPDATE bmos_platform.bp_menu SET code = '200020001000001', parent_id = 200020001, sort = 200020001110 WHERE id = 200020001000001;
UPDATE bmos_platform.bp_menu SET code = '200020001000002', parent_id = 200020001, sort = 200020001120 WHERE id = 200020001000002;
UPDATE bmos_platform.bp_menu SET code = '200020001000003', parent_id = 200020001, sort = 200020001130 WHERE id = 200020001000003;
UPDATE bmos_platform.bp_menu SET code = '200020001000004', parent_id = 200020001, sort = 200020001140 WHERE id = 200020001000004;
UPDATE bmos_platform.bp_menu SET code = '200020001000005', parent_id = 200020001, sort = 200020001150 WHERE id = 200020001000005;
UPDATE bmos_platform.bp_menu SET code = '200020001000006', parent_id = 200020001, sort = 200020001160 WHERE id = 200020001000006;

# 产线管理-数据权限按钮 by lilong 250313
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030001000011, '数据权限', '160030001000011', 160030001, 0, 0, 0, null, 160030001210, now(), now(), '1', '1', 0, null);
# 请验单自定义字段字典 by zhangruoyu
INSERT INTO bmos_platform.bp_dict (id, dict_name, dict_code, state, create_by, update_by, create_time, update_time, is_deleted, del_flag) VALUES (120100001001, '请验单自定义字段', 'InspectionCustomFields', 1, '1', '', now(), now(), 0, 0);
# 请验单自定义字段字典 by lilong 250218
INSERT INTO bmos_platform.bp_dict (id, dict_name, dict_code, state, create_by, update_by, create_time, update_time, is_deleted, del_flag) VALUES (120100001002, '检验结果自定义字段', 'InspectionResultCustomFields', 1, '1', '', now(), now(), 0, 0);
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
# 调整配液产出的标签信息 by lilong 250312
UPDATE bmos_platform.bp_tag_scene SET data_source_interface = '/api/app/mes/tag/print/PREPARATION_PRODUCE_STORAGE_MATERIAL' WHERE id = 121002010;
UPDATE bmos_platform.bp_tag_scene SET data_source_interface = '/api/app/mes/tag/print/PREPARATION_PRODUCE_STORAGE_MATERIAL' WHERE id = 121002011;
UPDATE bmos_platform.bp_tag_scene_field SET field = 'producerName', label = '产出人员' WHERE id = 121002010008;
UPDATE bmos_platform.bp_tag_scene_field SET field = 'produceTime', label = '操作时间' WHERE id = 121002010010;
UPDATE bmos_platform.bp_tag_scene_field SET field = 'producerName', label = '产出人员' WHERE id = 121002011008;
UPDATE bmos_platform.bp_tag_scene_field SET field = 'produceTime', label = '操作时间' WHERE id = 121002011010;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"物料信息：","defineField":"field1","dataSourceField":"fullName","consumeValue":null},{"label":"物料批号：","defineField":"field2","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"物料件号：","defineField":"field3","dataSourceField":"materialNo","consumeValue":null},{"label":"物料量：","defineField":"field4","dataSourceField":"quantityWithUnit","consumeValue":null},{"label":"产出人员：","defineField":"field5","dataSourceField":"producerName","consumeValue":null},{"label":"操作时间：","defineField":"field6","dataSourceField":"produceTime","consumeValue":null},{"label":"产品名称：","defineField":"field7","dataSourceField":"productName","consumeValue":null},{"label":"产品规格：","defineField":"field8","dataSourceField":"productSpecification","consumeValue":null},{"label":"生产批号：","defineField":"field9","dataSourceField":"batchNo","consumeValue":null}]' WHERE id = 121002010;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"物料信息：","defineField":"field1","dataSourceField":"fullName","consumeValue":null},{"label":"物料批号：","defineField":"field2","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"物料件号：","defineField":"field3","dataSourceField":"materialNo","consumeValue":null},{"label":"物料量：","defineField":"field4","dataSourceField":"quantityWithUnit","consumeValue":null},{"label":"产出人员：","defineField":"field5","dataSourceField":"producerName","consumeValue":null},{"label":"操作时间：","defineField":"field6","dataSourceField":"produceTime","consumeValue":null},{"label":"产品名称：","defineField":"field7","dataSourceField":"productName","consumeValue":null},{"label":"产品规格：","defineField":"field8","dataSourceField":"productSpecification","consumeValue":null},{"label":"生产批号：","defineField":"field9","dataSourceField":"batchNo","consumeValue":null}]' WHERE id = 121002011;
# 水印字体存放路径 by zhangruoyu 250221
INSERT INTO bmos_platform.bp_business_parameter (id, code, name, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100033, 'platform.sys.watermark-font-path', '水印字体存放路径', '/SimSun.ttf', 'STRING', 'BUSINESS', '生产', '数值组件趋势分析近n个批次', 100330, null, 1, '1', '1', now(), now(), 0);
# 系统AI服务地址 by lilong 250221
INSERT INTO bmos_platform.bp_business_parameter (id, code, name, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100034, 'platform.sys.AI-url', '系统AI服务地址', 'http://172.30.1.137/chatbot/7wQ8VGXuhydA4VEf', 'STRING', 'BUSINESS', '平台', '系统AI服务地址', 100340, '', 1, null, null, now(), now(), 0);
# 更新操作规程水印字体存放路径 by lilong 250226
UPDATE bmos_platform.bp_business_parameter SET name = '操作规程水印字体存放路径', value = '/usr/share/fonts/chinese/SimSun.ttf' WHERE id = 100033;

# 新增批记录照片归档正则表达式配置 by lilong 250228
INSERT INTO bmos_platform.bp_business_parameter (id, code, name, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120015, 'mes.record.archive-photos-regular', '批记录照片归档正则表达式配置', '{"take_photo":"\$\{<take_photo>(\[[\u4e00-\u9fa5a-zA-Z0-9#(),%.*（），、\\-\\s]+]){2}(\[\d*]){3}}","evidence_photo":"\$\{<evidence_photo>(\[[\u4e00-\u9fa5a-zA-Z0-9#(),%.*（），、\\-\\s]+]){2}(\[\d*]){3}}"}', 'JSON', 'BUSINESS', '生产', '批记录照片归档正则表达式配置:take_photo-拍照组件,evidence_photo-拍照取证', 120250, '', 1, '1', '1', now(), now(), 0);
UPDATE bmos_platform.bp_business_parameter SET value = '{"evidence_photo":"\\$\\{<evidence_photo>(\\[[\\u4e00-\\u9fa5a-zA-Z0-9#()（）%.*,，、\\-\\s]+]){2}(\\[\\d*]){3}}","take_photo":"\\$\\{<take_photo>(\\[[\\u4e00-\\u9fa5a-zA-Z0-9#()（）%.*,，、\\-\\s]+]){2}(\\[\\d*]){3}}"}' WHERE id = 120015;

# 更新操作规程水印字体存放路径参数描述信息 by lilong 250319
UPDATE bmos_platform.bp_business_parameter SET description = '操作规程水印字体存放路径' WHERE id = 100033;
# 批记录照片归档正则表达式配置 by dengke 250321
UPDATE `bmos_platform`.`bp_business_parameter` SET `value` = '{\"evidence_photo\":\"\\\\$\\\\{<evidence_photo>(\\\\[[\\\\u4e00-\\\\u9fa5a-zA-Z0-9#()（）%.*,，、\\\\-\\\\s]+]){2}(\\\\[\\\\d*]){3}}\",\"take_photo\":\"\\\\$\\\\{<take_photo>(\\\\[[\\\\u4e00-\\\\u9fa5a-zA-Z0-9#()（）%.*,，、\\\\-\\\\s]+]){2}(\\\\[\\\\d*]){3}}\"}' WHERE `id` = 120015;
