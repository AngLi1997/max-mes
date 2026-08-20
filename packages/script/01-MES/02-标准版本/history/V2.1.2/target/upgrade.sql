# 物料管理新增打印标签的按钮 by lilong 240813
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030007000006, '打印标签', '120030007000006', 120030007, 0, 0, 0, null, 120030007160, now(), now(), '1', '1', 0, null);
# 新增生产审核进度的菜单 by lilong 240813
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050009, '生产审核进度', '120050009', 120050, 0, 1, 0, null, 130150102, now(), now(), '1', '1', 0, null);

# 批签发菜单及权限变更
UPDATE bmos_platform.bp_menu SET name = '批签发配置' WHERE id = 120040002;
DELETE FROM bmos_platform.bp_menu WHERE bp_menu.parent_id IN (
'120040001',
'120040002',
'120040003',
'120040006'
    );
DELETE FROM bmos_platform.bp_menu WHERE id = 120040006;
DELETE FROM bmos_platform.bp_menu WHERE id = 120040001;
# 批签发配置
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040002000001, '新增分类', '120040002000001', 120040002, 0, 0, 0, null, 120040002110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040002000002, '编辑分类', '120040002000002', 120040002, 0, 0, 0, null, 120040002120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040002000003, '删除分类', '120040002000003', 120040002, 0, 0, 0, null, 120040002130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040002000004, '新增模版', '120040002000004', 120040002, 0, 0, 0, null, 120040002140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040002000005, '数据权限', '120040002000005', 120040002, 0, 0, 0, null, 120040002150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040002000006, '绑定工艺', '120040002000006', 120040002, 0, 0, 0, null, 120040002160, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040002000007, '新增版本', '120040002000007', 120040002, 0, 0, 0, null, 120040002170, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040002000008, '上传', '120040002000008', 120040002, 0, 0, 0, null, 120040002180, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040002000009, '下载', '120040002000009', 120040002, 0, 0, 0, null, 120040002190, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040002000010, '验证', '120040002000010', 120040002, 0, 0, 0, null, 120040002200, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040002000011, '生效', '120040002000011', 120040002, 0, 0, 0, null, 120040002210, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040002000012, '历史', '120040002000012', 120040002, 0, 0, 0, null, 120040002220, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040002000013, '作废', '120040002000013', 120040002, 0, 0, 0, null, 120040002230, now(), now(), '1', '1', 0, null);
# 批签发管理
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040003000001, '批记录', '120040003000001', 120040003, 0, 0, 0, null, 120040003110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040003000002, '批签发生成', '120040003000002', 120040003, 0, 0, 0, null, 120040003120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040003000003, '重新生成', '120040003000003', 120040003, 0, 0, 0, null, 120040003130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040003000004, '上传', '120040003000004', 120040003, 0, 0, 0, null, 120040003140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040003000005, '下载', '120040003000005', 120040003, 0, 0, 0, null, 120040003150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040003000006, '提交审批', '120040003000006', 120040003, 0, 0, 0, null, 120040003160, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040003000007, '审批进度', '120040003000007', 120040003, 0, 0, 0, null, 120040003170, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040003000008, '历史', '120040003000008', 120040003, 0, 0, 0, null, 120040003180, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040003000009, '作废', '120040003000009', 120040003, 0, 0, 0, null, 120040003190, now(), now(), '1', '1', 0, null);
# 批签发审核流绑定工艺
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020008000005, '绑定工艺', '120020008000005', 120020008, 0, 0, 0, null, 120020008150, now(), now(), '1', '1', 0, null);

# 数据集
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120070, '数据集', '120070', 120, 0, 1, 0, null, 130131, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120070001, '数据集管理', '120070001', 120070, 0, 1, 0, null, 130131110, now(), now(), '1', '1', 0, null);
# 数据集管理
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120070001000001, '新增分类', '120070001000001', 120070001, 0, 0, 0, null, 120070001110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120070001000002, '编辑分类', '120070001000002', 120070001, 0, 0, 0, null, 120070001120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120070001000003, '删除分类', '120070001000003', 120070001, 0, 0, 0, null, 120070001130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120070001000004, '新增数据集', '120070001000004', 120070001, 0, 0, 0, null, 120070001140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120070001000005, '编辑数据集', '120070001000005', 120070001, 0, 0, 0, null, 120070001150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120070001000006, '查看数据集', '120070001000006', 120070001, 0, 0, 0, null, 120070001160, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120070001000007, '删除数据集', '120070001000007', 120070001, 0, 0, 0, null, 120070001170, now(), now(), '1', '1', 0, null);

