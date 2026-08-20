# 生产执行页面录入空值按钮权限 by lilong 240912
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001015, '录入空值', '121010001001015', 121010001001, 1, 0, 0, null, 121010001001250, now(), now(), '1', '1', 0, null);

# 新增皮重管理菜单及权限 by lilong 241008
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020015, '皮重管理', '120020015', 120020, 0, 1, 0, null, 130120210, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020015000001, '新增皮重', '120020015000001', 120020015, 0, 0, 0, null, 120020015110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020015000002, '编辑皮重', '120020015000002', 120020015, 0, 0, 0, null, 120020015120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020015000003, '查看皮重', '120020015000003', 120020015, 0, 0, 0, null, 120020015130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020015000004, '删除皮重', '120020015000004', 120020015, 0, 0, 0, null, 120020015140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020015000005, '标签打印', '120020015000005', 120020015, 0, 0, 0, null, 120020015150, now(), now(), '1', '1', 0, null);

# 中间品产出-扫码去皮模式 by lilong 241009
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002025, '中间品产出-扫码去皮模式', '121010001002025', 121010001002, 1, 0, 0, null, 121010001010120204, now(), now(), '1', '1', 0, null);

# 生产管理工序流程强制完成按钮 by lilong 241023
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001016, '强制完成', '121010001001016', 121010001001, 1, 0, 0, null, 121010001001260, now(), now(), '1', '1', 0, null);
