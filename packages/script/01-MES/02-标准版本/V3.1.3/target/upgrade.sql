-- =============================================
-- 字典配置 - 稳定性考察试验类型
-- 描述:稳定性考察方案检验计划试验类型内置字典
-- 创建时间:2026-03-19
-- =============================================
INSERT INTO bmos_platform.bp_dict (id, dict_name, dict_code, state, create_by, update_by, create_time, update_time, is_deleted, del_flag) VALUES (130060001001, '稳定性考察试验类型', 'StabilityExperimentType', 1, '1', '', now(), now(), 0, 0) ON DUPLICATE KEY UPDATE dict_name = VALUES(dict_name), dict_code = VALUES(dict_code), state = VALUES(state), update_time = now();

INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (130060001001001, '影响因素实验', 'INFLUENCE_FACTOR', 130060001001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0) ON DUPLICATE KEY UPDATE dict_label = VALUES(dict_label), dict_value = VALUES(dict_value), update_time = VALUES(update_time);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (130060001001002, '加速实验', 'ACCELERATED', 130060001001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0) ON DUPLICATE KEY UPDATE dict_label = VALUES(dict_label), dict_value = VALUES(dict_value), update_time = VALUES(update_time);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (130060001001003, '长期试验', 'LONG_TERM', 130060001001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0) ON DUPLICATE KEY UPDATE dict_label = VALUES(dict_label), dict_value = VALUES(dict_value), update_time = VALUES(update_time);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (130060001001004, '中间条件实验', 'INTERMEDIATE', 130060001001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0) ON DUPLICATE KEY UPDATE dict_label = VALUES(dict_label), dict_value = VALUES(dict_value), update_time = VALUES(update_time);
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
-- =============================================
-- 菜单配置 - 稳定性考察
-- 描述:新增稳定性考察模块及其子菜单、按钮权限
-- 版本:V3.1.2
-- 创建时间:2026-03-17
-- 说明:稳定性考察一级菜单位于留样管理与报告管理之间
-- =============================================

