-- 编号规则 数据修复
DROP PROCEDURE IF EXISTS check_and_execute;
DELIMITER $$

CREATE PROCEDURE check_and_execute()
BEGIN

    DECLARE batch_code_version_count INT;
    DECLARE lot_code_version_count INT;
SELECT COUNT(1) INTO batch_code_version_count FROM bp_code_rule_version WHERE rule_code = 'batch.record.archive.serial';
SELECT COUNT(1) INTO lot_code_version_count FROM bp_code_rule_version WHERE rule_code = 'lot.release.serial';

IF batch_code_version_count IS NOT NULL THEN
DELETE FROM bp_code_rule_version_detail WHERE code_rule_version_id in (SELECT id FROM bp_code_rule_version WHERE rule_code = 'batch.record.archive.serial');
DELETE FROM bp_code_rule_version WHERE rule_code = 'batch.record.archive.serial';
DELETE FROM bp_code_rule WHERE code = 'batch.record.archive.serial';
END IF;

IF lot_code_version_count IS NOT NULL THEN
DELETE FROM bp_code_rule_version_detail WHERE code_rule_version_id in (SELECT id FROM bp_code_rule_version WHERE rule_code = 'lot.release.serial');
DELETE FROM bp_code_rule_version WHERE rule_code = 'lot.release.serial';
DELETE FROM bp_code_rule WHERE code = 'lot.release.serial';
END IF;

END$$
DELIMITER ;
-- 调用存储过程
CALL check_and_execute();
DROP PROCEDURE IF EXISTS check_and_execute;

-- 插入批记录数据
INSERT INTO bmos_platform.bp_code_rule (id, code, name, can_update, create_by, update_by, create_time, update_time, is_deleted) VALUES (1834484254428499968, 'batch.record.archive.serial', '批记录编号规则', 'TRUE', '1834413608589283328', '1834413608589283328', '2024-09-13 14:48:24', '2024-09-13 14:48:24', 0);
INSERT INTO bmos_platform.bp_code_rule_version (id, rule_code, version, dict_id, description, version_status, status, reset_rule, create_by, update_by, create_time, update_time, is_deleted, del_version_flag) VALUES (1834484254499803136, 'batch.record.archive.serial', 'V1', 100020001004, '批记录编号规则', 'CONFIRM', 1, '[1]', '1834413608589283328', '1834413608589283328', '2024-09-13 14:48:24', '2024-09-13 14:49:25', 0, '0');
INSERT INTO bmos_platform.bp_code_rule_version_detail (id, code_rule_version_id, type, value, parameter_id, date_type, date_format, start_no, max_length, step, fill_zero, sort, create_by, update_by, create_time, update_time, is_deleted, is_show) VALUES (1834484254529163264, 1834484254499803136, 'PARAMETER', null, '100020001004001', null, null, null, null, null, null, 1, '1834413608589283328', '1834413608589283328', '2024-09-13 14:48:24', '2024-09-13 14:48:24', 0, 1);
INSERT INTO bmos_platform.bp_code_rule_version_detail (id, code_rule_version_id, type, value, parameter_id, date_type, date_format, start_no, max_length, step, fill_zero, sort, create_by, update_by, create_time, update_time, is_deleted, is_show) VALUES (1834484254529163265, 1834484254499803136, 'CONSTANT', '-', null, null, null, null, null, null, null, 2, '1834413608589283328', '1834413608589283328', '2024-09-13 14:48:24', '2024-09-13 14:48:24', 0, null);
INSERT INTO bmos_platform.bp_code_rule_version_detail (id, code_rule_version_id, type, value, parameter_id, date_type, date_format, start_no, max_length, step, fill_zero, sort, create_by, update_by, create_time, update_time, is_deleted, is_show) VALUES (1834484254533357568, 1834484254499803136, 'SEQUENCE', null, null, null, null, 1, 2, 1, 'TRUE', 3, '1834413608589283328', '1834413608589283328', '2024-09-13 14:48:24', '2024-09-13 14:48:24', 0, null);

-- 插入批签发数据
INSERT INTO bmos_platform.bp_code_rule (id, code, name, can_update, create_by, update_by, create_time, update_time, is_deleted) VALUES (1834486799750270976, 'lot.release.serial', '批签发编号规则', 'TRUE', '1834413608589283328', '1834413608589283328', '2024-09-13 14:58:31', '2024-09-13 14:58:31', 0);
INSERT INTO bmos_platform.bp_code_rule_version (id, rule_code, version, dict_id, description, version_status, status, reset_rule, create_by, update_by, create_time, update_time, is_deleted, del_version_flag) VALUES (1834486799762853888, 'lot.release.serial', 'V1', 100020001004, '', 'CONFIRM', 1, '[1]', '1834413608589283328', '1834414163361484800', '2024-09-13 14:58:31', '2024-09-13 15:00:35', 0, '0');
INSERT INTO bmos_platform.bp_code_rule_version_detail (id, code_rule_version_id, type, value, parameter_id, date_type, date_format, start_no, max_length, step, fill_zero, sort, create_by, update_by, create_time, update_time, is_deleted, is_show) VALUES (1834486799771242496, 1834486799762853888, 'PARAMETER', null, '100020001004001', null, null, null, null, null, null, 1, '1834413608589283328', '1834413608589283328', '2024-09-13 14:58:31', '2024-09-13 14:58:31', 0, 1);
INSERT INTO bmos_platform.bp_code_rule_version_detail (id, code_rule_version_id, type, value, parameter_id, date_type, date_format, start_no, max_length, step, fill_zero, sort, create_by, update_by, create_time, update_time, is_deleted, is_show) VALUES (1834486799775436800, 1834486799762853888, 'CONSTANT', '-', null, null, null, null, null, null, null, 2, '1834413608589283328', '1834413608589283328', '2024-09-13 14:58:31', '2024-09-13 14:58:31', 0, null);
INSERT INTO bmos_platform.bp_code_rule_version_detail (id, code_rule_version_id, type, value, parameter_id, date_type, date_format, start_no, max_length, step, fill_zero, sort, create_by, update_by, create_time, update_time, is_deleted, is_show) VALUES (1834486799779631104, 1834486799762853888, 'SEQUENCE', null, null, null, null, 1, 2, 1, 'TRUE', 3, '1834413608589283328', '1834413608589283328', '2024-09-13 14:58:31', '2024-09-13 14:58:31', 0, null);