# 批记录
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080, '批记录', '120080', 120, 0, 1, 0, null, 130132, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001, '批记录配置', '120080001', 120080, 0, 1, 0, null, 130132110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080002, '批记录管理', '120080002', 120080, 0, 1, 0, null, 130132120, now(), now(), '1', '1', 0, null);

# 批记录配置
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001000001, '新增分类', '120080001000001', 120080001, 0, 0, 0, null, 120080001110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001000002, '编辑分类', '120080001000002', 120080001, 0, 0, 0, null, 120080001120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001000003, '删除分类', '120080001000003', 120080001, 0, 0, 0, null, 120080001130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001000004, '新增模版', '120080001000004', 120080001, 0, 0, 0, null, 120080001140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001000005, '数据权限', '120080001000005', 120080001, 0, 0, 0, null, 120080001150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001000006, '绑定工艺', '120080001000006', 120080001, 0, 0, 0, null, 120080001160, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001000007, '新增版本', '120080001000007', 120080001, 0, 0, 0, null, 120080001170, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001000008, '上传', '120080001000008', 120080001, 0, 0, 0, null, 120080001180, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001000009, '下载', '120080001000009', 120080001, 0, 0, 0, null, 120080001190, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001000010, '验证', '120080001000010', 120080001, 0, 0, 0, null, 120080001200, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001000011, '生效', '120080001000011', 120080001, 0, 0, 0, null, 120080001210, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001000012, '历史', '120080001000012', 120080001, 0, 0, 0, null, 120080001220, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001000013, '作废', '120080001000013', 120080001, 0, 0, 0, null, 120080001230, now(), now(), '1', '1', 0, null);
# 批记录管理
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080002000001, '批记录', '120080002000001', 120080002, 0, 0, 0, null, 120080002110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080002000002, '批记录生成', '120080002000002', 120080002, 0, 0, 0, null, 120080002120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080002000003, '重新生成', '120080002000003', 120080002, 0, 0, 0, null, 120080002130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080002000004, '上传', '120080002000004', 120080002, 0, 0, 0, null, 120080002140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080002000005, '下载', '120080002000005', 120080002, 0, 0, 0, null, 120080002150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080002000006, '历史', '120080002000006', 120080002, 0, 0, 0, null, 120080002160, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080002000007, '作废', '120080002000007', 120080002, 0, 0, 0, null, 120080002170, now(), now(), '1', '1', 0, null);

# 清除没角色、菜单的关联数据
DELETE FROM bmos_platform.bp_auth_role_menu WHERE role_id NOT IN (SELECT id FROM bmos_platform.bp_role );
DELETE FROM bmos_platform.bp_auth_role_menu WHERE menu_id NOT IN (SELECT id FROM bmos_platform.bp_menu );
DELETE FROM bmos_platform.bp_role_menu WHERE role_id NOT IN (SELECT id FROM bmos_platform.bp_role );
DELETE FROM bmos_platform.bp_role_menu WHERE menu_id NOT IN (SELECT id FROM bmos_platform.bp_menu );

# 批签发和批记录配置的按钮 by lilong 240819
UPDATE bmos_platform.bp_menu SET name = '确认' WHERE id = 120040002000011;
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040002000014, '生效', '120040002000014', 120040002, 0, 0, 0, null, 120040002240, now(), now(), '1', '1', 0, null);
UPDATE bmos_platform.bp_menu SET name = '确认' WHERE id = 120080001000011;
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080001000014, '设为默认', '120080001000014', 120080001, 0, 0, 0, null, 120080001240, now(), now(), '1', '1', 0, null);

