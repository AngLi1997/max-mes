INSERT INTO bmos_platform.bp_tag_scene (id, tag_scene_name, tag_scene_desc, tag_type_id, data_source_service_name,
                                        data_source_interface, qr_code_field, sort, create_time, update_time, create_by,
                                        update_by, is_deleted)
VALUES (130020003, '留样样品信息打印标签', '留样样品信息打印标签', 7, 'bmos-lims2-service', '/api/app/lims2/sample/retention/printTag',
        'sampleNo', 7110, '2025-09-22 10:35:08', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003001, 130020003, 'materialName', '检品名称', 'String', '氯化钠', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003002, 130020003, 'materialCode', '检品编码', 'String', '0001', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003003, 130020003, 'fullMaterialName', '检品信息', 'String', '0001-氯化钠', '2025-09-22 11:39:28', null, null,
        null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003005, 130020003, 'materialSpec', '检品规格', 'String', '500g/袋', '2025-09-22 11:39:28', null, null, null,
        0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003006, 130020003, 'batchNo', '批号', 'String', '20250901', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003007, 130020003, 'sampleNo', '样品编号', 'String', 'Sample20250901', '2025-09-22 11:39:28', null, null,
        null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003012, 130020003, 'quantityWithUnit', '样品数量', 'String', '100g', '2025-09-22 11:39:28', null, null, null,
        0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003014, 130020003, 'samplingTime', '取样时间', 'String', '2025-09-22 11:39:28', '2025-09-22 11:39:28', null,
        null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003015, 130020003, 'retentionUserName', '留样人', 'String', '2025-09-22 11:39:28', '2025-09-22 11:39:28', null,
        null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003016, 130020003, 'retentionTime', '留样时间', 'String', '2025-09-22 11:39:28', '2025-09-22 11:39:28', null,
        null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003017, 130020003, 'retentionExpiryDate', '留样期限', 'String', '2025-09-22 11:39:28', '2025-09-22 11:39:28', null,
        null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003018, 130020003, 'storageLocation', '储存位置', 'String', '2025-09-22 11:39:28', '2025-09-22 11:39:28', null,
        null, null, 0);


INSERT INTO bmos_platform.bp_tag_instance (id, tag_name, tag_type_id, tag_scene_id, tag_define_id, config_fields,
                                           is_enable, create_time, update_time, create_by, update_by, is_deleted)
VALUES (130020003, '留样样品信息打印标签', 7, 130020003, 2,
        '[{"label":"","defineField":"field1","dataSourceField":"fullMaterialName","consumeValue":null},{"label":"","defineField":"field2","dataSourceField":"quantityWithUnit","consumeValue":null},{"label":"","defineField":"field3","dataSourceField":"retentionTime","consumeValue":null},{"label":"","defineField":"field4","dataSourceField":"retentionExpiryDate","consumeValue":null}]',
        'TRUE', '2025-09-22 11:19:01', '2025-09-22 14:22:24', null, '888888888888888873', 0);



