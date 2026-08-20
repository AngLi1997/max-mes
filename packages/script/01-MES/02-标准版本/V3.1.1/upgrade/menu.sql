-- Active: 1756983401811@@172.30.1.160@3306@bmos_platform

-- =============================================
-- 调整已有菜单顺序
-- 描述:更新数据库中已存在菜单的sort值以调整顺序
-- 创建时间:2024-10-29
-- =============================================
UPDATE bmos_platform.bp_menu SET sort = 150110010, update_time = NOW() WHERE id = 130010001;
UPDATE bmos_platform.bp_menu SET sort = 150110020, update_time = NOW() WHERE id = 130010002;
UPDATE bmos_platform.bp_menu SET sort = 150110030, update_time = NOW() WHERE id = 130010003;
UPDATE bmos_platform.bp_menu SET sort = 150110040, update_time = NOW() WHERE id = 130010004;
UPDATE bmos_platform.bp_menu SET sort = 150110050, update_time = NOW() WHERE id = 130010005;
UPDATE bmos_platform.bp_menu SET sort = 150110060, update_time = NOW() WHERE id = 130010006;
UPDATE bmos_platform.bp_menu SET sort = 150110070, update_time = NOW() WHERE id = 130010007;
UPDATE bmos_platform.bp_menu SET sort = 150110100, update_time = NOW() WHERE id = 130010008;
UPDATE bmos_platform.bp_menu SET sort = 150110110, update_time = NOW() WHERE id = 130010009;
-- =============================================
-- 调整检验查询子菜单顺序
-- 描述:更新数据库中已存在菜单的sort值
-- 创建时间:2024-10-29
-- =============================================
UPDATE bmos_platform.bp_menu SET sort = 150140110, update_time = NOW() WHERE id = 130040001;
UPDATE bmos_platform.bp_menu SET sort = 150140130, update_time = NOW() WHERE id = 130040002;
UPDATE bmos_platform.bp_menu SET sort = 150140140, update_time = NOW() WHERE id = 130040003;
UPDATE bmos_platform.bp_menu SET sort = 150140150, update_time = NOW() WHERE id = 130040004;

-- =============================================
-- 菜单配置 - 方法管理
-- 描述:验证配置模块下的方法管理菜单
-- 创建时间:2024-10-22
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010010, '方法管理', '130010010', 130010, 0, 1, 0, NULL, 150110080, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);

-- =============================================
-- 菜单配置 - 方法审核
-- 描述:验证配置模块下的方法审核菜单
-- 创建时间:2024-10-22
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010011, '方法审核', '130010011', 130010, 0, 1, 0, NULL, 150110090, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);

-- =============================================
-- 菜单配置 - 操作规程
-- 描述:验证配置模块下的操作规程菜单
-- 创建时间:2024-10-29
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010012, '操作规程', '130010012', 130010, 0, 1, 0, NULL, 150110120, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);

-- =============================================
-- 菜单配置 - 操作规程审批
-- 描述:验证配置模块下的操作规程审批菜单
-- 创建时间:2024-10-29
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010013, '操作规程审批', '130010013', 130010, 0, 1, 0, NULL, 150110130, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);

