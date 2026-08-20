# 增加配方配置/审批菜单及默认权限 by lilong 240307
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020004, '配方配置', '120020004', 120020, 0, 1, 130120160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020005, '配方审批', '120020005', 120020, 0, 1, 130120170, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020004000001, '默认权限', '120020004000001', 120020004, 0, 0, 130120160110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020005000001, '默认权限', '120020005000001', 120020005, 0, 0, 130120170110, now(), now(), '1', '1', 0);
# 增加标签管理菜单及默认权限 by lilong 240307
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020007, '标签管理', '100020007', 100020, 0, 1, 110120170, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020007000001, '默认权限', '100020007000001', 100020007, 0, 0, 110120170110, now(), now(), '1', '1', 0);

# 平台权限码梳理
DELETE FROM bmos_platform.bp_menu WHERE id >999999999;
# 参数配置
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100010002001001, '编辑参数', '100010002001001', 100010002, 0, 0, 0, null, 110110120110, now(), now(), '1', '1', 0);
# 编号规则
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020001001001, '新建编号规则', '100020001001001', 100020001, 0, 0, 0, null, 110110120110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020001001002, '新增版本', '100020001001002', 100020001, 0, 0, 0, null, 110110120120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020001001003, '编辑', '100020001001003', 100020001, 0, 0, 0, null, 110110120130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020001001004, '查看', '100020001001004', 100020001, 0, 0, 0, null, 110110120140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020001001005, '确认', '100020001001005', 100020001, 0, 0, 0, null, 110110120150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020001001006, '删除', '100020001001006', 100020001, 0, 0, 0, null, 110110120160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020001001007, '启停', '100020001001007', 100020001, 0, 0, 0, null, 110110120170, now(), now(), '1', '1', 0);
# 公式配置
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020006000001, '新增分类', '100020006000001', 100020006, 0, 0, 0, null, 110120160110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020006000002, '编辑分类', '100020006000002', 100020006, 0, 0, 0, null, 110120160120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020006000003, '删除分类', '100020006000003', 100020006, 0, 0, 0, null, 110120160130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020006000004, '新建公式', '100020006000004', 100020006, 0, 0, 0, null, 110120160140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020006000005, '编辑', '100020006000005', 100020006, 0, 0, 0, null, 110120160150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020006000006, '查看', '100020006000006', 100020006, 0, 0, 0, null, 110120160160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020006000007, '确认', '100020006000007', 100020006, 0, 0, 0, null, 110120160170, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020006000008, '删除', '100020006000008', 100020006, 0, 0, 0, null, 110120160180, now(), now(), '1', '1', 0);
# 标签配置
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020007000001, '新增标签', '100020007000001', 100020007, 0, 0, 0, null, 110120170110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020007000002, '编辑', '100020007000002', 100020007, 0, 0, 0, null, 110120170120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020007000003, '查看', '100020007000003', 100020007, 0, 0, 0, null, 110120170130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020007000004, '删除', '100020007000004', 100020007, 0, 0, 0, null, 110120170140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020007000005, '启停', '100020007000005', 100020007, 0, 0, 0, null, 110120170150, now(), now(), '1', '1', 0);
# 字典管理
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020009000001, '新建字典', '100020009000001', 100020009, 0, 0, 0, null, 110120190110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020009000002, '查看字典', '100020009000002', 100020009, 0, 0, 0, null, 110120190120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020009000003, '新增数据', '100020009000003', 100020009, 0, 0, 0, null, 110120190130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020009000004, '编辑数据', '100020009000004', 100020009, 0, 0, 0, null, 110120190140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020009000005, '查看数据', '100020009000005', 100020009, 0, 0, 0, null, 110120190150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020009000006, '删除数据', '100020009000006', 100020009, 0, 0, 0, null, 110120190160, now(), now(), '1', '1', 0);
# 用户管理
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030001000001, '新增用户', '100030001000001', 100030001, 0, 0, 0, null, 110130110110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030001000002, '导入用户', '100030001000002', 100030001, 0, 0, 0, null, 110130110120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030001000003, '导出用户', '100030001000003', 100030001, 0, 0, 0, null, 110130110130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030001000004, '查看', '100030001000004', 100030001, 0, 0, 0, null, 110130110140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030001000005, '绑定角色', '100030001000005', 100030001, 0, 0, 0, null, 110130110150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030001000006, '分配部门', '100030001000006', 100030001, 0, 0, 0, null, 110130110160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030001000007, '重置密码', '100030001000007', 100030001, 0, 0, 0, null, 110130110170, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030001000008, '启停', '100030001000008', 100030001, 0, 0, 0, null, 110130110180, now(), now(), '1', '1', 0);
# 部门管理
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030002000001, '新增部门', '100030002000001', 100030002, 0, 0, 0, null, 110130120110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030002000002, '编辑部门', '100030002000002', 100030002, 0, 0, 0, null, 110130120120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030002000003, '删除部门', '100030002000003', 100030002, 0, 0, 0, null, 110130120130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030002000004, '分配人员', '100030002000004', 100030002, 0, 0, 0, null, 110130120140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030002000005, '移除', '100030002000005', 100030002, 0, 0, 0, null, 110130120150, now(), now(), '1', '1', 0);
# 角色管理
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030003000001, '新增分类', '100030003000001', 100030003, 0, 0, 0, null, 110130130110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030003000002, '编辑分类', '100030003000002', 100030003, 0, 0, 0, null, 110130130120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030003000003, '删除分类', '100030003000003', 100030003, 0, 0, 0, null, 110130130130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030003000004, '新增角色', '100030003000004', 100030003, 0, 0, 0, null, 110130130140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030003000005, '人员分配', '100030003000005', 100030003, 0, 0, 0, null, 110130130150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030003000006, '菜单分配', '100030003000006', 100030003, 0, 0, 0, null, 110130130160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030003000007, '编辑', '100030003000007', 100030003, 0, 0, 0, null, 110130130170, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030003000008, '删除', '100030003000008', 100030003, 0, 0, 0, null, 110130130180, now(), now(), '1', '1', 0);
# 权限授权
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030004000001, '编辑', '100030004000001', 100030004, 0, 0, 0, null, 110130140110, now(), now(), '1', '1', 0);
# 菜单权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030005000001, '编辑', '100030005000001', 100030005, 0, 0, 0, null, 110130150110, now(), now(), '1', '1', 0);
# 单位管理
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040001000001, '新增标准单位', '100040001000001', 100040001, 0, 0, 0, null, 110140110110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040001000002, '查看标准单位', '100040001000002', 100040001, 0, 0, 0, null, 110140110120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040001000003, '编辑标准单位', '100040001000003', 100040001, 0, 0, 0, null, 110140110130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040001000004, '删除标准单位', '100040001000004', 100040001, 0, 0, 0, null, 110140110140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040001000005, '启停标准单位', '100040001000005', 100040001, 0, 0, 0, null, 110140110150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040001000006, '新增扩展单位', '100040001000006', 100040001, 0, 0, 0, null, 110140110160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040001000007, '查看扩展单位', '100040001000007', 100040001, 0, 0, 0, null, 110140110170, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040001000008, '编辑扩展单位', '100040001000008', 100040001, 0, 0, 0, null, 110140110180, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040001000009, '删除扩展单位', '100040001000009', 100040001, 0, 0, 0, null, 110140110190, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040001000010, '启停扩展单位', '100040001000010', 100040001, 0, 0, 0, null, 110140110210, now(), now(), '1', '1', 0);
# 物料信息
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040002000001, '新增物料', '100040002000001', 100040002, 0, 0, 0, null, 110140120110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040002000002, '下发', '100040002000002', 100040002, 0, 0, 0, null, 110140120120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040002000003, '导入', '100040002000003', 100040002, 0, 0, 0, null, 110140120130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040002000004, '查看', '100040002000004', 100040002, 0, 0, 0, null, 110140120140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040002000005, '编辑', '100040002000005', 100040002, 0, 0, 0, null, 110140120150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040002000006, '单位配置', '100040002000006', 100040002, 0, 0, 0, null, 110140120160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040002000007, '删除', '100040002000007', 100040002, 0, 0, 0, null, 110140120170, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040002000008, '启停', '100040002000008', 100040002, 0, 0, 0, null, 110140120180, now(), now(), '1', '1', 0);