# 生产执行增加查看工序和查看工艺按钮
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001010, '查看工序', '121010001001010', 121010001001, 1, 0, 0, null, 121010001001200, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001011, '查看工艺', '121010001001011', 121010001001, 1, 0, 0, null, 121010001001210, now(), now(), '1', '1', 0, null);
# WEB异常管理菜单及权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120090, '异常管理', '120090', 120, 0, 1, 0, null, 130160, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120090001, '异常管理', '120090001', 120090, 0, 1, 0, null, 130160110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120090001000001, '新增', '120090001000001', 120090001, 0, 0, 0, null, 120090001110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120090001000002, '编辑', '120090001000002', 120090001, 0, 0, 0, null, 120090001120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120090001000003, '处理', '120090001000003', 120090001, 0, 0, 0, null, 120090001130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120090001000004, '作废', '120090001000004', 120090001, 0, 0, 0, null, 120090001140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120090001000005, '操作历史', '120090001000005', 120090001, 0, 0, 0, null, 120090001150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120090001000006, '重新调查', '120090001000006', 120090001, 0, 0, 0, null, 120090001160, now(), now(), '1', '1', 0, null);
# APP异常管理菜单及权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121040, '异常管理', '121040', 121, 1, 1, 0, null, 140140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121040001, '异常管理', '121040001', 121040, 1, 1, 0, null, 140140110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121040001000001, '新增', '121040001000001', 121040001, 0, 0, 0, null, 121040001110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121040001000002, '编辑', '121040001000002', 121040001, 0, 0, 0, null, 121040001120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121040001000003, '处理', '121040001000003', 121040001, 0, 0, 0, null, 121040001130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121040001000004, '作废', '121040001000004', 121040001, 0, 0, 0, null, 121040001140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121040001000005, '操作历史', '121040001000005', 121040001, 0, 0, 0, null, 121040001150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121040001000006, '重新调查', '121040001000006', 121040001, 0, 0, 0, null, 121040001160, now(), now(), '1', '1', 0, null);
# 生产执行增加异常填报按钮
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001012, '异常填报', '121010001001012', 121010001001, 1, 0, 0, null, 121010001001220, now(), now(), '1', '1', 0, null);
# 生产进度和生产历史增加查看批次异常信息
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050001000003, '批次异常信息', '120050001000003', 120050001, 0, 0, 0, null, 120050001130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050008000002, '批次异常信息', '120050008000002', 120050008, 0, 0, 0, null, 120050008120, now(), now(), '1', '1', 0, null);

# 工艺配置增加排序按钮
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020006000015, '排序', '120020006000015', 120020006, 0, 0, 0, null, 120020006260, now(), now(), '1', '1', 0, null);
# 生产计划模版
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020014, '生产计划模版', '120020014', 120020, 0, 1, 0, null, 130120191, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020014000001, '新增计划模版', '120020014000001', 120020014, 0, 0, 0, null, 120020014110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020014000002, '编辑', '120020014000002', 120020014, 0, 0, 0, null, 120020014120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020014000003, '查看', '120020014000003', 120020014, 0, 0, 0, null, 120020014130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020014000004, '删除', '120020014000004', 120020014, 0, 0, 0, null, 120020014140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020014000005, '启停', '120020014000005', 120020014, 0, 0, 0, null, 120020014150, now(), now(), '1', '1', 0, null);
# 生产计划管理
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030011, '生产计划管理', '120030011', 120030, 0, 1, 0, null, 130130090, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030011000001, '新增生产计划', '120030011000001', 120030011, 0, 0, 0, null, 120030011110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030011000002, '日历调整', '120030011000002', 120030011, 0, 0, 0, null, 120030011120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030011000003, '查看', '120030011000003', 120030011, 0, 0, 0, null, 120030011130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030011000004, '作废', '120030011000004', 120030011, 0, 0, 0, null, 120030011140, now(), now(), '1', '1', 0, null);
# 生产计划日历
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030012, '生产计划日历', '120030012', 120030, 0, 1, 0, null, 130130100, now(), now(), '1', '1', 0, null);
# 生产指令单按钮调整
DELETE FROM bmos_platform.bp_menu WHERE id = 120030001000001;
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030001000009, '日历调整', '120030001000009', 120030001, 0, 0, 0, null, 120030001180, now(), now(), '1', '1', 0, null);

