INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (999, '康盛科泰大屏', '999', 0, 0, 1, 1, '/app/bmos-platform/kskt/index.html', 190, now(), now(), '1', '1', 0, 'BM-DB');
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (999010, '康盛科泰大屏', '999010', 999, 0, 1, 0, null, 190110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (999010001, '康盛科泰大屏', '999010001', 999010, 0, 1, 0, null, 190110110, now(), now(), '1', '1', 0, null);
UPDATE bmos_platform.bp_menu SET is_outside = 2 WHERE id = 999;

DELETE FROM bmos_platform.bp_menu WHERE bmos_platform.bp_menu.id in (
'999',
'999010',
'999010001'
);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (900, '康盛科泰大屏', '900', 0, 0, 1, 2, '/app/bmos-platform/kskt/index.html', 190, now(), now(), '1', '1', 0, 'BM-DB');
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (900010, '康盛科泰大屏', '900010', 900, 0, 1, 0, null, 190110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (900010001, '康盛科泰大屏', '900010001', 900010, 0, 1, 0, null, 190110110, now(), now(), '1', '1', 0, null);
