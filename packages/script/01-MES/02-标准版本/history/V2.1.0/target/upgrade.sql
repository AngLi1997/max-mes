# 增加生产计划类型标识参数 by lilong 240522
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120006, 'mes.ProductionPlanType', '{"PRODUCT":"A","EXPERIMENT":"B","VERIFY":"C"}', 'JSON', 'BUSINESS', '生产', '生产计划类型标识:PRODUCT-生产批次、EXPERIMENT-实验批次、VERIFY-验证批次', 120060, '', 1, '1', '1', now(),now(), 0);

# 增加批记录组件默认字号的参数 by lilong 240527
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120007, 'mes.record.font.size', '16', 'NUMBER', 'BUSINESS', '生产', '批记录组件默认字号,字号范围[5,72]', 120070, '', 1, '1', '1', now(), now(), 0);

# 增加批记录组件默认字体的参数 by lilong 240527
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120008, 'mes.record.default.font', 'SimSun', 'STRING', 'BUSINESS', '生产', '批记录组件默认字体，宋体：SimSun，黑体：SimHei，微软雅黑：YaHei，仿宋：FangSong', 120080, '', 1, '1', '1', now(), now(), 0);

# 增加称量需求提前规划时间的参数 by lilong 240612
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120009, 'mes.weigh.require.advance', '15', 'NUMBER', 'BUSINESS', '生产', '称量需求提前规划时间，单位：日，范围[0,999]', 120090, '', 1, '1', '1', now(), now(), 0);

# 物料临期提醒默认时间的参数 by lilong 240620
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120010, 'mes.material.dying.period', '15', 'NUMBER', 'BUSINESS', '生产', '物料临期提醒默认时间，单位：日，范围[0,999]', 120100, '', 1, '1', '1', now(), now(), 0);

# 记录作业日期组件时间格式 by lilong 240625
UPDATE bmos_platform.bp_business_parameter SET value = 'yyyy-MM-dd HH:mm:ss' WHERE id = 100001;

# 生产计划命名改为生产指令单 by lilong 240710
UPDATE bmos_platform.bp_business_parameter SET description = '生产指令单作废标记' WHERE id = 120004;
UPDATE bmos_platform.bp_business_parameter SET description = '生产指令单类型标识:PRODUCT-生产批次、EXPERIMENT-实验批次、VERIFY-验证批次' WHERE id = 120006;