# 新增设备类型菜单和权限 by chelu 240821
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001, '设备类型', '160010001', 160010, 0, 1, 0, null, 160010110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001000001, '新增类型', '160010001000001', 160010001, 0, 0, 0, null, 160010001110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001000002, '编辑类型', '160010001000002', 160010001, 0, 0, 0, null, 160010001120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001000003, '删除类型', '160010001000003', 160010001, 0, 0, 0, null, 160010001130, now(), now(), '1', '1', 0, null);

# 批次摘要功能及权限 by lilong 240821
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050010, '批次摘要', '120050010', 120050, 0, 1, 0, null, 130150122, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050010000001, '新增摘要', '120050010000001', 120050010, 0, 0, 0, null, 120050010110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050010000002, '编辑摘要', '120050010000002', 120050010, 0, 0, 0, null, 120050010120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050010000003, '批次查询', '120050010000003', 120050010, 0, 0, 0, null, 120050010130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050010000004, '查看摘要', '120050010000004', 120050010, 0, 0, 0, null, 120050010140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050010000005, '删除摘要', '120050010000005', 120050010, 0, 0, 0, null, 120050010150, now(), now(), '1', '1', 0, null);

# 辅助记录功能及权限 by lilong 240821
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050011, '辅助记录', '120050011', 120050, 0, 1, 0, null, 130150123, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050011000001, '查看详情', '120050011000001', 120050011, 0, 0, 0, null, 120050011110, now(), now(), '1', '1', 0, null);

# 清场组件复核人签名权限 by lilong 240821
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002023, '清场检查-复核人签名', '121010001002023', 121010001002, 1, 0, 0, null, 121010001010120330, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002024, '清场执行-复核人签名', '121010001002024', 121010001002, 1, 0, 0, null, 121010001010120340, now(), now(), '1', '1', 0, null);

# 批签发设为默认按钮 by lilong 240822
UPDATE bmos_platform.bp_menu SET name = '设为默认' WHERE id = 120040002000014;

# 批记录审核菜单及权限 by lilong 240822
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080003, '批记录审核', '120080003', 120080, 0, 1, 0, null, 130132130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080003000001, '处理', '120080003000001', 120080003, 0, 0, 0, null, 120080003110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080003000002, '详情', '120080003000002', 120080003, 0, 0, 0, null, 120080003120, now(), now(), '1', '1', 0, null);

# 批签发审核权限 by lilong 240826
UPDATE bmos_platform.bp_menu SET name = '审批处理' WHERE id = 120040005000001;
UPDATE bmos_platform.bp_menu SET name = '下载' WHERE id = 120040005000002;
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040005000003, '审批进度', '120040005000003', 120040005, 0, 0, 0, null, 120040005130, now(), now(), '1', '1', 0, null);

# 设备使用日志填报菜单 by lilong 240826
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121030003, '设备使用日志填报', '121030003', 121030, 1, 1, 0, null, 140130130, now(), now(), '1', '1', 0, null);
# 清场-QA人员签名权限 by lilong 240827
UPDATE bmos_platform.bp_menu SET name = '清场-QA人员签名' WHERE id = 121010001002023;
DELETE FROM bmos_platform.bp_menu WHERE id = 121010001002024;