-- =============================================
-- 业务参数配置 - 检验记录作业空值符号
-- 描述:用于配置检验记录作业中空值的显示符号
-- 创建时间:2024-10-22
-- =============================================
INSERT INTO bmos_platform.bp_business_parameter (`id`, `code`, `name`, `value`, `value_type`, `business_type`, `belong`, `description`, `sort`, `value_range`, `is_display`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (130002, 'lims.record.empty-data', '检验记录作业空值符号', 'N/A', 'STRING', 'BUSINESS', '检验', '检验记录作业空值符号', 130020, '', 1, '1', '1', NOW(), NOW(), 0) ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `value` = VALUES(`value`), `value_type` = VALUES(`value_type`), `business_type` = VALUES(`business_type`), `belong` = VALUES(`belong`), `description` = VALUES(`description`), `sort` = VALUES(`sort`), `value_range` = VALUES(`value_range`), `is_display` = VALUES(`is_display`), `update_by` = VALUES(`update_by`), `update_time` = NOW();

-- =============================================
-- 业务参数配置 - 方法管理记录页边距
-- 描述:方法管理记录页边距，单位：毫米
-- 创建时间:2024-10-22
-- =============================================
INSERT INTO bmos_platform.bp_business_parameter (`id`, `code`, `name`, `value`, `value_type`, `business_type`, `belong`, `description`, `sort`, `value_range`, `is_display`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (130003, 'lims.record.margin', '方法管理记录页边距', '{"left":"10", "right":"10", "top":"10", "bottom":"10"}', 'JSON', 'BUSINESS', '检验', '方法管理记录页边距，单位：毫米', 130030, '', 1, '1', '1', NOW(), NOW(), 0) ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `value` = VALUES(`value`), `value_type` = VALUES(`value_type`), `business_type` = VALUES(`business_type`), `belong` = VALUES(`belong`), `description` = VALUES(`description`), `sort` = VALUES(`sort`), `value_range` = VALUES(`value_range`), `is_display` = VALUES(`is_display`), `update_by` = VALUES(`update_by`), `update_time` = NOW();

-- =============================================
-- 业务参数配置 - 结论判定选项
-- 描述:用于配置检验结论判定的可选项
-- 创建时间:2024-10-22
-- =============================================
INSERT INTO bmos_platform.bp_business_parameter (`id`, `code`, `name`, `value`, `value_type`, `business_type`, `belong`, `description`, `sort`, `value_range`, `is_display`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (130001, 'lims.conclusion.options', '结论判定选项', '[{"label":"不符合规定","value":false},{"label":"符合规定","value":true}]', 'JSON', 'BUSINESS', '检验', '结论判定选项', 130010, '', 1, '1', '1', NOW(), NOW(), 0) ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `value` = VALUES(`value`), `value_type` = VALUES(`value_type`), `business_type` = VALUES(`business_type`), `belong` = VALUES(`belong`), `description` = VALUES(`description`), `sort` = VALUES(`sort`), `value_range` = VALUES(`value_range`), `is_display` = VALUES(`is_display`), `update_by` = VALUES(`update_by`), `update_time` = NOW();

-- =============================================
-- 菜单配置 - 记录打印
-- 描述:检验查询模块下的记录打印菜单（新增）
-- 创建时间:2024-10-22
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130040005, '记录打印', '130040005', 130040, 0, 1, 0, NULL, 150140120, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);


-- =============================================
-- 子菜单配置 - 方法管理功能按钮
-- 描述:方法管理下的功能按钮子菜单
-- 创建时间:2024-10-30
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010010001, '功能按钮', '130010010001', 130010010, 0, 0, 0, NULL, 130010010110, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010010002, '业务组件', '130010010002', 130010010, 0, 0, 0, NULL, 130010010120, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);

-- =============================================
-- 业务组件子菜单 - 仪器信息
-- 描述:业务组件下的仪器信息子菜单
-- 创建时间:2024-11-04
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010010002001, '仪器信息', '130010010002001', 130010010002, 0, 0, 0, NULL, 130010010121, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);

-- =============================================
-- 业务组件子菜单 - 仪器数采
-- 描述:业务组件下的仪器数采子菜单
-- 创建时间:2024-11-04
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010010002002, '仪器数采', '130010010002002', 130010010002, 0, 0, 0, NULL, 130010010122, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);

