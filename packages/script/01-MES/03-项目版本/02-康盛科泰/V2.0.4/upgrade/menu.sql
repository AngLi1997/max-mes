# 新增WMS菜单 by lilong 240320
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150, '仓储管理系统', '150', 0, 0, 1, 0, null, 170, now(), now(), '1', '1', 0, 'BM-WMS');
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150010, '仓库配置', '150010', 150, 0, 1, 0, null, 170110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150020, '仓库管理', '150020', 150, 0, 1, 0, null, 170120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150030, '仓库查询', '150030', 150, 0, 1, 0, null, 170130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150010001, '货品名称', '150010001', 150010, 0, 1, 0, null, 170110110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150010002, '货位配置', '150010002', 150010, 0, 1, 0, null, 170110120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150020001, '库存管理', '150020001', 150020, 0, 1, 0, null, 170120110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150020002, '货品管理', '150020002', 150020, 0, 1, 0, null, 170120120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150020003, '仓库发料', '150020003', 150020, 0, 1, 0, null, 170120130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150030001, '货品日志', '150030001', 150030, 0, 1, 0, null, 170130110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150030002, '货位日志', '150030002', 150030, 0, 1, 0, null, 170130120, now(), now(), '1', '1', 0, null);

# 调整货品信息菜单名称 by lilong 240320
UPDATE bmos_platform.bp_menu SET name = '货品信息' WHERE id = 150010001;

# 货品信息权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010001000001, '新增货品分类', '150010001000001', 150010001, 0, 0, 0, null, 150010001110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010001000002, '编辑货品分类', '150010001000002', 150010001, 0, 0, 0, null, 150010001120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010001000003, '删除货品分类', '150010001000003', 150010001, 0, 0, 0, null, 150010001130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010001000004, '新增货品', '150010001000004', 150010001, 0, 0, 0, null, 150010001140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010001000005, '同步', '150010001000005', 150010001, 0, 0, 0, null, 150010001150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010001000006, '查看', '150010001000006', 150010001, 0, 0, 0, null, 150010001160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010001000007, '编辑', '150010001000007', 150010001, 0, 0, 0, null, 150010001170, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010001000008, '删除', '150010001000008', 150010001, 0, 0, 0, null, 150010001180, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010001000009, '启停', '150010001000009', 150010001, 0, 0, 0, null, 150010001190, now(), now(), '1', '1', 0);
# 货位配置权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010002000001, '新增区域', '150010002000001', 150010002, 0, 0, 0, null, 150010002110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010002000002, '编辑区域', '150010002000002', 150010002, 0, 0, 0, null, 150010002120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010002000003, '删除区域', '150010002000003', 150010002, 0, 0, 0, null, 150010002130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010002000004, '新增货位', '150010002000004', 150010002, 0, 0, 0, null, 150010002140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010002000005, '查看', '150010002000005', 150010002, 0, 0, 0, null, 150010002150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010002000006, '编辑', '150010002000006', 150010002, 0, 0, 0, null, 150010002160, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010002000007, '数据权限', '150010002000007', 150010002, 0, 0, 0, null, 150010002170, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010002000008, '删除', '150010002000008', 150010002, 0, 0, 0, null, 150010002180, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150010002000009, '启停', '150010002000009', 150010002, 0, 0, 0, null, 150010002190, now(), now(), '1', '1', 0);
# 库存管理权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150020001000001, '货品接收', '150020001000001', 150020001, 0, 0, 0, null, 150020001110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150020001000002, '查看货品批次', '150020001000002', 150020001, 0, 0, 0, null, 150020001120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150020001000003, '货品出库', '150020001000003', 150020001, 0, 0, 0, null, 150020001130, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150020001000004, '货品移库', '150020001000004', 150020001, 0, 0, 0, null, 150020001140, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150020001000005, '查看货品件', '150020001000005', 150020001, 0, 0, 0, null, 150020001150, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150020001000006, '盘点', '150020001000006', 150020001, 0, 0, 0, null, 150020001160, now(), now(), '1', '1', 0);
# 货品管理权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150020002000001, '查看货品件', '150020002000001', 150020002, 0, 0, 0, null, 150020002110, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150020002000002, '编辑批次', '150020002000002', 150020002, 0, 0, 0, null, 150020002120, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150020002000003, '查看批次', '150020002000003', 150020002, 0, 0, 0, null, 150020002130, now(), now(), '1', '1', 0);
# 仓库发料权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150020003000001, '发料', '150020003000001', 150020003, 0, 0, 0, null, 150020003110, now(), now(), '1', '1', 0);
# 货品日志权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (150030001000001, '查看检验详情', '150030001000001', 150030001, 0, 0, 0, null, 150030001110, now(), now(), '1', '1', 0);

