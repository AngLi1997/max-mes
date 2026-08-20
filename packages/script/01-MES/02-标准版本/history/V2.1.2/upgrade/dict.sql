# 新增设备自定义字段的内置字典 by lilong 240812
INSERT INTO bmos_platform.bp_dict (id, dict_name, dict_code, state, create_by, update_by, create_time, update_time, is_deleted, del_flag) VALUES (160010002001, '设备信息自定义字段', 'DeviceInformationFields', 1, '1', '', now(), now(), 0, 0);
INSERT INTO bmos_platform.bp_dict (id, dict_name, dict_code, state, create_by, update_by, create_time, update_time, is_deleted, del_flag) VALUES (160010002002, '设备数据自定义字段', 'DeviceDataFields', 1, '1', '', now(), now(), 0, 0);

# 新增设备信息内置字典数据 by yuxiaorong 240816
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (160010002001001, '称量单位', 'WEIGHING_UNIT_001', 160010002001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (160010002001002, '称量精度', 'WEIGHING_ACCURACY_002', 160010002001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (160010002001003, '称量范围', 'WEIGHING_RANGE_003', 160010002001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (160010002001004, '容器皮重', 'CONTAINER_WEIGHT_004', 160010002001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (160010002001005, '内容物体积', 'CONTENT_VOLUME_005', 160010002001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (160010002001006, '内容物重量', 'CONTENT_WEIGHT_006', 160010002001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (160010002001007, 'IP地址', 'IP_ADDRESS_007', 160010002001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (160010002001008, '端口', 'PORT_008', 160010002001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (160010002001009, '资产编码', 'ASSET_CODE_009', 160010002001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (160010002001010, 'PAD地址', 'PAD_ADDRESS_010', 160010002001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (160010002001011, '打印机dpi', 'PRINTER_DPI_011', 160010002001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);

# 新增异常管理的内置字段和数据 by lilong 240822
INSERT INTO bmos_platform.bp_dict (id, dict_name, dict_code, state, create_by, update_by, create_time, update_time, is_deleted, del_flag) VALUES (120090001001, '异常类型', 'ExceptionType', 1, '1', '', now(), now(), 0, 0);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted, del_flag) VALUES (120090001001001, '自动记录', 'AutomaticRecording', 120090001001, '', '', now(), now(), 0, 0);
# 新增生产修订异常内置数据 by lilong 240903
UPDATE bmos_platform.bp_dict_detail SET dict_label = '超限异常', dict_value = 'OverLimitException' WHERE id = 120090001001001;
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted, del_flag) VALUES (120090001001002, '生产修订异常', 'ProductReviseException', 120090001001, '', '', now(), now(), 0, 0);

# 批记录/批签发编号规则内置字典
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted, del_flag) VALUES (100020001003003, '批记录/批签发编号规则', 'RecordNumberingRules', 1730513339114741760, '', '', now(), now(), 0, 0);
INSERT INTO bmos_platform.bp_dict (id, dict_name, dict_code, state, create_by, update_by, create_time, update_time, is_deleted, del_flag) VALUES (100020001004, '批记录/批签发编号规则', 'RecordNumberingRules', 1, '1', '', now(), now(), 0, 0);
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted, del_flag) VALUES (100020001004001, '生产批号', 'batchNo', 100020001004, '', '', now(), now(), 0, 0);

# 新增称量协议内置字典数据 by lilong 240919
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted, del_flag) VALUES (160010002001012, '称量协议类型', 'WEIGHING_PROTOCOL_TYPE_012', 160010002001, '', '', now(), now(), 0, 0);
# 新增皮重单位内置字典数据 by lilong 241008
INSERT INTO bmos_platform.bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted, del_flag) VALUES (160010002001013, '皮重单位', 'CONTAINER_WEIGHT_UNIT_013', 160010002001, '', '', now(), now(), 0, 0);
DELETE FROM bmos_platform.bp_dict_detail where bmos_platform.bp_dict_detail.id in (160010002001005,160010002001006);