-- =============================================
-- 一级菜单配置 - 稳定性考察
-- 描述:稳定性考察模块一级菜单（位于留样管理和报告管理之间）
-- 创建时间:2026-03-17
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130060, '稳定性考察', '130060', 130, 0, 1, 0, NULL, 150127, NOW(), NOW(), '1', '1', 0, NULL, 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTgiIGhlaWdodD0iMTgiIHZpZXdCb3g9IjAgMCAxOCAxOCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGcgY2xpcC1wYXRoPSJ1cmwoI2NsaXAwXzI5NzVfOTc4MjApIj4KPHBhdGggZD0iTTEwLjYzMTQgMS43MTQyOUgxNC4yMTQzQzE1LjE1NzEgMS43MTQyOSAxNS45Mjg2IDIuNDg1NzEgMTUuOTI4NiAzLjQyODU3VjcuNzE0MjhDMTUuOTI4NiA4LjE4NzY3IDE1LjU0NDggOC41NzE0MyAxNS4wNzE0IDguNTcxNDNDMTQuNTk4IDguNTcxNDMgMTQuMjE0MyA4LjE4NzY3IDE0LjIxNDMgNy43MTQyOVYzLjQyODU3SDEyLjVWNC4xQzEyLjUgNC4zMjA5MSAxMi4zMjA5IDQuNSAxMi4xIDQuNUg0LjQyODU3QzQuMTUyNDMgNC41IDMuOTI4NTcgNC4yNzYxNCAzLjkyODU3IDRWMy40Mjg1N0gyLjIxNDI5VjE2LjI4NTdINi41QzYuOTczMzkgMTYuMjg1NyA3LjM1NzE0IDE2LjY2OTUgNy4zNTcxNCAxNy4xNDI5QzcuMzU3MTQgMTcuNjE2MiA2Ljk3MzM5IDE4IDYuNSAxOEgyLjIxNDI5QzEuMjcxNDMgMTggMC41IDE3LjIyODYgMC41IDE2LjI4NTdWMy40Mjg1N0MwLjUgMi40ODU3MSAxLjI3MTQzIDEuNzE0MjkgMi4yMTQyOSAxLjcxNDI5SDUuNzk3MTRDNi4xNTcxNCAwLjcyIDcuMSAwIDguMjE0MjkgMEM5LjMyODU3IDAgMTAuMjcxNCAwLjcyIDEwLjYzMTQgMS43MTQyOVpNOS4wNzE0MyAyLjU3MTQzQzkuMDcxNDMgMi4xIDguNjg1NzEgMS43MTQyOSA4LjIxNDI5IDEuNzE0MjlDNy43NDI4NiAxLjcxNDI5IDcuMzU3MTQgMi4xIDcuMzU3MTQgMi41NzE0M0M3LjM1NzE0IDMuMDQyODYgNy43NDI4NiAzLjQyODU3IDguMjE0MjkgMy40Mjg1N0M4LjY4NTcxIDMuNDI4NTcgOS4wNzE0MyAzLjA0Mjg2IDkuMDcxNDMgMi41NzE0M1pNMTIuNzYxMSAxMS4xODU3QzEyLjgzMjggMTEuMDY0MSAxMi45MzU5IDEwLjk2MzEgMTMuMDYgMTAuODkyOUMxMy4xODQyIDEwLjgyMjcgMTMuMzI1IDEwLjc4NTcgMTMuNDY4NCAxMC43ODU3QzEzLjYxMTcgMTAuNzg1NyAxMy43NTI2IDEwLjgyMjcgMTMuODc2NyAxMC44OTI5QzE0LjAwMDkgMTAuOTYzMSAxNC4xMDQgMTEuMDY0MSAxNC4xNzU3IDExLjE4NTdMMTcuMzU4NiAxNi41ODU3QzE3LjQzMDIgMTYuNzA3MyAxNy40NjggMTYuODQ1MyAxNy40NjggMTYuOTg1N0MxNy40NjggMTcuMTI2MSAxNy40MzAyIDE3LjI2NDEgMTcuMzU4NiAxNy4zODU3QzE3LjI4NjkgMTcuNTA3MyAxNy4xODM4IDE3LjYwODMgMTcuMDU5NiAxNy42Nzg1QzE2LjkzNTUgMTcuNzQ4NyAxNi43OTQ2IDE3Ljc4NTcgMTYuNjUxMyAxNy43ODU3SDEwLjI4NTVDMTAuMTQyMSAxNy43ODU5IDEwLjAwMTEgMTcuNzQ5IDkuODc2ODcgMTcuNjc4OEM5Ljc1MjYgMTcuNjA4NyA5LjY0OTM4IDE3LjUwNzcgOS41Nzc2IDE3LjM4NkM5LjUwNTgyIDE3LjI2NDQgOS40NjgwMSAxNy4xMjY0IDkuNDY3OTggMTYuOTg1OUM5LjQ2Nzk0IDE2Ljg0NTQgOS41MDU2OCAxNi43MDc0IDkuNTc3NCAxNi41ODU3TDEyLjc2MTEgMTEuMTg1N1oiIGZpbGw9IndoaXRlIi8+CjxyZWN0IHg9IjMuOCIgeT0iNyIgd2lkdGg9IjgiIGhlaWdodD0iMS41IiByeD0iMC43NSIgZmlsbD0id2hpdGUiLz4KPHJlY3QgeD0iMy44IiB5PSIxMCIgd2lkdGg9IjUiIGhlaWdodD0iMS41IiByeD0iMC43NSIgZmlsbD0id2hpdGUiLz4KPC9nPgo8ZGVmcz4KPGNsaXBQYXRoIGlkPSJjbGlwMF8yOTc1Xzk3ODIwIj4KPHJlY3Qgd2lkdGg9IjE4IiBoZWlnaHQ9IjE4IiBmaWxsPSJ3aGl0ZSIvPgo8L2NsaXBQYXRoPgo8L2RlZnM+Cjwvc3ZnPgo=') ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);

-- =============================================
-- 二级菜单配置 - 稳定性考察子菜单
-- 描述:稳定性考察模块下的子菜单
-- 创建时间:2026-03-17
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130060001, '稳定性方案管理', '130060001', 130060, 0, 1, 0, NULL, 150127010, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130060002, '稳定性方案审批', '130060002', 130060, 0, 1, 0, NULL, 150127020, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130060003, '稳定性考察计划', '130060003', 130060, 0, 1, 0, NULL, 150127030, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130060004, '稳定性样品接收', '130060004', 130060, 0, 1, 0, NULL, 150127040, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130060005, '稳定性样品管理', '130060005', 130060, 0, 1, 0, NULL, 150127050, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130060006, '稳定性样品取样', '130060006', 130060, 0, 1, 0, NULL, 150127060, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130060007, '稳定性结果审核', '130060007', 130060, 0, 1, 0, NULL, 150127070, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130060008, '稳定性统计查询', '130060008', 130060, 0, 1, 0, NULL, 150127080, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130060009, '稳定性趋势查询', '130060009', 130060, 0, 1, 0, NULL, 150127090, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);