# 补充原辅包信息分类的权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010001000007, '新增原辅包分类', '120010001000007', 120010001, 0, 0, 0, null, 120010001101, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010001000008, '编辑原辅包分类', '120010001000008', 120010001, 0, 0, 0, null, 120010001102, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010001000009, '删除原辅包分类', '120010001000009', 120010001, 0, 0, 0, null, 120010001103, now(), now(), '1', '1', 0);
# 补充中间品信息分类的权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010002000007, '新增中间品分类', '120010002000007', 120010002, 0, 0, 0, null, 120010002101, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010002000008, '编辑中间品分类', '120010002000008', 120010002, 0, 0, 0, null, 120010002102, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010002000009, '删除中间品分类', '120010002000009', 120010002, 0, 0, 0, null, 120010002103, now(), now(), '1', '1', 0);
# 补充产品信息分类的权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010003000008, '新增产品分类', '120010003000008', 120010003, 0, 0, 0, null, 120010003101, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010003000009, '编辑产品分类', '120010003000009', 120010003, 0, 0, 0, null, 120010003102, now(), now(), '1', '1', 0);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (120010003000010, '删除产品分类', '120010003000010', 120010003, 0, 0, 0, null, 120010003103, now(), now(), '1', '1', 0);

# 用户管理修改密码按钮 by lilong 240321
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (100030001000011, '修改密码', '100030001000011', 100030001, 0, 0, 0, null, 110130110200, now(), now(),'1', '1', 0, null);

# 康盛科泰-批次审批菜单权限 by lilong 240403
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030009, '批次审批', '120030009', 120030, 0, 1, 0, null, 130130190, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050007, '批次审批查询', '120050007', 120050, 0, 1, 0, null, 130150121, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030009000001, '审批结论', '120030009000001', 120030009, 0, 0, 0, null, 120030009110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030009000002, '工序审批', '120030009000002', 120030009, 0, 0, 0, null, 120030009120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050007000001, '报表查看', '120050007000001', 120050007, 0, 0, 0, null, 120050007110, now(), now(), '1', '1', 0, null);

# 增加查看审批进度的权限 by lilong 240403
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020001000014, '审批进度', '120020001000014', 120020001, 0, 0, 0, null, 120020001221, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020002000002, '审批进度', '120020002000002', 120020002, 0, 0, 0, null, 120020002120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020004000009, '审批进度', '120020004000009', 120020004, 0, 0, 0, null, 120020004190, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020005000002, '审批进度', '120020005000002', 120020005, 0, 0, 0, null, 120020005120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020006000012, '审批进度', '120020006000012', 120020006, 0, 0, 0, null, 120020006220, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020007000002, '审批进度', '120020007000002', 120020007, 0, 0, 0, null, 120020007120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030001000007, '审批进度', '120030001000007', 120030001, 0, 0, 0, null, 120030001151, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030002000002, '审批进度', '120030002000002', 120030002, 0, 0, 0, null, 120030002120, now(), now(), '1', '1', 0, null);
# 增加批签发管理历史按钮权限 by lilong 240403
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120040003000005, '历史', '120040003000005', 120040003, 0, 0, 0, null, 120040003150, now(), now(), '1', '1', 0, null);

# 调整库存管理和货品管理的菜单权限 by lilong 240403
UPDATE bmos_platform.bp_menu SET name = '货品入库' WHERE id = 150020001000001;
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150020002000004, '新增批次', '150020002000004', 150020002, 0, 0, 0, null, 150020002140, now(), now(), '1', '1', 0, null);