# 更新系统一级菜单别名 by lilong 240312
UPDATE bmos_platform.bp_menu SET alias = 'BM-PMP' WHERE id = 100;
UPDATE bmos_platform.bp_menu SET alias = 'BM-ATM' WHERE id = 111;
UPDATE bmos_platform.bp_menu SET alias = 'BM-MES' WHERE id = 120;
UPDATE bmos_platform.bp_menu SET alias = 'BM-LIMS' WHERE id = 130;

# 增加项目配置菜单 by lilong 240312
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100010005, '项目配置', '100010005', 100010, 0, 1, 0, null, 110110150, now(), now(), '1', '1', 0);

# 字典管理权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020009000007, '编辑字典', '100020009000007', 100020009, 0, 0, 0, null, 110120190121, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100020009000008, '删除字典', '100020009000008', 100020009, 0, 0, 0, null, 110120190122, now(), now(), '1', '1', 0);

# 用户管理权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030001000009, '解锁', '100030001000009', 100030001, 0, 0, 0, null, 110130110190, now(), now(), '1', '1', 0);

# 用户管理权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100030001000010, '编辑', '100030001000010', 100030001, 0, 0, 0, null, 110130110131, now(), now(), '1', '1', 0);

# 物料管理
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040002000009, '新增物料分类', '100040002000009', 100040002, 0, 0, 0, null, 110140120101, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040002000010, '编辑物料分类', '100040002000010', 100040002, 0, 0, 0, null, 110140120102, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (100040002000011, '删除物料分类', '100040002000011', 100040002, 0, 0, 0, null, 110140120103, now(), now(), '1', '1', 0);