# 新增生产修订菜单 by lilong 240829
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010004, '生产修订', '121010004', 121010, 1, 1, 0, null, 140110140, now(), now(), '1', '1', 0, null);
# 批签发管理按钮 by lilong 240829
DELETE FROM bmos_platform.bp_menu WHERE parent_id = 120040003;
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040003000001, '版本管理', '120040003000001', 120040003, 0, 0, 0, null, 120040003110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040003000002, '下载', '120040003000002', 120040003, 0, 0, 0, null, 120040003120, now(), now(), '1', '1', 0, null);
# 批记录管理按钮 by lilong 240829
DELETE FROM bmos_platform.bp_menu WHERE parent_id = 120080002;
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080002000001, '版本管理', '120080002000001', 120080002, 0, 0, 0, null, 120080002110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080002000002, '下载', '120080002000002', 120080002, 0, 0, 0, null, 120080002120, now(), now(), '1', '1', 0, null);
# 删除批次异常信息按钮 by lilong 240829
DELETE FROM bmos_platform.bp_menu WHERE id = 120050001000003;
DELETE FROM bmos_platform.bp_menu WHERE id = 120050008000002;
# 生产审核进度新增查看详情按钮 by lilong 2408029
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050009000001, '查看详情', '120050009000001', 120050009, 0, 0, 0, null, 120050009110, now(), now(), '1', '1', 0, null);
# 库存管理新增退库、销毁、使用按钮 by lilong 240829
UPDATE bmos_platform.bp_menu SET name = '物料入库-递交人签名' WHERE id = 121020002000009;
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000013, '物料退库按钮', '121020002000013', 121020002, 1, 0, 0, null, 121020002000230, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000014, '物料销毁按钮', '121020002000014', 121020002, 1, 0, 0, null, 121020002000240, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000015, '物料使用按钮', '121020002000015', 121020002, 1, 0, 0, null, 121020002000250, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000016, '物料退库-复核人签名', '121020002000016', 121020002, 1, 0, 0, null, 121020002000260, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000017, '物料销毁-复核人签名', '121020002000017', 121020002, 1, 0, 0, null, 121020002000270, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000018, '物料使用-复核人签名', '121020002000018', 121020002, 1, 0, 0, null, 121020002000280, now(), now(), '1', '1', 0, null);
# 批签发审核权限 by lilong 240902
UPDATE bmos_platform.bp_menu SET name = '审批处理' WHERE id = 120080003000001;
UPDATE bmos_platform.bp_menu SET name = '下载' WHERE id = 120080003000002;
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120080003000003, '审批进度', '120080003000003', 120080003, 0, 0, 0, null, 120080003130, now(), now(), '1', '1', 0, null);
# 生产执行-趋势分析按钮权限 by lilong 240902
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001013, '趋势分析', '121010001001013', 121010001001, 1, 0, 0, null, 121010001001230, now(), now(), '1', '1', 0, null);
# 生产管理生产修订功能增加关联批次按钮 by lilong 240903
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001014, '关联批次', '121010001001014', 121010001001, 1, 0, 0, null, 121010001001240, now(), now(), '1', '1', 0, null);
# 物料接收功能 by lilong 240903
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020004, '物料接收', '121020004', 121020, 1, 1, 0, null, 140120121, now(), now(), '1', '1', 0, null);
# 记录配置物料件信息组件的权限 by lilong 240904
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020001002023, '物料件信息', '120020001002023', 120020001002, 0, 0, 0, null, 120020001120023, now(), now(), '1', '1', 0, null);
# 物料接收-递交人签名权限 by lilong 240904
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020004000001, '物料接收-递交人签名', '121020004000001', 121020004, 1, 0, 0, null, 121020004110, now(), now(), '1', '1', 0, null);
# 中间品产出-手动产出模式 by lilong 240905
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002024, '中间品产出-手动产出模式', '121010001002024', 121010001002, 1, 0, 0, null, 121010001010120203, now(), now(), '1', '1', 0, null);
# 数据修订签名 by lilong 240905
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010004000001, '数据保存-保存人签名', '121010004000001', 121010004, 1, 0, 0, null, 121010004110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010004000002, '数据保存-复核人签名', '121010004000002', 121010004, 1, 0, 0, null, 121010004120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010004000003, '数据修订-修订人签名', '121010004000003', 121010004, 1, 0, 0, null, 121010004130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010004000004, '数据修订-复核人签名', '121010004000004', 121010004, 1, 0, 0, null, 121010004140, now(), now(), '1', '1', 0, null);
# 生产进度和生产历史增加修订记录按钮 by lilong 240905
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050001000003, '修订记录', '120050001000003', 120050001, 0, 0, 0, null, 120050001130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050008000002, '修订记录', '120050008000002', 120050008, 0, 0, 0, null, 120050008120, now(), now(), '1', '1', 0, null);
# 删除批量创建指令单的按钮 by lilong 240918
DELETE FROM bmos_platform.bp_menu WHERE id = 120030001000002;
# 库存管理物料预定-复核人签名 by lilong 240927
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002000019, '物料预定-复核人签名', '121020002000019', 121020002, 1, 0, 0, null, 121020002000290, now(), now(), '1', '1', 0, null);
# 异常管理签名权限 by chendanting 240929
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120090001000007, '新增-操作人签名', '120090001000007', 120090001, 0, 0, 0, null, 120090001170, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120090001000008, '编辑-操作人签名', '120090001000008', 120090001, 0, 0, 0, null, 120090001180, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120090001000009, '处理-操作人签名', '120090001000009', 120090001, 0, 0, 0, null, 120090001190, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120090001000010, '作废-操作人签名', '120090001000010', 120090001, 0, 0, 0, null, 120090001200, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120090001000011, '重新调查-操作人签名', '120090001000011', 120090001, 0, 0, 0, null, 12009000210, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121040001000007, '新增-操作人签名', '121040001000007', 121040001, 0, 0, 0, null, 121040001170, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121040001000008, '编辑-操作人签名', '121040001000008', 121040001, 0, 0, 0, null, 121040001180, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121040001000009, '处理-操作人签名', '121040001000009', 121040001, 0, 0, 0, null, 121040001190, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121040001000010, '作废-操作人签名', '121040001000010', 121040001, 0, 0, 0, null, 121040001200, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121040001000011, '重新调查-操作人签名', '121040001000011', 121040001, 0, 0, 0, null, 121040001210, now(), now(), '1', '1', 0, null);
# 修改应用端设备状态菜单为设备管理 by lilong 241016
UPDATE bmos_platform.bp_menu SET name = '设备管理' WHERE id = 121030001;
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
# 批记录审批 by lilong 240822
INSERT INTO bmos_mes.bm_flow_audit_category (id, name, code, parent_id, create_by, update_by, create_time, update_time, is_deleted, tree_code, tree_name) VALUES (120050, '批记录', '120050', 0, '1', '1', now(), now(), 0, '120050,120050001', '批记录');
INSERT INTO bmos_mes.bm_flow_audit_category (id, name, code, parent_id, create_by, update_by, create_time, update_time, is_deleted, tree_code, tree_name) VALUES (120050001, '批记录审核', '120050001', 120050, '1', '1', now(), now(), 0, '120050001', '批记录/批记录审核');
# 新增物料接收打印标签 by lilong 240918
INSERT INTO bmos_platform.bp_tag_scene (id, tag_scene_name, tag_scene_desc, tag_type_id, data_source_service_name, data_source_interface, qr_code_field, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010, '原辅包物料接收打印标签', '原辅包物料接收打印标签', 1, 'bmos-mes-service', '/api/app/mes/tag/print/STORAGE_MATERIAL', 'materialNo', 1200, now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene (id, tag_scene_name, tag_scene_desc, tag_type_id, data_source_service_name, data_source_interface, qr_code_field, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014, '中间品物料接收打印标签', '中间品物料接收打印标签', 2, 'bmos-mes-service', '/api/app/mes/tag/print/STORAGE_MATERIAL', 'materialNo', 2240, now(), null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_instance (id, tag_name, tag_type_id, tag_scene_id, tag_define_id, config_fields, is_enable, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010, '原辅包物料接收打印标签', 1, 121001010, 1, '[{"label":"物料信息：","defineField":"field1","dataSourceField":"fullName","consumeValue":null},{"label":"物料规格：","defineField":"field2","dataSourceField":"materialSpecification","consumeValue":null},{"label":"物料批号：","defineField":"field3","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"物料件号：","defineField":"field4","dataSourceField":"materialNo","consumeValue":null},{"label":"物料量：","defineField":"field5","dataSourceField":"quantityWithUnit","consumeValue":null},{"label":"有效期至：","defineField":"field6","dataSourceField":"expiredDate","consumeValue":null},{"label":"暂存货位：","defineField":"field7","dataSourceField":"positionFullName","consumeValue":null},{"label":null,"defineField":"field8","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field9","dataSourceField":null,"consumeValue":null}]', 'TRUE', now(), now(), null, null, 0);
INSERT INTO bmos_platform.bp_tag_instance (id, tag_name, tag_type_id, tag_scene_id, tag_define_id, config_fields, is_enable, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014, '中间品物料接收打印标签', 2, 121002014, 1, '[{"label":"物料信息：","defineField":"field1","dataSourceField":"fullName","consumeValue":null},{"label":"物料规格：","defineField":"field2","dataSourceField":"materialSpecification","consumeValue":null},{"label":"物料批号：","defineField":"field3","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"物料件号：","defineField":"field4","dataSourceField":"materialNo","consumeValue":null},{"label":"物料量：","defineField":"field5","dataSourceField":"quantityWithUnit","consumeValue":null},{"label":"有效期至：","defineField":"field6","dataSourceField":"expiredDate","consumeValue":null},{"label":"暂存货位：","defineField":"field7","dataSourceField":"positionFullName","consumeValue":null},{"label":null,"defineField":"field8","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field9","dataSourceField":null,"consumeValue":null}]', 'TRUE', now(), now(), null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010001, 121001010, 'fullName', '物料全称', 'String', 'W01-氯化钠', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010002, 121001010, 'materialName', '物料名称', 'String', '氯化钠', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010003, 121001010, 'materialMergeCode', '物料编码', 'String', 'W01', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010004, 121001010, 'materialSpecification', '物料规格', 'String', '25kg/袋', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010005, 121001010, 'materialBatchNo', '物料批号', 'String', 'WH01-221001', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010006, 121001010, 'materialNo', '物料件号', 'String', '10086', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010007, 121001010, 'quantityWithUnit', '物料量(带单位)', 'String', '15.780kg', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010008, 121001010, 'netWeightWithUnit', '净重(带单位)', 'String', '15.780kg', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010009, 121001010, 'tareWeightWithUnit', '皮重(带单位)', 'String', '5.180kg', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010010, 121001010, 'grossWeightWithUnit', '毛重(带单位)', 'String', '20.960kg', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010011, 121001010, 'weigherName', '称量人员', 'String', '张三', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010012, 121001010, 'reCheckerName', '称量复核人员', 'String', '李四', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010013, 121001010, 'weighTime', '称量时间', 'String', '2024-02-02 14:36:42', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010014, 121001010, 'productName', '产品名称', 'String', '氯化钠溶液', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010015, 121001010, 'productMergeCode', '产品编码', 'String', 'C01', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010016, 121001010, 'productSpecification', '产品规格', 'String', '0.9%', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010017, 121001010, 'processName', '工艺名称', 'String', '氯化钠溶液配置工艺', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010018, 121001010, 'batchNo', '生产批号', 'String', 'C01230101', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010019, 121001010, 'expiredDate', '有效期至', 'String', '2025-10-31', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001010020, 121001010, 'positionFullName', '暂存货位', 'String', 'KQ10-01-氯化钠货位', now(), null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014001, 121002014, 'fullName', '物料全称', 'String', 'W01-氯化钠', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014002, 121002014, 'materialName', '物料名称', 'String', '氯化钠', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014003, 121002014, 'materialMergeCode', '物料编码', 'String', 'W01', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014004, 121002014, 'materialSpecification', '物料规格', 'String', '25kg/袋', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014005, 121002014, 'materialBatchNo', '物料批号', 'String', 'WH01-221001', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014006, 121002014, 'materialNo', '物料件号', 'String', '10086', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014007, 121002014, 'quantityWithUnit', '物料量(带单位)', 'String', '15.780kg', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014008, 121002014, 'netWeightWithUnit', '净重(带单位)', 'String', '15.780kg', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014009, 121002014, 'tareWeightWithUnit', '皮重(带单位)', 'String', '5.180kg', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014010, 121002014, 'grossWeightWithUnit', '毛重(带单位)', 'String', '20.960kg', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014011, 121002014, 'weigherName', '称量人员', 'String', '张三', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014012, 121002014, 'reCheckerName', '称量复核人员', 'String', '李四', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014013, 121002014, 'weighTime', '称量时间', 'String', '2024-02-02 14:36:42', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014014, 121002014, 'productName', '产品名称', 'String', '氯化钠溶液', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014015, 121002014, 'productMergeCode', '产品编码', 'String', 'C01', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014016, 121002014, 'productSpecification', '产品规格', 'String', '0.9%', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014017, 121002014, 'processName', '工艺名称', 'String', '氯化钠溶液配置工艺', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014018, 121002014, 'batchNo', '生产批号', 'String', 'C01230101', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014019, 121002014, 'expiredDate', '有效期至', 'String', '2025-10-31', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002014020, 121002014, 'positionFullName', '暂存货位', 'String', 'KQ10-01-氯化钠货位', now(), null, null, null, 0);
# 调整默认标签样式的长宽单位 by liang 240919
UPDATE bmos_platform.bp_tag_define SET tag_width = 70, tag_height = 40 WHERE id = 1;
# 生产指令单是否需要确认 by lilong 240807
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120012, 'mes.schedule.confirm.isRequired', 'true', 'STRING', 'BUSINESS', '平台', '生产指令单是否需要确认', 121210, '', 1, '1', '1', now(), now(), 0);
# 新增批签发excel宏的参数 by lilong 240829
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120013, 'mes.lot.release.VBA', '["宏1","宏2"]', 'STRING', 'BUSINESS', '生产', '批签发excel宏，输入宏命名，按顺序执行', 121220, '', 1, '1', '1', now(), now(), 0);
UPDATE bmos_platform.bp_business_parameter SET belong = '生产' WHERE id = 120012;
# 新增系统版本号的参数 by lilong 240909
REPLACE INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100022, 'platform.sys.version', 'V2.1.1', 'STRING', 'BUSINESS', '平台', '系统版本号', 100220, '', 1, '1', '1', now(), now(), 0);
# 新增MQTT服务地址的参数 by yigaohui 240810
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100023, 'platform.sys.MQTT.address', '', 'STRING', 'BUSINESS', '平台', 'MQTT服务地址', 100230, '', 1, '1', '1', now(), now(), 0);

