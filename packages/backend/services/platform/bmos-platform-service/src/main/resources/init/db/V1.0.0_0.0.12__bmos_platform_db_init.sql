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
DELETE from bp_dict_detail where id = 100020001001007;
DELETE from bp_dict_detail where id = 100020001001008;
insert into bp_dict_detail (id, dict_label, dict_value, dict_id, create_time, update_time, create_by, update_by)
values (100020001002007, '生产阶段代码', 'productionStageCode', 100020001001, '2024-06-24T16:40:38.308',
        '2024-06-24T16:40:38.308', '1802955436731506688', '1802955436731506688');
insert into bp_dict_detail (id, dict_label, dict_value, dict_id, create_time, update_time, create_by, update_by)
values (100020001002008, '生产阶段代码', 'productionStageCode', 100020001002, '2024-06-24T16:40:38.308',
        '2024-06-24T16:40:38.308', '1802955436731506688', '1802955436731506688');

-- 产线
DELETE from bp_dict_detail where id = 100020001001009;
DELETE from bp_dict_detail where id = 100020001001010;
insert into bp_dict_detail (id, dict_label, dict_value, dict_id, create_time, update_time, create_by, update_by)
values (100020001002009, '产线编码', 'productionLineCode', 100020001001, '2024-06-24T16:40:38.308',
        '2024-06-24T16:40:38.308', '1802955436731506688', '1802955436731506688');
insert into bp_dict_detail (id, dict_label, dict_value, dict_id, create_time, update_time, create_by, update_by)
values (100020001002010, '产线编码', 'productionLineCode', 100020001002, '2024-06-24T16:40:38.308',
        '2024-06-24T16:40:38.308', '1802955436731506688', '1802955436731506688');

-- 产品标识
DELETE from bp_dict_detail where id = 100020001001011;
DELETE from bp_dict_detail where id = 100020001001012;
insert into bp_dict_detail (id, dict_label, dict_value, dict_id, create_time, update_time, create_by, update_by)
values (100020001002011, '产品标识', 'productMark', 100020001001, '2024-06-24T16:40:38.308',
        '2024-06-24T16:40:38.308', '1802955436731506688', '1802955436731506688');
insert into bp_dict_detail (id, dict_label, dict_value, dict_id, create_time, update_time, create_by, update_by)
values (100020001002012, '产品标识', 'productMark', 100020001002, '2024-06-24T16:40:38.308',
        '2024-06-24T16:40:38.308', '1802955436731506688', '1802955436731506688');