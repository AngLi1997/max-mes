# 检验菜单
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100, '检验管理', '120100', 120, 0, 1, 0, null, 130170, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100001, '请验单配置', '120100001', 120100, 0, 1, 0, null, 130170110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100001000001, '新增请验单', '120100001000001', 120100001, 0, 0, 0, null, 120100001110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100001000002, '编辑请验单', '120100001000002', 120100001, 0, 0, 0, null, 120100001120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100001000003, '删除请验单', '120100001000003', 120100001, 0, 0, 0, null, 120100001130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100001000004, '查看请验单', '120100001000004', 120100001, 0, 0, 0, null, 120100001140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100001000005, '绑定物料', '120100001000005', 120100001, 0, 0, 0, null, 120100001150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120100001000006, '启停', '120100001000006', 120100001, 0, 0, 0, null, 120100001160, now(), now(), '1', '1', 0, null);

# 查看检验结果按钮
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030007000007, '检验结果', '120030007000007', 120030007, 0, 0, 0, null, 120030007121, now(), now(), '1', '1', 0, null);
# 检验结果组件配置权限 by lilong 250218
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020001002026, '检验结果', '120020001002026', 120020001002, 0, 0, 0, null, 120020001120026, now(), now(), '1', '1', 0, null);

# 调整楼宇管理权限错误 by lilong 250312
UPDATE bmos_platform.bp_menu SET code = '200020001000001', parent_id = 200020001, sort = 200020001110 WHERE id = 200020001000001;
UPDATE bmos_platform.bp_menu SET code = '200020001000002', parent_id = 200020001, sort = 200020001120 WHERE id = 200020001000002;
UPDATE bmos_platform.bp_menu SET code = '200020001000003', parent_id = 200020001, sort = 200020001130 WHERE id = 200020001000003;
UPDATE bmos_platform.bp_menu SET code = '200020001000004', parent_id = 200020001, sort = 200020001140 WHERE id = 200020001000004;
UPDATE bmos_platform.bp_menu SET code = '200020001000005', parent_id = 200020001, sort = 200020001150 WHERE id = 200020001000005;
UPDATE bmos_platform.bp_menu SET code = '200020001000006', parent_id = 200020001, sort = 200020001160 WHERE id = 200020001000006;

# 产线管理-数据权限按钮 by lilong 250313
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160030001000011, '数据权限', '160030001000011', 160030001, 0, 0, 0, null, 160030001210, now(), now(), '1', '1', 0, null);
