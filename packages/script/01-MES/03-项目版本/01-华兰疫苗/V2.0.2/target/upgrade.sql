# 物料、暂存间、日志菜单 by lilong 240204
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by,update_by, is_deleted) VALUES (120010001, '原辅包信息', 120010001, 120010, 0, 1, 130110110, now(), now(), 1, 1, 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by,update_by, is_deleted) VALUES (120010002, '中间品信息', 120010002, 120010, 0, 1, 130110120, now(), now(), 1, 1, 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by,update_by, is_deleted) VALUES (120020010, '暂存间配置', 120020010, 120020, 0, 1, 130120121, now(), now(), 1, 1, 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by,update_by, is_deleted) VALUES (120030008, '暂存间管理', 120030008, 120030, 0, 1, 130130180, now(), now(), 1, 1, 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by,update_by, is_deleted) VALUES (120050003, '物料日志', 120050003, 120050, 0, 1, 130150130, now(), now(), 1, 1, 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by,update_by, is_deleted) VALUES (120050004, '货位日志', 120050004, 120050, 0, 1, 130150140, now(), now(), 1, 1, 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010001000001, '默认权限', '120010001000001', 120010001, 0, 0, 130110110110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010002000001, '默认权限', '120010002000001', 120010002, 0, 0, 130110120110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020010000001, '默认权限', '120020010000001', 120020010, 0, 0, 130120121110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030008000001, '默认权限', '120030006000001', 120030008, 0, 0, 130130180110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120050003000001, '默认权限', '120050003000001', 120050003, 0, 0, 130150130110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120050004000001, '默认权限', '120050004000001', 120050004, 0, 0, 130150140110, now(), now(), '1', '1', 0);

# 删除华兰疫苗用不到的菜单 by lilong 240304
DELETE FROM bmos_platform.bp_menu WHERE bp_menu.parent_id NOT IN (
'100030001',
'100030002',
'100030003',
'100030004',
'100030005',
'100040001',
'100040002',
'120010001',
'120010002',
'120020010',
'120030008',
'120050003',
'120050004',
'111010001',
'111010002',
'111020003'               )and is_menu = 0;


DELETE FROM bmos_platform.bp_menu WHERE id NOT IN (
'100',
'111',
'120',
'100030',
'100030001',
'100030002',
'100030003',
'100030004',
'100030005',
'100040',
'100040001',
'100040002',
'120010',
'120020',
'120030',
'120050',
'120010001',
'120010002',
'120020010',
'120030008',
'120050003',
'120050004',
'111010',
'111020',
'111010001',
'111010002',
'111020003'               )and is_menu = 1;

