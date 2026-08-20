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