# 增加库存管理签名权限 by lilong 240403
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150020001000007, '货品入库递交人权限', '150020001000007', 150020001, 0, 0, 0, null, 150020001170, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150020001000008, '货品出库领用人权限', '150020001000008', 150020001, 0, 0, 0, null, 150020001180, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150020001000009, '货品盘点复核人权限', '150020001000009', 150020001, 0, 0, 0, null, 150020001190, now(), now(), '1', '1', 0, null);
# 增加仓库发料复核人签名权限 by lilong 240409
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150020003000002, '仓库发料复核人权限', '150020003000002', 150020003, 0, 0, 0, null, 150020003120, now(), now(), '1', '1', 0, null);
# 增加生产进度和称量日志的菜单 by lilong 240415
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050005, '称量日志', '120050005', 120050, 0, 1, 0, null, 130150150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050008, '生产进度', '120050008', 120050, 0, 1, 0, null, 130150101, now(), now(), '1', '1', 0, null);

# 增加设备管理的菜单 by zhangziyang 240419
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160, '设备管理系统', '160', 0, 0, 1, 0, null, 180, now(), now(), '1', '1', 0, 'BM-EMS');
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010, '设备管理', '160010', 160, 0, 1, 0, null, 180110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160020, '设备日志', '160020', 160, 0, 1, 0, null, 180120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001, '工厂建模', '160010001', 160010, 0, 1, 0, null, 160010110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010002, '设备管理', '160010002', 160010, 0, 1, 0, null, 160010120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010003, '采集点管理', '160010003', 160010, 0, 1, 0, null, 160010130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160020001, '状态变更日志', '160020001', 160020, 0, 1, 0, null, 160020110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121030, '设备管理', '121030', 121, 1, 1, 0, null, 140130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121030001, '设备状态', '121030001', 121030, 1, 1, 0, null, 140130110, now(), now(), '1', '1', 0, null);

# 增加移动端生产历史菜单 by lilong 240419
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010003, '生产历史', '121010003', 121010, 1, 1, 0, null, 140110130, now(), now(), '1', '1', 0, null);

# 增加物料管理菜单 by lilong 240419
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030007, '物料管理', '120030007', 120030, 0, 1, 0, null, 130130170, now(), now(),  '1', '1', 0, null);

# 增加设备使用日志菜单 by zhangziyang 240422
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160020002, '设备使用日志', '160020002', 160020, 0, 1, 0, null, 160020101, now(), now(), '1', '1', 0, null);

# 移动端生产执行和业务组件权限配置
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001, '生产执行', '121010001', 121010001, 1, 0, 0, null, 121010001010110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002, '业务组件', '121010001', 121010001, 1, 0, 0, null, 121010001010120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002001, '领料计划-计划人签名', '121010001002001', 121010001002, 1, 0, 0, null, 121010001010120110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002002, '领料接收-递交人签名', '121010001002002', 121010001002, 1, 0, 0, null, 121010001010120120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002003, '配料计划-计划人签名', '121010001002003', 121010001002, 1, 0, 0, null, 121010001010120130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002004, '配料称量-复核人签名', '121010001002004', 121010001002, 1, 0, 0, null, 121010001010120140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002005, '配料称量-直接完成配料称量签名', '121010001002005', 121010001002, 1, 0, 0, null, 121010001010120150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002006, '配料称量-直接完成余料称量签名', '121010001002006', 121010001002, 1, 0, 0, null, 121010001010120160, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002007, '配料称量-余料称量超目标量签名', '121010001002007', 121010001002, 1, 0, 0, null, 121010001010120170, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002008, '配料投入-投料人签名', '121010001002008', 121010001002, 1, 0, 0, null, 121010001010120180, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002009, '投料/回收-投料人签名', '121010001002009', 121010001002, 1, 0, 0, null, 121010001010120190, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002010, '投料/回收-回收人签名', '121010001002010', 121010001002, 1, 0, 0, null, 121010001010120200, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002011, '产出称量-复核人签名', '121010001002011', 121010001002, 1, 0, 0, null, 121010001010120210, now(), now(), '1', '1', 0, null);