# 密码复杂度参数配置描述变更 by lilong 240719
UPDATE bmos_platform.bp_business_parameter SET description = '密码复杂度校验配置：lowerCase：是否必须有小写字母；upperCase：是否必须有大写字母；digit：是否必须有数字；specialCharacters：是否必须有specialCharacters配置的字符' WHERE id = 100011;
use bmos_platform;
-- 计划类型字典
DELETE from bp_dict where id = 100020001001;
INSERT INTO `bp_dict`(`id`, `dict_name`, `dict_code`, `state`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (100020001001, '生产批号', 'ProductionBatchNoParameter', 1, '1', '', '2023-12-25 10:43:15', '2023-12-25 10:43:15', 0);
DELETE from bp_dict where id = 100020001002;
INSERT INTO `bp_dict`(`id`, `dict_name`, `dict_code`, `state`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (100020001002, '生产计划编号', 'ProductionPlanNoParameter', 1, '1', '', '2023-12-25 10:43:16', '2023-12-25 10:43:16', 0);
DELETE from bp_dict where id = 120020009002;
INSERT INTO `bp_dict`(`id`, `dict_name`, `dict_code`, `state`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (120020009002, '生产计划编号规则', 'ProductionPlanNumberingRules', 1, '1', '', '2023-12-21 13:58:23', '2023-12-21 13:58:23', 0);
DELETE from bp_dict where id = 1729066680262463488;
INSERT INTO `bp_dict`(`id`, `dict_name`, `dict_code`, `state`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (1729066680262463488, '生产批号编号规则', 'ProductionBatchNumberingRules', 1, '1', '', '2023-11-21 15:24:48', '2023-11-21 17:08:59', 0);
DELETE from bp_dict where id = 1730513339114741760;
INSERT INTO `bp_dict`(`id`, `dict_name`, `dict_code`, `state`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (1730513339114741760, '编号规则', 'NumberingRules', 1, '1', '', '2023-11-21 15:24:48', '2023-11-21 17:08:59', 0);
DELETE from bp_dict_detail where id = 100020001001001;
INSERT INTO bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (100020001001001, '产品名称', 'productName', 100020001001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
DELETE from bp_dict_detail where id = 100020001001002;
INSERT INTO bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (100020001001002, '产品编码', 'productMergeCode', 100020001001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
DELETE from bp_dict_detail where id = 100020001001003;
INSERT INTO bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (100020001001003, '内包规格', 'innerPackingSpecification', 100020001001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
DELETE from bp_dict_detail where id = 100020001001004;
INSERT INTO bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (100020001001004, '包装规格', 'packingSpecification', 100020001001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
DELETE FROM bp_dict_detail where id = 100020001001005;
INSERT INTO bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (100020001001005, '产品标识', 'productFlag', 100020001001, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
DELETE from bp_dict_detail where id = 100020001002001;
INSERT INTO bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (100020001002001, '产品名称', 'productName', 100020001002, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
DELETE from bp_dict_detail where id = 100020001002002;
INSERT INTO bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (100020001002002, '产品编码', 'productMergeCode', 100020001002, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
DELETE from bp_dict_detail where id = 100020001002003;
INSERT INTO bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (100020001002003, '内包规格', 'innerPackingSpecification', 100020001002, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
DELETE from bp_dict_detail where id = 100020001002004;
INSERT INTO bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (100020001002004, '包装规格', 'packingSpecification', 100020001002, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
DELETE from bp_dict_detail where id = 100020001003001;
INSERT INTO bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (100020001003001, '生产批号', 'ProductionBatchNoParameter', 1730513339114741760, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
DELETE from bp_dict_detail where id = 100020001003002;
INSERT INTO bp_dict_detail (id, dict_label, dict_value, dict_id, create_by, update_by, create_time, update_time, is_deleted) VALUES (100020001003002, '生产计划编号', 'ProductionPlanNoParameter', 1730513339114741760, '', '', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), 0);
DELETE from bp_dict_detail where id = 100020001002005;
insert into bp_dict_detail (id, dict_label, dict_value, dict_id, create_time, update_time, create_by, update_by)
values (100020001002005, '计划类型', 'productPlanType', 100020001002, '2024-06-24T16:40:38.308',
        '2024-06-24T16:40:38.308', '1802955436731506688', '1802955436731506688');
DELETE from bp_dict_detail where id = 100020001001006;
insert into bp_dict_detail (id, dict_label, dict_value, dict_id, create_time, update_time, create_by, update_by)
values (100020001001006, '计划类型', 'productPlanType', 100020001001, '2024-06-24T16:42:32.742',
        '2024-06-24T16:42:32.742', '1802955436731506688', '1802955436731506688');

-- 生产阶段编码
DELETE from bp_dict_detail where id = 100020001002007;
DELETE from bp_dict_detail where id = 100020001002008;
insert into bp_dict_detail (id, dict_label, dict_value, dict_id, create_time, update_time, create_by, update_by)
values (100020001002007, '生产阶段代码', 'productionStageCode', 100020001001, '2024-06-24T16:40:38.308',
        '2024-06-24T16:40:38.308', '1802955436731506688', '1802955436731506688');
insert into bp_dict_detail (id, dict_label, dict_value, dict_id, create_time, update_time, create_by, update_by)
values (100020001002008, '生产阶段代码', 'productionStageCode', 100020001002, '2024-06-24T16:40:38.308',
        '2024-06-24T16:40:38.308', '1802955436731506688', '1802955436731506688');

-- 产线
DELETE from bp_dict_detail where id = 100020001002009;
DELETE from bp_dict_detail where id = 100020001002010;
insert into bp_dict_detail (id, dict_label, dict_value, dict_id, create_time, update_time, create_by, update_by)
values (100020001002009, '产线编码', 'productionLineCode', 100020001001, '2024-06-24T16:40:38.308',
        '2024-06-24T16:40:38.308', '1802955436731506688', '1802955436731506688');
insert into bp_dict_detail (id, dict_label, dict_value, dict_id, create_time, update_time, create_by, update_by)
values (100020001002010, '产线编码', 'productionLineCode', 100020001002, '2024-06-24T16:40:38.308',
        '2024-06-24T16:40:38.308', '1802955436731506688', '1802955436731506688');

-- 产品标识
DELETE from bp_dict_detail where id = 100020001002011;
DELETE from bp_dict_detail where id = 100020001002012;
insert into bp_dict_detail (id, dict_label, dict_value, dict_id, create_time, update_time, create_by, update_by)
values (100020001002011, '产品标识', 'productMark', 100020001001, '2024-06-24T16:40:38.308',
        '2024-06-24T16:40:38.308', '1802955436731506688', '1802955436731506688');
insert into bp_dict_detail (id, dict_label, dict_value, dict_id, create_time, update_time, create_by, update_by)
values (100020001002012, '产品标识', 'productMark', 100020001002, '2024-06-24T16:40:38.308',
        '2024-06-24T16:40:38.308', '1802955436731506688', '1802955436731506688');

UPDATE bmos_platform.bp_dict SET dict_name = '生产指令单' WHERE id = 100020001002;
UPDATE bmos_platform.bp_dict SET dict_name = '生产指令单编号规则' WHERE id = 120020009002;

DELETE FROM bp_dict_detail WHERE id = 100020001001005;

# 更新计划为指令单 by lilong 240709
UPDATE bmos_platform.bp_dict_detail SET dict_label = '指令单类型' WHERE id = 100020001001006;
UPDATE bmos_platform.bp_dict_detail SET dict_label = '指令单类型' WHERE id = 100020001002005;
UPDATE bmos_platform.bp_dict_detail SET dict_label = '生产指令单编号' WHERE id = 100020001003002;
# 生产操作规程启用审核流程 by lilong 240628
INSERT INTO bmos_mes.bm_flow_audit_category (id, name, code, parent_id, create_by, update_by, create_time, update_time, is_deleted, tree_code, tree_name) VALUES (120020004, '操作规程启用审核', '120020004', 120020, '1', '1', now(), now(), 0, '120020004', '生产配置/操作规程启用审核');
# 更新配方为生产BOM by lilong 240628
UPDATE bmos_mes.bm_flow_audit_category SET name = '生产BOM审批', tree_name = '生产配置/生产BOM审批' WHERE id = 120020003;

# 更新生产指令单和操作规程的流程命名 by lilong 240704
UPDATE bmos_mes.bm_flow_audit_category SET name = '生产指令单', tree_name = '生产指令单' WHERE id = 120030;
UPDATE bmos_mes.bm_flow_audit_category SET name = '指令单审批', tree_name = '生产指令单/指令单审批' WHERE id = 120030001;
UPDATE bmos_mes.bm_flow_audit_category SET name = '操作规程' WHERE id = 120020004;

# 更新操作规程审批命名 by lilong 240709
UPDATE bmos_mes.bm_flow_audit_category SET name = '操作规程审批' WHERE id = 120020004;
# 新增房间信息打印标签 by lilong 240523
INSERT INTO bmos_platform.bp_tag_scene (id, tag_scene_name, tag_scene_desc, tag_type_id, data_source_service_name, data_source_interface, qr_code_field, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (160005001, '房间信息打印标签', '房间信息打印标签', 5, 'bmos-platform-service', '/api/app/platform/factory/room/print', 'roomId', 5110, now(),  null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_type (id, tag_type_name, tag_type_desc, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (5, '房间标签', '贴在房间上的标签', 5, now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_instance (id, tag_name, tag_type_id, tag_scene_id, tag_define_id, config_fields, is_enable, create_time, update_time, create_by, update_by, is_deleted) VALUES (160005001, '房间信息打印标签', 5, 160005001, 1, '[{"label":"房间名称：","defineField":"field1","dataSourceField":"roomName","consumeValue":null},{"label":"房间编码：","defineField":"field2","dataSourceField":"roomCode","consumeValue":null},{"label":"清场时限：","defineField":"field3","dataSourceField":"timeLimit","consumeValue":null},{"label":null,"defineField":"field4","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field5","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field6","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field7","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field8","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field9","dataSourceField":null,"consumeValue":null}]', 'TRUE', now(), now(), null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (160005001001, 160005001, 'roomName', '房间名称', 'String', '纯化间', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (160005001002, 160005001, 'roomCode', '房间编码', 'String', 'WQF-201', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (160005001003, 160005001, 'timeLimit', '清场时限', 'String', '48小时', now(), null, null, null, 0);

# 更新中间品配料称量补打标签 by lilong 240710
UPDATE bmos_platform.bp_tag_scene SET tag_scene_name = '中间品配料称量补打标签', tag_scene_desc = '中间品配料称量补打标签' WHERE id = 121002002;
# 设备管理菜单更新 by zhangziyang 240521
DELETE FROM bmos_platform.bp_menu WHERE bmos_platform.bp_menu.id = 160010001;
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030, '区域管理', '160030', 160, 0, 1, 0, null, 180101, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030001, '产线管理', '160030001', 160030, 0, 1, 0, null, 160030110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030002, '房间管理', '160030002', 160030, 0, 1, 0, null, 160030120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030003, '工位管理', '160030003', 160030, 0, 1, 0, null, 160030130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160020003, '房间清场日志', '160020003', 160020, 0, 1, 0, null, 160020130, now(), now(), '1', '1', 0, null);

# 增加APP房间管理菜单 by zhangziyang 240521
UPDATE bmos_platform.bp_menu SET name = '资源管理' WHERE id = 121030;
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121030002, '房间管理', '121030002', 121030, 1, 1, 0, null, 140130110, now(), now(), '1', '1', 0, null);

# 操作规程菜单 by lilong 240606
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011, '操作规程', '120020011', 120020, 0, 1, 0, null, 130120111, now(), now(), '1', '1', 0, null);

# 库存管理按钮权限及签名权限 by lilong 240607
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000001, '物料出库按钮', '121020002000001', 121020002, 1, 0, 0, null, 121020002000110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000002, '物料盘点按钮', '121020002000002', 121020002, 1, 0, 0, null, 121020002000120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000003, '物料移库按钮', '121020002000003', 121020002, 1, 0, 0, null, 121020002000130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000004, '物料预定按钮', '121020002000004', 121020002, 1, 0, 0, null, 121020002000140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000005, '取消预定按钮', '121020002000005', 121020002, 1, 0, 0, null, 121020002000150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000006, '拆包出库按钮', '121020002000006', 121020002, 1, 0, 0, null, 121020002000160, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000007, '标签打印按钮', '121020002000007', 121020002, 1, 0, 0, null, 121020002000170, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000008, '物料出库-领用人签名', '121020002000008', 121020002, 1, 0, 0, null, 121020002000180, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000009, '物料退库-递交人签名', '121020002000009', 121020002, 1, 0, 0, null, 121020002000180, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000010, '物料盘点-复核人签名', '121020002000010', 121020002, 1, 0, 0, null, 121020002000180, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000011, '取消预定-复核人签名', '121020002000011', 121020002, 1, 0, 0, null, 121020002000180, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000012, '拆包出库-领用人签名', '121020002000012', 121020002, 1, 0, 0, null, 121020002000180, now(), now(), '1', '1', 0, null);

# 设备管理权限更新 by lilong 240607
# 删除原工厂建模菜单的按钮权限
DELETE FROM bmos_platform.bp_menu WHERE parent_id = 160010001;
# 产线管理的按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030001000001, '新增模型', '160030001000001', 160030001, 0, 0, 0, null, 160030001110, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030001000002, '编辑模型', '160030001000002', 160030001, 0, 0, 0, null, 160030001120, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030001000003, '删除模型', '160030001000003', 160030001, 0, 0, 0, null, 160030001130, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030001000004, '新增产线', '160030001000004', 160030001, 0, 0, 0, null, 160030001140, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030001000005, '编辑产线', '160030001000005', 160030001, 0, 0, 0, null, 160030001150, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030001000006, '查看产线', '160030001000006', 160030001, 0, 0, 0, null, 160030001160, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030001000007, '绑定房间', '160030001000007', 160030001, 0, 0, 0, null, 160030001170, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030001000008, '绑定工位', '160030001000008', 160030001, 0, 0, 0, null, 160030001180, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030001000009, '删除产线', '160030001000009', 160030001, 0, 0, 0, null, 160030001190, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030001000010, '启停', '160030001000010', 160030001, 0, 0, 0, null, 160030001200,now(),now(), '1', '1', 0, null);
# 房间管理的按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030002000001, '新增模型', '160030002000001', 160030002, 0, 0, 0, null, 160030002110, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030002000002, '编辑模型', '160030002000002', 160030002, 0, 0, 0, null, 160030002120, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030002000003, '删除模型', '160030002000003', 160030002, 0, 0, 0, null, 160030002130, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030002000004, '新增房间', '160030002000004', 160030002, 0, 0, 0, null, 160030002140, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030002000005, '编辑房间', '160030002000005', 160030002, 0, 0, 0, null, 160030002150, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030002000006, '查看房间', '160030002000006', 160030002, 0, 0, 0, null, 160030002160, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030002000007, '数据权限', '160030002000007', 160030002, 0, 0, 0, null, 160030002170, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030002000008, '绑定工位', '160030002000008', 160030002, 0, 0, 0, null, 160030002180, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030002000009, '删除房间', '160030002000009', 160030002, 0, 0, 0, null, 160030002190, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030002000010, '启停', '160030002000010', 160030002, 0, 0, 0, null, 160030002200,now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030002000011, '标签打印', '160030002000011', 160030002, 0, 0, 0, null, 160030002210,now(),now(), '1', '1', 0, null);
# 工位管理的按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030003000001, '新增模型', '160030003000001', 160030003, 0, 0, 0, null, 160030003110, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030003000002, '编辑模型', '160030003000002', 160030003, 0, 0, 0, null, 160030003120, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030003000003, '删除模型', '160030003000003', 160030003, 0, 0, 0, null, 160030003130, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030003000004, '新增工位', '160030003000004', 160030003, 0, 0, 0, null, 160030003140, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030003000005, '编辑工位', '160030003000005', 160030003, 0, 0, 0, null, 160030003150, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030003000006, '查看工位', '160030003000006', 160030003, 0, 0, 0, null, 160030003160, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030003000007, '绑定设备', '160030003000007', 160030003, 0, 0, 0, null, 160030003170, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030003000008, '绑定人员', '160030003000008', 160030003, 0, 0, 0, null, 160030003180, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030003000009, '删除工位', '160030003000009', 160030003, 0, 0, 0, null, 160030003190, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030003000010, '启停', '160030003000010', 160030003, 0, 0, 0, null, 160030003200,now(),now(), '1', '1', 0, null);
# 房间清场日志
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160020003000001, '导出', '160020003000001', 160020003, 0, 0, 0, null, 160020003110, now(),now(), '1', '1', 0, null);

# 操作规程的按钮权限 by lilong 240607
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000001, '新增分类', '160030001000001', 160030001, 0, 0, 0, null, 160030001110, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000002, '编辑分类', '160030001000002', 160030001, 0, 0, 0, null, 160030001120, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000003, '删除分类', '160030001000003', 160030001, 0, 0, 0, null, 160030001130, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000004, '新增文件', '160030001000004', 160030001, 0, 0, 0, null, 160030001140, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000005, '新增版本', '160030001000005', 160030001, 0, 0, 0, null, 160030001150, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000006, '编辑文件', '160030001000006', 160030001, 0, 0, 0, null, 160030001160, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000007, '查看文件', '160030001000007', 160030001, 0, 0, 0, null, 160030001170, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000008, '确认文件', '160030001000008', 160030001, 0, 0, 0, null, 160030001180, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000009, '启用', '160030001000009', 160030001, 0, 0, 0, null, 160030001190, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000010, '停用', '160030001000010', 160030001, 0, 0, 0, null, 160030001200, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000011, '历史', '120020011000011', 160030001, 0, 0, 0, null, 160030001210, now(),now(), '1', '1', 0, null);
# 生产执行的操作规程按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001009, '操作规程', '121010001001009', 121010001001, 1, 0, 0, null, 121010001001190, now(), now(), '1', '1', 0, null);

# 操作规程按钮权限更新
UPDATE bmos_platform.bp_menu SET code = '120020011000001', parent_id = 120020011, sort = 120020011110 WHERE id = 120020011000001;
UPDATE bmos_platform.bp_menu SET code = '120020011000002', parent_id = 120020011, sort = 120020011120 WHERE id = 120020011000002;
UPDATE bmos_platform.bp_menu SET code = '120020011000003', parent_id = 120020011, sort = 120020011130 WHERE id = 120020011000003;
UPDATE bmos_platform.bp_menu SET code = '120020011000004', parent_id = 120020011, sort = 120020011140 WHERE id = 120020011000004;
UPDATE bmos_platform.bp_menu SET code = '120020011000005', parent_id = 120020011, sort = 120020011150 WHERE id = 120020011000005;
UPDATE bmos_platform.bp_menu SET code = '120020011000006', parent_id = 120020011, sort = 120020011160 WHERE id = 120020011000006;
UPDATE bmos_platform.bp_menu SET code = '120020011000007', parent_id = 120020011, sort = 120020011170 WHERE id = 120020011000007;
UPDATE bmos_platform.bp_menu SET code = '120020011000008', parent_id = 120020011, sort = 120020011180 WHERE id = 120020011000008;
UPDATE bmos_platform.bp_menu SET code = '120020011000009', parent_id = 120020011, sort = 120020011190 WHERE id = 120020011000009;
UPDATE bmos_platform.bp_menu SET code = '120020011000010', parent_id = 120020011, sort = 120020011200 WHERE id = 120020011000010;
UPDATE bmos_platform.bp_menu SET code = '120020011000011', parent_id = 120020011, sort = 120020011210 WHERE id = 120020011000011;

# 称量中心菜单及权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020012, '称量中心', '120020012', 120020, 0, 1, 0, null, 130120200, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020012000001, '新增分类', '120020012000001', 120020012, 0, 0, 0, null, 120020012110, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020012000002, '编辑分类', '120020012000002', 120020012, 0, 0, 0, null, 120020012120, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020012000003, '删除分类', '120020012000003', 120020012, 0, 0, 0, null, 120020012130, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020012000004, '新增称量中心', '120020012000004', 120020012, 0, 0, 0, null, 120020012140, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020012000005, '编辑称量中心', '120020012000005', 120020012, 0, 0, 0, null, 120020012150, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020012000006, '查看称量中心', '120020012000006', 120020012, 0, 0, 0, null, 120020012160, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020012000007, '数据权限', '120020012000007', 120020012, 0, 0, 0, null, 120020012170, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020012000008, '绑定工位', '120020012000008', 120020012, 0, 0, 0, null, 120020012180, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020012000009, '启停', '120020012000009', 120020012, 0, 0, 0, null, 120020012190, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020012000010, '删除称量中心', '120020012000010', 120020012, 0, 0, 0, null, 120020012200, now(),now(), '1', '1', 0, null);
# 称量任务规划菜单及权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030010, '称量任务', '120030010', 120030, 0, 1, 0, null, 130120200, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030010000001, '任务规划', '120030010000001', 120030010, 0, 0, 0, null, 120030010110, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030010000002, '自动规划', '120030010000002', 120030010, 0, 0, 0, null, 120030010120, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030010000003, '编辑称量任务', '120030010000003', 120030010, 0, 0, 0, null, 120030010130, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030010000004, '查看称量任务', '120030010000004', 120030010, 0, 0, 0, null, 120030010140, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030010000005, '确认称量任务', '120030010000005', 120030010, 0, 0, 0, null, 120030010150, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030010000006, '下发称量任务', '120030010000006', 120030010, 0, 0, 0, null, 120030010160, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030010000007, '取消称量任务', '120030010000007', 120030010, 0, 0, 0, null, 120030010170, now(),now(), '1', '1', 0, null);
# APP称量执行菜单及权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020001, '称量执行', '121020001', 121020, 1, 1, 0, null, 140120130, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020001000001, '称量执行-物料称量模式', '121020001000001', 121020001, 1, 0, 0, null, 121020001000110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020001000002, '称量执行-手工称量模式', '121020001000002', 121020001, 1, 0, 0, null, 121020001000120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020001000003, '称量执行-复核人签名', '121020001000003', 121020001, 1, 0, 0, null, 121020001000130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020001000004, '称量执行-直接完成物料称量签名', '121020001000004', 121020001, 1, 0, 0, null, 121020001000140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020001000005, '称量执行-直接完成余料称量签名', '121020001000005', 121020001, 1, 0, 0, null, 121020001000150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020001000006, '称量执行-余料称量超目标量签名', '121020001000006', 121020001, 1, 0, 0, null, 121020001000160, now(), now(), '1', '1', 0, null);
# 称量历史菜单
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020003, '称量历史', '121020003', 121020, 1, 1, 0, null, 140120140, now(),now(), '1', '1', 0, null);
# 操作规程审批按钮
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020013, '操作规程审批', '120020013', 120020, 0, 1, 0, null, 130120112, now(), now(), '1', '1', 0, null);
# 操作规程立即生效按钮
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000012, '立即生效', '120020011000012', 120020011, 0, 0, 0, null, 120020011220, now(), now(), '1', '1', 0, null);
# 操作规程审批按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020013000001, '处理', '120020013000001', 120020013, 0, 0, 0, null, 120020013110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020013000002, '审批进度', '120020013000002', 120020013, 0, 0, 0, null, 120020013120, now(), now(), '1', '1', 0, null);
# 公式验证按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (100020006000009, '验证', '100020006000009', 100020006, 0, 0, 0, null, 110120160190, now(), now(), '1', '1', 0, null);
# 班组管理绑定产线按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030005000006, '产线', '120030005000006', 120030005, 0, 0, 0, null, 120030005160, now(), now(), '1', '1', 0, null);

# 屏蔽称量中心菜单和权限 by lilong 240627
DELETE FROM bmos_platform.bp_menu WHERE id IN (
'120020012',
'120020012000001',
'120020012000002',
'120020012000003',
'120020012000004',
'120020012000005',
'120020012000006',
'120020012000007',
'120020012000008',
'120020012000009',
'120020012000010',
'120030010',
'120030010000001',
'120030010000002',
'120030010000003',
'120030010000004',
'120030010000005',
'120030010000006',
'120030010000007',
'121020001',
'121020001000001',
'121020001000002',
'121020001000003',
'121020001000004',
'121020001000005',
'121020001000006',
'121020003'
    );

# 更新生产配方的菜单权限为生产BOM by lilong 240628
UPDATE bmos_platform.bp_menu SET name = '生产BOM配置' WHERE id = 120020004;
UPDATE bmos_platform.bp_menu SET name = '生产BOM审批' WHERE id = 120020005;
UPDATE bmos_platform.bp_menu SET name = '新增生产BOM' WHERE id = 120020004000001;

# 更新中间品产出权限名称 by lilong 240704
UPDATE bmos_platform.bp_menu SET name = '中间品产出-复核人签名' WHERE id = 121010001002011;
UPDATE bmos_platform.bp_menu SET name = '中间品产出-产出称量模式' WHERE id = 121010001002014;
UPDATE bmos_platform.bp_menu SET name = '中间品产出-手工称量模式' WHERE id = 121010001002015;

# 更新生产指令单的菜单命名 by lilong 240704
UPDATE bmos_platform.bp_menu SET name = '生产指令单' WHERE id = 120030001;
UPDATE bmos_platform.bp_menu SET name = '指令单审批' WHERE id = 120030002;
UPDATE bmos_platform.bp_menu SET name = '新建指令单' WHERE id = 120030001000001;

# 更新生产执行完成按钮的权限命名 by lilong 240704
UPDATE bmos_platform.bp_menu SET name = '完成' WHERE id = 121010001001005;

# 操作规程的审批进度按钮 by lilong 240712
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000013, '审批进度', '120020011000013', 120020011, 0, 0, 0, null, 120020011230, now(), now(), '1', '1', 0, null);

# 称量权限名称和按钮一致 by lilong 240719
UPDATE bmos_platform.bp_menu SET name = '配料称量-手动称量模式' WHERE id = 121010001002013;
UPDATE bmos_platform.bp_menu SET name = '中间品产出-手动称量模式' WHERE id = 121010001002015;

# 操作规程的数据权限按钮 by lilong 240723
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000014, '数据权限', '120020011000014', 120020011, 0, 0, 0, null, 120020011141, now(), now(), '1', '1', 0, null);
# 操作规程生效状态变更定时任务 by zhangruoyu 240603
INSERT INTO bmos_scheduler.xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES (5, 1, '操作规程状态修改', now(), now(), 'admin', '', 'CRON', '0 5 0 ? * *', 'DO_NOTHING', 'FIRST', 'updateOperateRuleVersion', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 1, 0, now());

# 设备占用心跳释放默认启用 by lilong 240710
UPDATE bmos_scheduler.xxl_job_info SET trigger_status = 1 WHERE id = 4;

# 更新操作规程状态修改cron表达式 by renjinguang 240712
UPDATE bmos_scheduler.xxl_job_info SET schedule_conf = '0 5 0 * * ?' WHERE id = 5;
