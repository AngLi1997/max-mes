delete from bp_code_rule_version where id = '1767785736607764480';

INSERT INTO `bp_code_rule`
VALUES (1784131477303332864, 'mes.storage.material.serial', 'MES物料件编号', 'TRUE', '1760852189758922752',
        '1760852189758922752', now(), now(), 0);
INSERT INTO `bp_code_rule`
VALUES (1783783943292719104, 'wms.inventory.serial', 'WMS物料件编号', 'TRUE', '1760852189758922752',
        '1760852189758922752', now(), now(), 0);
INSERT INTO `bp_code_rule_version`
VALUES (1784131477345275904, 'mes.storage.material.serial', 'v1.0', null, 'v1', 'CONFIRM', 1, '[]',
        '1760852189758922752', '1760852189758922752', now(), now(), 0, '0');
INSERT INTO `bp_code_rule_version`
VALUES (1783783943364022272, 'wms.inventory.serial', 'v1.0', null, 'v1', 'CONFIRM', 1, '[]',
        '1760852189758922752', '1760852189758922752', now(), now(), 0, '0');
INSERT INTO bp_code_rule_version_detail (id, code_rule_version_id, type, value, parameter_id, date_type, date_format, start_no, max_length, step, fill_zero, sort, create_by, update_by, create_time, update_time, is_deleted)
VALUES (1784131477366247424, 1784131477345275904, 'SEQUENCE', null, null, null, null, 1, 10, 1, 'FALSE', 1, '1760852189758922752', '1760852189758922752', now(), now(), 0);
INSERT INTO bp_code_rule_version_detail (id, code_rule_version_id, type, value, parameter_id, date_type, date_format, start_no, max_length, step, fill_zero, sort, create_by, update_by, create_time, update_time, is_deleted)
VALUES (1783783943481462784, 1783783943364022272, 'SEQUENCE', null, null, null, null, 1, 10, 1, 'FALSE', 1, '1760852189758922752', '1760852189758922752', now(), now(), 0);