# 补充物料管理、生产进度权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030001000008, '历史', '120030001000008', 120030001, 0, 0, 0, null, 120030001170, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030007000001, '新增物料批次', '120030007000001', 120030007, 0, 0, 0, null, 120030007110, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030007000002, '编辑物料批次', '120030007000002', 120030007, 0, 0, 0, null, 120030007120, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030007000003, '查看物料批次', '120030007000003', 120030007, 0, 0, 0, null, 120030007130, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030007000004, '新增物料件', '120030007000004', 120030007, 0, 0, 0, null, 120030007140, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030007000005, '查看物料件', '120030007000005', 120030007, 0, 0, 0, null, 120030007150, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120050008000001, '生产进度', '120050008000001', 120050008, 0, 0, 0, null, 120050008110, now(),now(), '1', '1', 0, null);
# 删除无用权限
DELETE FROM bmos_platform.bp_menu WHERE id IN ('100030001000002','100030001000003','120010001000008','120010002000008','120010003000009','150010001000002');

# 工位管理权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001000001, '新增部门', '160010001000001', 160010001, 0, 0, 0, null, 160010001110, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001000002, '编辑部门', '160010001000002', 160010001, 0, 0, 0, null, 160010001120, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001000003, '删除部门', '160010001000003', 160010001, 0, 0, 0, null, 160010001130, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001000004, '新增工位', '160010001000004', 160010001, 0, 0, 0, null, 160010001140, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001000005, '编辑工位', '160010001000005', 160010001, 0, 0, 0, null, 160010001150, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001000006, '查看工位', '160010001000006', 160010001, 0, 0, 0, null, 160010001160, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001000007, '绑定设备', '160010001000007', 160010001, 0, 0, 0, null, 160010001170, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001000008, '绑定人员', '160010001000008', 160010001, 0, 0, 0, null, 160010001180, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001000009, '删除工位', '160010001000009', 160010001, 0, 0, 0, null, 160010001190, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010001000010, '启停', '160010001000010', 160010001, 0, 0, 0, null, 160010001200, now(),now(), '1', '1', 0, null);
# 设备管理权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010002000001, '新增分类', '160010002000001', 160010002, 0, 0, 0, null, 160010002110, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010002000002, '编辑分类', '160010002000002', 160010002, 0, 0, 0, null, 160010002120, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010002000003, '删除分类', '160010002000003', 160010002, 0, 0, 0, null, 160010002130, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010002000004, '新增设备', '160010002000004', 160010002, 0, 0, 0, null, 160010002140, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010002000005, '编辑设备', '160010002000005', 160010002, 0, 0, 0, null, 160010002150, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010002000006, '查看设备', '160010002000006', 160010002, 0, 0, 0, null, 160010002160, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010002000007, '打印标签', '160010002000007', 160010002, 0, 0, 0, null, 160010002170, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010002000008, '使用记录', '160010002000008', 160010002, 0, 0, 0, null, 160010002180, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010002000009, '删除设备', '160010002000009', 160010002, 0, 0, 0, null, 160010002190, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010002000010, '启停', '160010002000010', 160010002, 0, 0, 0, null, 160010002200, now(),now(), '1', '1', 0, null);
# 采集点管理权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010003000001, '新增采集点', '160010003000001', 160010003, 0, 0, 0, null, 160010003110, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010003000002, '编辑采集点', '160010003000002', 160010003, 0, 0, 0, null, 160010003120, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010003000003, '查看详情', '160010003000003', 160010003, 0, 0, 0, null, 160010003130, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010003000004, '删除采集点', '160010003000004', 160010003, 0, 0, 0, null, 160010003140, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010003000005, '导入', '160010003000005', 160010003, 0, 0, 0, null, 160010003150, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010003000006, '导出', '160010003000006', 160010003, 0, 0, 0, null, 160010003160, now(),now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160010003000007, '启停', '160010003000007', 160010003, 0, 0, 0, null, 160010003170, now(),now(), '1', '1', 0, null);
# 状态变更日志
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160020001000001, '导出', '160020001000001', 160020001, 0, 0, 0, null, 160020001110, now(),now(), '1', '1', 0, null);
# 设备使用日志
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (160020002000001, '导出', '160020002000001', 160020002, 0, 0, 0, null, 160020002110, now(),now(), '1', '1', 0, null);

