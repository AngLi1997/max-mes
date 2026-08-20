INSERT INTO bmos_platform.bp_tag_scene (id, tag_scene_name, tag_scene_desc, tag_type_id, data_source_service_name,
                                        data_source_interface, qr_code_field, sort, create_time, update_time, create_by,
                                        update_by, is_deleted)
VALUES (130020004, '稳定性考察打印标签', '稳定性考察打印标签', 7, 'bmos-lims2-service', '/api/app/lims2/sample/stability/printTag',
        'sampleNo', 8110, '2025-09-22 10:35:08', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004001, 130020004, 'sampleNo', '样品编号', 'String', 'S25060200201', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004002, 130020004, 'materialName', '检品名称', 'String', '复方氨酚烷胺胶囊', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004003, 130020004, 'materialCode', '检品编码', 'String', 'WH030101', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004004, 130020004, 'fullMaterialName', '检品信息', 'String', 'WH030101-复方氨酚烷胺胶囊', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004005, 130020004, 'materialSpec', '检品规格', 'String', '12粒/盒', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004006, 130020004, 'batchNo', '批号', 'String', 'T000000015', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004007, 130020004, 'quantityWithUnit', '样品数量', 'String', '3盒', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004008, 130020004, 'samplingTime', '取样时间', 'String', '2025/03/01 09:32:32', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004009, 130020004, 'samplerName', '取样人', 'String', 'ZS-张三', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004010, 130020004, 'experimentType', '试验类型', 'String', '长期试验', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004011, 130020004, 'storageCondition', '试验储存条件', 'String', '25±2℃、60%±10%RH', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004012, 130020004, 'storageLocation', '储存位置', 'String', 'B2-1-103', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004013, 130020004, 'productionDate', '生产日期', 'String', '2025/03/01', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004014, 130020004, 'receiveDate', '样品接收日期', 'String', '2025/03/01 11:32:32', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020004015, 130020004, 'planCode', '稳定性考察编号', 'String', 'W000000015', '2025-09-22 11:39:28', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_instance (id, tag_name, tag_type_id, tag_scene_id, tag_define_id, config_fields,
                                           is_enable, create_time, update_time, create_by, update_by, is_deleted)
VALUES (130020004, '稳定性考察打印标签', 7, 130020004, 2,
        '[{"label":"","defineField":"field1","dataSourceField":"fullMaterialName","consumeValue":null},{"label":"","defineField":"field2","dataSourceField":"batchNo","consumeValue":null},{"label":"","defineField":"field3","dataSourceField":"experimentType","consumeValue":null},{"label":"","defineField":"field4","dataSourceField":"sampleNo","consumeValue":null}]',
        'TRUE', '2025-09-22 11:19:01', null, null, null, 0);

-- 稳定性周期任务取样打印标签
INSERT INTO bmos_platform.bp_tag_scene (id, tag_scene_name, tag_scene_desc, tag_type_id, data_source_service_name,
                                        data_source_interface, qr_code_field, sort, create_time, update_time, create_by,
                                        update_by, is_deleted)
VALUES (130020005, '稳定性周期任务取样标签', '稳定性周期任务取样标签', 7, 'bmos-lims2-service', '/api/app/lims2/sample/stability/timepoint/printTag',
        'sampleNo', 8120, '2025-09-22 10:35:08', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005001, 130020005, 'sampleNo', '样品编号', 'String', 'S25060200201', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005002, 130020005, 'materialName', '检品名称', 'String', '复方氨酚烷胺胶囊', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005003, 130020005, 'materialCode', '检品编码', 'String', 'WH030101', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005004, 130020005, 'fullMaterialName', '检品信息', 'String', 'WH030101-复方氨酚烷胺胶囊', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005005, 130020005, 'materialSpec', '检品规格', 'String', '12粒/盒', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005006, 130020005, 'batchNo', '批号', 'String', 'T000000015', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005007, 130020005, 'quantityWithUnit', '样品数量', 'String', '3盒', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005008, 130020005, 'samplingTime', '取样时间', 'String', '2025/03/01 09:32:32', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005009, 130020005, 'samplerName', '取样人', 'String', 'ZS-张三', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005010, 130020005, 'experimentType', '试验类型', 'String', '长期试验', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005011, 130020005, 'storageCondition', '试验储存条件', 'String', '25±2℃、60%±10%RH', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005012, 130020005, 'storageLocation', '储存位置', 'String', 'B2-1-103', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005013, 130020005, 'productionDate', '生产日期', 'String', '2025/03/01', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005014, 130020005, 'receiveDate', '样品接收日期', 'String', '2025/03/01 11:32:32', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020005015, 130020005, 'planCode', '稳定性考察编号', 'String', 'W000000015', '2025-09-22 11:39:28', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_instance (id, tag_name, tag_type_id, tag_scene_id, tag_define_id, config_fields,
                                           is_enable, create_time, update_time, create_by, update_by, is_deleted)
VALUES (130020005, '稳定性周期任务取样标签', 7, 130020005, 2,
        '[{"label":"","defineField":"field1","dataSourceField":"fullMaterialName","consumeValue":null},{"label":"","defineField":"field2","dataSourceField":"batchNo","consumeValue":null},{"label":"","defineField":"field3","dataSourceField":"experimentType","consumeValue":null},{"label":"","defineField":"field4","dataSourceField":"sampleNo","consumeValue":null}]',
        'TRUE', '2025-09-22 11:19:01', null, null, null, 0);