-- =============================================
-- 按钮权限配置 - 稳定性方案管理
-- 描述:稳定性方案管理菜单下的按钮权限配置
-- 创建时间:2026-03-17
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060001000001, '新增方案', '130060001000001', 130060001, 0, 0, 0, NULL, 130060001010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060001000002, '数据权限', '130060001000002', 130060001, 0, 0, 0, NULL, 130060001020, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060001000003, '复制方案', '130060001000003', 130060001, 0, 0, 0, NULL, 130060001030, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060001000004, '新增版本', '130060001000004', 130060001, 0, 0, 0, NULL, 130060001040, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060001000005, '编辑', '130060001000005', 130060001, 0, 0, 0, NULL, 130060001050, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060001000006, '启用', '130060001000006', 130060001, 0, 0, 0, NULL, 130060001060, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060001000007, '停用', '130060001000007', 130060001, 0, 0, 0, NULL, 130060001070, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060001000008, '审批进度', '130060001000008', 130060001, 0, 0, 0, NULL, 130060001080, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060001000009, '作废', '130060001000009', 130060001, 0, 0, 0, NULL, 130060001090, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060001000010, '历史', '130060001000010', 130060001, 0, 0, 0, NULL, 130060001100, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 稳定性方案审批
-- 描述:稳定性方案审批菜单下的按钮权限配置
-- 创建时间:2026-03-17
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060002000001, '处理', '130060002000001', 130060002, 0, 0, 0, NULL, 130060002010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060002000002, '审批进度', '130060002000002', 130060002, 0, 0, 0, NULL, 130060002020, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 稳定性考察计划
-- 描述:稳定性考察计划菜单下的按钮权限配置
-- 创建时间:2026-03-17
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060003000001, '新建', '130060003000001', 130060003, 0, 0, 0, NULL, 130060003010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060003000002, '编辑', '130060003000002', 130060003, 0, 0, 0, NULL, 130060003020, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060003000003, '暂停', '130060003000003', 130060003, 0, 0, 0, NULL, 130060003030, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060003000004, '恢复', '130060003000004', 130060003, 0, 0, 0, NULL, 130060003040, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060003000005, '查看详情', '130060003000005', 130060003, 0, 0, 0, NULL, 130060003050, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 稳定性样品接收
-- 描述:稳定性样品接收菜单下的按钮权限配置
-- 创建时间:2026-03-17
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060004000001, '接收', '130060004000001', 130060004, 0, 0, 0, NULL, 130060004010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 稳定性样品管理
-- 描述:稳定性样品管理菜单下的按钮权限配置
-- 创建时间:2026-03-17
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060005000001, '标签补打', '130060005000001', 130060005, 0, 0, 0, NULL, 130060005010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 稳定性样品取样
-- 描述:稳定性样品取样菜单下的按钮权限配置
-- 创建时间:2026-03-17
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060006000001, '取样', '130060006000001', 130060006, 0, 0, 0, NULL, 130060006010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060006000002, '标签取样', '130060006000002', 130060006, 0, 0, 0, NULL, 130060006020, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 稳定性结果审核
-- 描述:稳定性结果审核菜单下的按钮权限配置
-- 创建时间:2026-03-17
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060007000001, '审核处理', '130060007000001', 130060007, 0, 0, 0, NULL, 130060007010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060007000002, '审核进度', '130060007000002', 130060007, 0, 0, 0, NULL, 130060007020, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 稳定性统计查询
-- 描述:稳定性统计查询菜单下的按钮权限配置
-- 创建时间:2026-03-17
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060008000001, '导出', '130060008000001', 130060008, 0, 0, 0, NULL, 130060008010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 稳定性趋势查询
-- 描述:稳定性考察模块最后一项子菜单下的按钮权限
-- 创建时间:2026-03-25
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130060009000001, '导出', '130060009000001', 130060009, 0, 0, 0, NULL, 130060009010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 删除菜单 - 实验包管理
-- 描述:硬删除实验包管理菜单及其所有子菜单、按钮权限
-- 创建时间:2026-03-17
-- =============================================

-- 删除实验包管理详情页子路由菜单（130010004001）
DELETE FROM bmos_platform.bp_menu WHERE id = 130010004001;

-- 删除实验包管理按钮权限（新增、编辑、删除）
DELETE FROM bmos_platform.bp_menu WHERE id IN (130010004000001, 130010004000002, 130010004000003);

-- 删除实验包管理菜单
DELETE FROM bmos_platform.bp_menu WHERE id = 130010004;
REPLACE INTO bmos_scheduler.xxl_job_group (id, app_name, title, address_type, address_list, update_time) VALUES (5, 'bmos-lims2-service', '实验室信息管理系统', 0, '', '2026-04-09 11:32:08');

REPLACE INTO bmos_scheduler.xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES (12, 5, '稳定性考察计划定时任务', '2026-04-09 11:36:28', '2026-04-09 11:36:52', 'admin', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'stabilityTriggerDueTimepointTasks', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2026-04-09 11:36:28', '', 1, 0, 1775754000000);