# 新增称量协议的配置参数 by lilong 240919
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100024, 'platform.sys.weighing.protocol-type', '[
  {
    "name":"赛多利斯",
    "type":"Hex",
    // 清零
    "clear":"",
    // 去皮
    "peel":"",
    // 取值
    "read":"",
	// 正则
	"value":""
  }，
  {
    "name":"常熟双杰",
    "type":"ASCII",
    // 清零
    "clear":"",
    // 去皮
    "peel":"",
    // 取值
    "read":"",
	// 正则
	"value":""
  }，
  {
    "name":"梅特勒",
    "type":"ASCII",
    // 清零
    "clear":"T",
    // 去皮
    "peel":"Z",
    // 取值
    "read":"SI",
	// 正则
	"value":""
  }
]', 'JSON', 'BUSINESS', '平台', '称量协议类型配置', 100240, '', 1, '1', '1', now(), now(), 0);
# 数值组件趋势分析近n个批次 by zhangruoyu 240923
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120014, 'mes.field.trend.analysis', '50', 'NUMBER', 'BUSINESS', null, '数值组件趋势分析近n个批次', 121230, null, 1, '1', '1', now(), now(), 0);
# 更新数值组件趋势分析近n个批次参数所属应用 by lilong 240929
UPDATE bmos_platform.bp_business_parameter SET belong = '生产' WHERE id = 120014;

