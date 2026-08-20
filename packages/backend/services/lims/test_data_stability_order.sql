-- 稳定性检验单测试数据构造脚本
-- 检验单ID: 2172963458780368898
-- 参考常规检验单: 2019321859883012096

-- 第1部分: 样品数据
INSERT INTO lm_sample (id, sample_no, inspection_order_id, inspect_item_id, received, parent_sample_id, storage_location, destroyed, create_time, update_time, create_by, update_by, is_deleted)
VALUES
(2172963458780368900, 'STAB-S001', 2172963458780368898, 1980539917930991616, 1, NULL, 'A-01-01', 0, NOW(), NOW(), 'system', 'system', 0),
(2172963458780368901, 'STAB-S002', 2172963458780368898, 1980539917930991616, 1, NULL, 'A-01-02', 0, NOW(), NOW(), 'system', 'system', 0);

-- 第2部分: 任务数据
INSERT INTO lm_task (id, scheme_version_id, inspection_order_id, inspect_item_id, inspect_item_code, item_config_id, parameter_id, parameter_code, parameter_config_id, is_executable, is_reportable, execute_method, record_id, record_version_id, record_item_id, status, owner_id, owner_name, create_time, update_time, create_by, update_by, is_deleted)
VALUES
(2172963458780368910, 2036334004566888448, 2172963458780368898, 1980539917930991616, 'd', 2036334004646580224, 1981185815677636608, 'ph', 2036334004818546688, 1, 1, 'ELN', NULL, NULL, NULL, 'SAMPLE_AUDIT_PENDING', '1905452244012847104', '邓轲', NOW(), NOW(), 'system', 'system', 0),
(2172963458780368911, 2036334004566888448, 2172963458780368898, 1980539917930991616, 'd', 2036334004646580224, 1980935278289883136, 'appearance', 2036334004894044160, 1, 1, 'ELN', NULL, NULL, NULL, 'SAMPLE_AUDIT_PENDING', '1905452244012847104', '邓轲', NOW(), NOW(), 'system', 'system', 0),
(2172963458780368912, 2036334004566888448, 2172963458780368898, 1980539917930991616, 'd', 2036334004998901760, 1981185815677636608, 'ph', 2036334005095370752, 1, 1, 'ELN', NULL, NULL, NULL, 'SAMPLE_AUDIT_PENDING', '1905452244012847104', '邓轲', NOW(), NOW(), 'system', 'system', 0);

-- 第3部分: 录入记录数据
INSERT INTO lm_inspection_entry_record (id, inspection_order_id, inspection_order_no, task_id, scheme_id, scheme_version_id, item_config_id, parameter_config_id, data_point_config_id, inspect_item_id, inspect_item_code, parameter_id, parameter_code, data_point_id, data_point_name, point_type, value_text, value_number, operator_id, operator_name, is_abnormal, create_time, update_time, create_by, update_by, is_deleted)
VALUES
(2172963458780368920, 2172963458780368898, 'STAB-TEST-002', 2172963458780368910, 2036334004566888448, 2036334004566888448, 2036334004646580224, 2036334004818546688, 2036400000000000001, 1980539917930991616, 'd', 1981185815677636608, 'ph', 1981185815681830912, '0.1%维A酸乳膏', 'TEXT', '合格', NULL, '1905452244012847104', '邓轲', 0, NOW(), NOW(), 'system', 'system', 0),
(2172963458780368921, 2172963458780368898, 'STAB-TEST-002', 2172963458780368910, 2036334004566888448, 2036334004566888448, 2036334004646580224, 2036334004818546688, 2036400000000000002, 1980539917930991616, 'd', 1981185815677636608, 'ph', 1982692087677718528, '0.1%维A酸乳膏', 'TEXT', '正常', NULL, '1905452244012847104', '邓轲', 0, NOW(), NOW(), 'system', 'system', 0);