-- =============================================
-- 按钮权限配置 - 方法管理
-- 描述:方法管理菜单下的按钮权限配置
-- 创建时间:2024-10-22
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000001, '新增方法分类', '130010010000001', 130010010001, 0, 0, 0, NULL, 130010010010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000016, '编辑方法分类', '130010010000016', 130010010001, 0, 0, 0, NULL, 130010010011, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000002, '删除方法分类', '130010010000002', 130010010001, 0, 0, 0, NULL, 130010010020, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000003, '新增方法', '130010010000003', 130010010001, 0, 0, 0, NULL, 130010010030, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000004, '数据权限', '130010010000004', 130010010001, 0, 0, 0, NULL, 130010010040, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000005, '关联分析项', '130010010000005', 130010010001, 0, 0, 0, NULL, 130010010050, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000006, '绑定公式', '130010010000006', 130010010001, 0, 0, 0, NULL, 130010010060, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000017, '关联操作规程', '130010010000017', 130010010001, 0, 0, 0, NULL, 130010010065, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000007, '新增版本', '130010010000007', 130010010001, 0, 0, 0, NULL, 130010010070, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000008, '公式配置', '130010010000008', 130010010001, 0, 0, 0, NULL, 130010010080, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000009, '编辑', '130010010000009', 130010010001, 0, 0, 0, NULL, 130010010090, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000010, '审核', '130010010000010', 130010010001, 0, 0, 0, NULL, 130010010100, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000011, '历史', '130010010000011', 130010010001, 0, 0, 0, NULL, 130010010110, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000012, '查看', '130010010000012', 130010010001, 0, 0, 0, NULL, 130010010120, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000013, '审批进度', '130010010000013', 130010010001, 0, 0, 0, NULL, 130010010130, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000014, '启用', '130010010000014', 130010010001, 0, 0, 0, NULL, 130010010140, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010010000015, '停用', '130010010000015', 130010010001, 0, 0, 0, NULL, 130010010150, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 方法审核
-- 描述:方法审核菜单下的按钮权限配置
-- 创建时间:2024-10-22
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010011000001, '处理', '130010011000001', 130010011, 0, 0, 0, NULL, 130010011010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010011000002, '审批进度', '130010011000002', 130010011, 0, 0, 0, NULL, 130010011020, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 操作规程
-- 描述:操作规程菜单下的按钮权限配置
-- 创建时间:2024-10-29
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010012000001, '新增分类', '130010012000001', 130010012, 0, 0, 0, NULL, 130010012010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010012000014, '编辑分类', '130010012000014', 130010012, 0, 0, 0, NULL, 130010012015, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010012000002, '删除分类', '130010012000002', 130010012, 0, 0, 0, NULL, 130010012020, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010012000003, '新增文件', '130010012000003', 130010012, 0, 0, 0, NULL, 130010012030, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010012000004, '数据权限', '130010012000004', 130010012, 0, 0, 0, NULL, 130010012040, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010012000005, '关联方法', '130010012000005', 130010012, 0, 0, 0, NULL, 130010012050, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010012000006, '新增版本', '130010012000006', 130010012, 0, 0, 0, NULL, 130010012060, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010012000007, '查看', '130010012000007', 130010012, 0, 0, 0, NULL, 130010012070, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010012000008, '历史', '130010012000008', 130010012, 0, 0, 0, NULL, 130010012080, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010012000009, '编辑', '130010012000009', 130010012, 0, 0, 0, NULL, 130010012090, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010012000010, '审核', '130010012000010', 130010012, 0, 0, 0, NULL, 130010012100, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010012000011, '停用', '130010012000011', 130010012, 0, 0, 0, NULL, 130010012110, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010012000012, '启用', '130010012000012', 130010012, 0, 0, 0, NULL, 130010012120, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010012000013, '审核进度', '130010012000013', 130010012, 0, 0, 0, NULL, 130010012130, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 分析项管理
-- 描述:分析项管理菜单下的方法管理按钮
-- 创建时间:2025-12-23
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010002000004, '关联方法', '130010002000004', 130010002, 0, 0, 0, NULL, 130010002040, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 操作规程审批
-- 描述:操作规程审批菜单下的按钮权限配置
-- 创建时间:2024-10-29
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010013000001, '处理', '130010013000001', 130010013, 0, 0, 0, NULL, 130010013010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010013000002, '审核处理', '130010013000002', 130010013, 0, 0, 0, NULL, 130010013020, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 记录打印
-- 描述:记录打印菜单下的按钮权限配置
-- 创建时间:2024-10-22
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130040005000001, '查看', '130040005000001', 130040005, 0, 0, 0, NULL, 130040005010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130040005000002, '打印', '130040005000002', 130040005, 0, 0, 0, NULL, 130040005020, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130040005000003, '操作历史', '130040005000003', 130040005, 0, 0, 0, NULL, 130040005030, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 公式配置绑定方法
-- 描述:公式配置模块下的绑定方法按钮权限配置
-- 创建时间:2024-10-30
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (100020006000011, '绑定方法', '100020006000011', 100020006, 0, 0, 0, NULL, 110120160210, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 删除按钮权限 - 130030004000002
-- 描述:删除指定按钮权限
-- =============================================
DELETE FROM bmos_platform.bp_menu WHERE id = 130030004000002;