# 生产执行权限 by lilong 240430
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001001, '暂停生产', '121010001001001', 121010001001, 1, 0, 0, null, 121010001001110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001002, '恢复生产', '121010001001002', 121010001001, 1, 0, 0, null, 121010001001120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001003, '终止生产', '121010001001003', 121010001001, 1, 0, 0, null, 121010001001130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001004, '保存', '121010001001004', 121010001001, 1, 0, 0, null, 121010001001140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001005, '完成步骤', '121010001001005', 121010001001, 1, 0, 0, null, 121010001001150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001006, '拍照取证', '121010001001006', 121010001001, 1, 0, 0, null, 121010001001160, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001007, '复制记录', '121010001001007', 121010001001, 1, 0, 0, null, 121010001001170, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001008, '记录作废', '121010001001008', 121010001001, 1, 0, 0, null, 121010001001180, now(), now(), '1', '1', 0, null);

# 屏蔽配料投入的投料人、投料/回收的投料人和回收人权限 by lilong 240513
DELETE FROM bmos_platform.bp_menu WHERE id IN (
'121010001002008',
'121010001002009',
'121010001002010'
    );

# 调整工厂建模权限名称 by lilong 240515
UPDATE bmos_platform.bp_menu SET name = '新增模型' WHERE id = 160010001000001;
UPDATE bmos_platform.bp_menu SET name = '编辑模型' WHERE id = 160010001000002;
UPDATE bmos_platform.bp_menu SET name = '删除模型' WHERE id = 160010001000003;

# 补充仓库取消发料的按钮权限 by lilong 240523
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (150020003000003, '取消发料', '150020003000003', 150020003, 0, 0, 0, null, 150020003130, now(), now(), '1', '1', 0, null);

# 称量组件增加称量模式的按钮权限 by lilong 240523
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002012, '配料称量-配料称量模式', '121010001002012', 121010001002, 1, 0, 0, null, 121010001010120131, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002013, '配料称量-手工称量模式', '121010001002013', 121010001002, 1, 0, 0, null, 121010001010120132, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002014, '产出称量-产出称量模式', '121010001002014', 121010001002, 1, 0, 0, null, 121010001010120201, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001002015, '产出称量-手工称量模式', '121010001002015', 121010001002, 1, 0, 0, null, 121010001010120202, now(), now(), '1', '1', 0, null);

# 班组管理数据权限按钮 by lilong 240612
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120030005000005, '数据权限', '120030005000005', 120030005, 0, 0, 0, null, 120030005150, now(), now(), '1', '1', 0, null);

DELETE FROM bmos_platform.bp_menu WHERE id IN (
'130',
'150',
'160',
'120040',
'121020',
'121030',
'130010',
'130020',
'150010',
'150020',
'150030',
'160010',
'160020',
'100020007',
'100020010',
'120020010',
'120030007',
'120030008',
'120040001',
'120040002',
'120040003',
'120040005',
'120040006',
'120050003',
'120050004',
'120050005',
'121020002',
'130010001',
'130010002',
'130010003',
'130010004',
'130020001',
'130020002',
'130020003',
'130020004',
'130020005',
'130020006',
'130020007',
'150010001',
'150010002',
'150020001',
'150020002',
'150020003',
'150030001',
'150030002',
'160010001',
'160010002',
'160010003',
'160020001',
'160020002',
'121010001002'
  );


DELETE FROM bmos_platform.bp_menu WHERE bp_menu.parent_id IN (
'130',
'150',
'160',
'120040',
'121020',
'121030',
'130010',
'130020',
'150010',
'150020',
'150030',
'160010',
'160020',
'100020007',
'100020010',
'120020010',
'120030007',
'120030008',
'120040001',
'120040002',
'120040003',
'120040005',
'120040006',
'120050003',
'120050004',
'120050005',
'121020002',
'130010001',
'130010002',
'130010003',
'130010004',
'130020001',
'130020002',
'130020003',
'130020004',
'130020005',
'130020006',
'130020007',
'150010001',
'150010002',
'150020001',
'150020002',
'150020003',
'150030001',
'150030002',
'160010001',
'160010002',
'160010003',
'160020001',
'160020002',
'121010001002'
  );