# 新增移动端库存管理菜单 by lilong 240314
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020, '物料管理', '121020', 121, 1, 1, 0, null, 140120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121020002, '库存管理', '121020002', 121020, 0, 1, 0, null, 140120120, now(), now(), '1', '1', 0, null);

# lims菜单权限 by lilong 240223
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130, '实验室信息管理系统', '130', 0, 1, 1, 150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130010, '基础数据', '130010', 130, 0, 1, 150110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020, '检验管理', '130020', 130, 0, 1, 150120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130010001, '检品管理', '130010001', 130010, 0, 1, 150110110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130010002, '分析项管理', '130010002', 130010, 0, 1, 150110120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130010003, '检验项目管理', '130010003', 130010, 0, 1, 150110130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130010004, '实验包管理', '130010004', 130010, 0, 1, 150110140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020001, '检验查询', '130020001', 130020, 0, 1, 150120110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002, '请验确认', '130020002', 130020, 0, 1, 150120120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020003, '取样', '130020003', 130020, 0, 1, 150120130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020004, '检验录入', '130020004', 130020, 0, 1, 150120140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020005, '报告生成', '130020005', 130020, 0, 1, 150120150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020006, '报告审核', '130020006', 130020, 0, 1, 150120160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020007, '报告签发', '130020007', 130020, 0, 1, 150120170, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130010001000001, '默认权限', '130010001000001', 130010001, 0, 0, 150110110110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130010002000001, '默认权限', '130010002000001', 130010002, 0, 0, 150110120110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130010003000001, '默认权限', '130010003000001', 130010003, 0, 0, 150110130110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130010004000001, '默认权限', '130010004000001', 130010004, 0, 0, 150110140110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020001000001, '默认权限', '130020001000001', 130020001, 0, 0, 150120110110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002000001, '默认权限', '130020002000001', 130020002, 0, 0, 150120120110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020003000001, '默认权限', '130020003000001', 130020003, 0, 0, 150120130110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020004000001, '默认权限', '130020004000001', 130020004, 0, 0, 150120140110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020005000001, '默认权限', '130020005000001', 130020005, 0, 0, 150120150110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020006000001, '默认权限', '130020006000001', 130020006, 0, 0, 150120160110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020007000001, '默认权限', '130020007000001', 130020007, 0, 0, 150120170110, now(), now(), '1', '1', 0);