# 调整称量协议的配置参数 by lilong 241010
UPDATE bmos_platform.bp_business_parameter SET value = '[{"name":"赛多利斯","type":"Hex","clear":"","peel":"","read":"","value":""},{"name":"常熟双杰","type":"ASCII","clear":"","peel":"","read":"","value":""},{"name":"梅特勒","type":"ASCII","clear":"T","peel":"Z","read":"R","value":""}]' WHERE id = 100024;
# 调整称量协议的配置参数 by lilong 241011
UPDATE bmos_platform.bp_business_parameter SET value = '[{"name":"赛多利斯","type":"Hex","clear":"1B540D0A","peel":"1B540D0A","read":"1B500D0A","value":"","sendType":"unit8Array"},{"name":"常熟双杰","type":"ASCII","clear":"","peel":"","read":"","value":"","sendType":""},{"name":"梅特勒","type":"ASCII","clear":"T","peel":"Z","read":"R","value":"","sendType":""}]' WHERE id = 100024;

# 调整称量协议的配置参数 by lilong 241018
UPDATE bmos_platform.bp_business_parameter SET value = '[{"name":"赛多利斯","type":"","clear":"1B540D0A","peel":"1B540D0A","read":"1B500D0A","value":"","sendType":"unit8Array"},{"name":"常熟双杰","type":"ASCII","clear":"","peel":"","read":"","value":"","sendType":""},{"name":"梅特勒","type":"ASCII","clear":"T","peel":"Z","read":"R","value":"","sendType":""}]' WHERE id = 100024;