# LIMS菜单终端类型修正 by lilong 240304
UPDATE bmos_platform.bp_menu SET terminal_type = 0 WHERE id = 130;
# LIMS系统别名 by lilong 240314
UPDATE bmos_platform.bp_menu SET alias = 'BM-LIMS' WHERE id = 130;

# 删除LIMS默认权限 by lilong 240314
delete from bmos_platform.bp_menu where bmos_platform.bp_menu.id in (
'130010001000001',
'130010002000001',
'130010003000001',
'130010004000001',
'130020001000001',
'130020002000001',
'130020003000001',
'130020004000001',
'130020005000001',
'130020006000001',
'130020007000001'
    );

# 修正库存管理菜单终端类型 by lilong 240314
UPDATE bmos_platform.bp_menu SET terminal_type = 1 WHERE id = 121020002;

# 登录日志
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (111010001000001, '导出', '111010001000001', 111010001, 0, 0, 0, null, 111010001110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (111010001000002, '查看', '111010001000002', 111010001, 0, 0, 0, null, 111010001120, now(), now(), '1', '1', 0);
# 操作日志
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (111010002000001, '导出', '111010002000001', 111010002, 0, 0, 0, null, 111010002110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (111010002000002, '查看', '111010002000002', 111010002, 0, 0, 0, null, 111010002120, now(), now(), '1', '1', 0);
# 审批流追溯
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (111020001000001, '导出', '111020001000001', 111020001, 0, 0, 0, null, 111020001110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (111020001000002, '查看', '111020001000002', 111020001, 0, 0, 0, null, 111020001130, now(), now(), '1', '1', 0);
# 签名追溯
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (111020003000001, '导出', '111020003000001', 111020003, 0, 0, 0, null, 111020003110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (111020003000002, '查看', '111020003000002', 111020003, 0, 0, 0, null, 111020003120, now(), now(), '1', '1', 0);

# 原辅包信息
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010001000001, '新增', '120010001000001', 120010001, 0, 0, 0, null, 120010001110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010001000002, '同步', '120010001000002', 120010001, 0, 0, 0, null, 120010001120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010001000003, '查看', '120010001000003', 120010001, 0, 0, 0, null, 120010001130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010001000004, '编辑', '120010001000004', 120010001, 0, 0, 0, null, 120010001140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010001000005, '删除', '120010001000005', 120010001, 0, 0, 0, null, 120010001150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010001000006, '启停', '120010001000006', 120010001, 0, 0, 0, null, 120010001160, now(), now(), '1', '1', 0);
# 中间品信息
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010002000001, '新增', '120010002000001', 120010002, 0, 0, 0, null, 120010002110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010002000002, '同步', '120010002000002', 120010002, 0, 0, 0, null, 120010002120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010002000003, '查看', '120010002000003', 120010002, 0, 0, 0, null, 120010002130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010002000004, '编辑', '120010002000004', 120010002, 0, 0, 0, null, 120010002140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010002000005, '删除', '120010002000005', 120010002, 0, 0, 0, null, 120010002150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010002000006, '启停', '120010002000006', 120010002, 0, 0, 0, null, 120010002160, now(), now(), '1', '1', 0);
# 产品信息
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010003000001, '新增', '120010003000001', 120010003, 0, 0, 0, null, 120010003110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010003000002, '同步', '120010003000002', 120010003, 0, 0, 0, null, 120010003120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010003000003, '查看', '120010003000003', 120010003, 0, 0, 0, null, 120010003130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010003000004, '编辑', '120010003000004', 120010003, 0, 0, 0, null, 120010003140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010003000005, '批记录', '120010003000005', 120010003, 0, 0, 0, null, 120010003150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010003000006, '删除', '120010003000006', 120010003, 0, 0, 0, null, 120010003160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010003000007, '启停', '120010003000007', 120010003, 0, 0, 0, null, 120010003170, now(), now(), '1', '1', 0);
# 流程配置
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020008000001, '新建流程', '120020008000001', 120020008, 0, 0, 0, null, 120020008110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020008000002, '编辑', '120020008000002', 120020008, 0, 0, 0, null, 120020008120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020008000003, '升级版本', '120020008000003', 120020008, 0, 0, 0, null, 120020008130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020008000004, '详情', '120020008000004', 120020008, 0, 0, 0, null, 120020008140, now(), now(), '1', '1', 0);
# 编号规则
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020009000001, '编辑', '120020009000001', 120020009, 0, 0, 0, null, 120020009110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020009000002, '批量配置', '120020009000002', 120020009, 0, 0, 0, null, 120020009120, now(), now(), '1', '1', 0);
# 暂存间配置
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020010000001, '新增区域', '120020010000001', 120020010, 0, 0, 0, null, 120020010110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020010000002, '编辑区域', '120020010000002', 120020010, 0, 0, 0, null, 120020010120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020010000003, '删除区域', '120020010000003', 120020010, 0, 0, 0, null, 120020010130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020010000004, '新增货位', '120020010000004', 120020010, 0, 0, 0, null, 120020010140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020010000005, '查看', '120020010000005', 120020010, 0, 0, 0, null, 120020010150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020010000006, '编辑', '120020010000006', 120020010, 0, 0, 0, null, 120020010160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020010000007, '数据权限', '120020010000007', 120020010, 0, 0, 0, null, 120020010170, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020010000008, '删除', '120020010000008', 120020010, 0, 0, 0, null, 120020010180, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020010000009, '启停', '120020010000009', 120020010, 0, 0, 0, null, 120020010190, now(), now(), '1', '1', 0);
# 记录配置
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020001000001, '新增分类', '120020001000001', 120020001, 0, 0, 0, null, 120020001110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020001000002, '编辑分类', '120020001000002', 120020001, 0, 0, 0, null, 120020001120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020001000003, '删除分类', '120020001000003', 120020001, 0, 0, 0, null, 120020001130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020001000004, '新增记录', '120020001000004', 120020001, 0, 0, 0, null, 120020001140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020001000005, '数据权限', '120020001000005', 120020001, 0, 0, 0, null, 120020001150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020001000006, '绑定产品', '120020001000006', 120020001, 0, 0, 0, null, 120020001160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020001000007, '新增版本', '120020001000007', 120020001, 0, 0, 0, null, 120020001170, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020001000008, '记录编辑', '120020001000008', 120020001, 0, 0, 0, null, 120020001180, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020001000009, '公式配置', '120020001000009', 120020001, 0, 0, 0, null, 120020001190, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020001000010, '查看', '120020001000010', 120020001, 0, 0, 0, null, 120020001200, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020001000011, '历史', '120020001000011', 120020001, 0, 0, 0, null, 120020001210, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020001000012, '审批', '120020001000012', 120020001, 0, 0, 0, null, 120020001220, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020001000013, '作废', '120020001000013', 120020001, 0, 0, 0, null, 120020001230, now(), now(), '1', '1', 0);
# 记录审批
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020002000001, '处理', '120020002000001', 120020002, 0, 0, 0, null, 120020002110, now(), now(), '1', '1', 0);
# 配方配置
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020004000001, '新增配方', '120020004000001', 120020004, 0, 0, 0, null, 120020004110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020004000002, '数据权限', '120020004000002', 120020004, 0, 0, 0, null, 120020004120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020004000003, '新增版本', '120020004000003', 120020004, 0, 0, 0, null, 120020004130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020004000004, '编辑', '120020004000004', 120020004, 0, 0, 0, null, 120020004140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020004000005, '查看', '120020004000005', 120020004, 0, 0, 0, null, 120020004150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020004000006, '审批', '120020004000006', 120020004, 0, 0, 0, null, 120020004160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020004000007, '历史', '120020004000007', 120020004, 0, 0, 0, null, 120020004170, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020004000008, '启停', '120020004000008', 120020004, 0, 0, 0, null, 120020004180, now(), now(), '1', '1', 0);
# 配方审批
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020005000001, '处理', '120020005000001', 120020005, 0, 0, 0, null, 120020005110, now(), now(), '1', '1', 0);
# 工艺配置
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020006000001, '新增工艺', '120020006000001', 120020006, 0, 0, 0, null, 120020006110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020006000002, '关联工艺', '120020006000002', 120020006, 0, 0, 0, null, 120020006120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020006000003, '数据权限', '120020006000003', 120020006, 0, 0, 0, null, 120020006130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020006000004, '新增版本', '120020006000004', 120020006, 0, 0, 0, null, 120020006140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020006000005, '复制工艺', '120020006000005', 120020006, 0, 0, 0, null, 120020006150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020006000006, '归档顺序', '120020006000006', 120020006, 0, 0, 0, null, 120020006160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020006000007, '编辑', '120020006000007', 120020006, 0, 0, 0, null, 120020006170, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020006000008, '查看', '120020006000008', 120020006, 0, 0, 0, null, 120020006180, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020006000009, '审批', '120020006000009', 120020006, 0, 0, 0, null, 120020006190, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020006000010, '历史', '120020006000010', 120020006, 0, 0, 0, null, 120020006200, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020006000011, '启停', '120020006000011', 120020006, 0, 0, 0, null, 120020006210, now(), now(), '1', '1', 0);
# 工艺审批
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120020007000001, '处理', '120020007000001', 120020007, 0, 0, 0, null, 120020007110, now(), now(), '1', '1', 0);
# 生产计划
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030001000001, '新建计划', '120030001000001', 120030001, 0, 0, 0, null, 120030001110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030001000002, '批量创建', '120030001000002', 120030001, 0, 0, 0, null, 120030001120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030001000003, '编辑', '120030001000003', 120030001, 0, 0, 0, null, 120030001130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030001000004, '查看', '120030001000004', 120030001, 0, 0, 0, null, 120030001140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030001000005, '提交审核', '120030001000005', 120030001, 0, 0, 0, null, 120030001150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030001000006, '作废', '120030001000006', 120030001, 0, 0, 0, null, 120030001160, now(), now(), '1', '1', 0);
# 计划审批
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030002000001, '处理', '120030002000001', 120030002, 0, 0, 0, null, 120030002110, now(), now(), '1', '1', 0);
# 指令单分解
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030003000001, '分解', '120030003000001', 120030003, 0, 0, 0, null, 120030003110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030003000002, '查看', '120030003000002', 120030003, 0, 0, 0, null, 120030003120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030003000003, '下发', '120030003000003', 120030003, 0, 0, 0, null, 120030003130, now(), now(), '1', '1', 0);
# 指令单确认
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030004000001, '确认', '120030004000001', 120030004, 0, 0, 0, null, 120030004110, now(), now(), '1', '1', 0);
# 班组管理
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030005000001, '新建班组', '120030005000001', 120030005, 0, 0, 0, null, 120030005110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030005000002, '编辑', '120030005000002', 120030005, 0, 0, 0, null, 120030005120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030005000003, '查看', '120030005000003', 120030005, 0, 0, 0, null, 120030005130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030005000004, '启停', '120030005000004', 120030005, 0, 0, 0, null, 120030005140, now(), now(), '1', '1', 0);
# 暂存间管理
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030008000001, '物料入库', '120030008000001', 120030008, 0, 0, 0, null, 120030008110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030008000002, '物料退库', '120030008000002', 120030008, 0, 0, 0, null, 120030008120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030008000003, '批次查看', '120030008000003', 120030008, 0, 0, 0, null, 120030008130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030008000004, '物料出库', '120030008000004', 120030008, 0, 0, 0, null, 120030008140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030008000005, '物料移库', '120030008000005', 120030008, 0, 0, 0, null, 120030008150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030008000006, '物料件查看', '120030008000006', 120030008, 0, 0, 0, null, 120030008160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120030008000007, '盘点', '120030008000007', 120030008, 0, 0, 0, null, 120030008170, now(), now(), '1', '1', 0);
# 数据集管理
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040001000001, '新增数据集', '120040001000001', 120040001, 0, 0, 0, null, 120040001110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040001000002, '编辑', '120040001000002', 120040001, 0, 0, 0, null, 120040001120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040001000003, '确认', '120040001000003', 120040001, 0, 0, 0, null, 120040001130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040001000004, '升级版本', '120040001000004', 120040001, 0, 0, 0, null, 120040001140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040001000005, '详情', '120040001000005', 120040001, 0, 0, 0, null, 120040001150, now(), now(), '1', '1', 0);
# 批签发模板
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040002000001, '新增模板', '120040002000001', 120040002, 0, 0, 0, null, 120040002110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040002000002, '编辑', '120040002000002', 120040002, 0, 0, 0, null, 120040002120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040002000003, '确定', '120040002000003', 120040002, 0, 0, 0, null, 120040002130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040002000004, '升级版本', '120040002000004', 120040002, 0, 0, 0, null, 120040002140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040002000005, '详情', '120040002000005', 120040002, 0, 0, 0, null, 120040002150, now(), now(), '1', '1', 0);
# 批签发管理
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040003000001, '批签发生成', '120040003000001', 120040003, 0, 0, 0, null, 120040003110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040003000002, '批签发详情', '120040003000002', 120040003, 0, 0, 0, null, 120040003120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040003000003, '提交', '120040003000003', 120040003, 0, 0, 0, null, 120040003130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040003000004, '重新生成', '120040003000004', 120040003, 0, 0, 0, null, 120040003140, now(), now(), '1', '1', 0);
# 批签发审核
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040005000001, '处理', '120040005000001', 120040005, 0, 0, 0, null, 120040005110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040005000002, '详情', '120040005000002', 120040005, 0, 0, 0, null, 120040005120, now(), now(), '1', '1', 0);
# 批签发文件
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040006000001, '查看报表', '120040006000001', 120040006, 0, 0, 0, null, 120040006110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120040006000002, '导出', '120040006000002', 120040006, 0, 0, 0, null, 120040006120, now(), now(), '1', '1', 0);
# 生产历史
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120050001000001, '预览打印', '120050001000001', 120050001, 0, 0, 0, null, 120050001110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120050001000002, '操作历史', '120050001000002', 120050001, 0, 0, 0, null, 120050001120, now(), now(), '1', '1', 0);
# 批记录打印
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120050002000001, '关联批次', '120050002000001', 120050002, 0, 0, 0, null, 120050002110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120050002000002, '操作历史', '120050002000002', 120050002, 0, 0, 0, null, 120050002120, now(), now(), '1', '1', 0);
# 物料日志
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120050003000001, '查看检验详情', '120050003000001', 120050003, 0, 0, 0, null, 120050003110, now(), now(), '1', '1', 0);

# 增加系统信息菜单 by lilong 240319
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (100020010, '系统信息', '100020010', 100020, 0, 1, 0, null, 110120101, now(), now(), '1', '1', 0, null);


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

# 补充原辅包信息分类的权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010001000007, '新增原辅包分类', '120010001000007', 120010001, 0, 0, 0, null, 120010001101, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010001000008, '编辑原辅包分类', '120010001000008', 120010001, 0, 0, 0, null, 120010001102, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010001000009, '删除原辅包分类', '120010001000009', 120010001, 0, 0, 0, null, 120010001103, now(), now(), '1', '1', 0);
# 补充中间品信息分类的权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010002000007, '新增中间品分类', '120010002000007', 120010002, 0, 0, 0, null, 120010002101, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010002000008, '编辑中间品分类', '120010002000008', 120010002, 0, 0, 0, null, 120010002102, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010002000009, '删除中间品分类', '120010002000009', 120010002, 0, 0, 0, null, 120010002103, now(), now(), '1', '1', 0);
