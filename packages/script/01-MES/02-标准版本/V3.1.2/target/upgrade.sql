-- =============================================
-- 按钮权限配置 - 检验方案
-- 描述:检验方案菜单下的作废按钮权限配置
-- 创建时间:2026-01-28
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010008000009, '作废', '130010008000009', 130010008, 0, 0, 0, NULL, 130010008090, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);


-- =============================================
-- 一级菜单配置 - 留样管理
-- 描述:留样管理模块一级菜单（位于检验管理和报告管理之间）
-- 创建时间:2026-02-09
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130050, '留样管理', '130050', 130, 0, 1, 0, NULL, 150125, NOW(), NOW(), '1', '1', 0, NULL, 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTgiIGhlaWdodD0iMTgiIHZpZXdCb3g9IjAgMCAxOCAxOCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cGF0aCBkPSJNMyAxMkMzIDEyLjgyODQgMy42NzE1NyAxMy41IDQuNSAxMy41SDYuNUM3LjMyODQzIDEzLjUgOCAxMi44Mjg0IDggMTJWNi41SDEwVjEyQzEwIDEyLjgyODQgMTAuNjcxNiAxMy41IDExLjUgMTMuNUgxMy41QzE0LjMyODQgMTMuNSAxNSAxMi44Mjg0IDE1IDEyVjYuNUgxNkMxNi41NTIzIDYuNSAxNyA2Ljk0NzcyIDE3IDcuNVYxNS41QzE3IDE2LjA1MjMgMTYuNTUyMyAxNi41IDE2IDE2LjVIMkMxLjQ0NzcyIDE2LjUgMSAxNi4wNTIzIDEgMTUuNVY3LjVDMSA2Ljk0NzcyIDEuNDQ3NzIgNi41IDIgNi41SDNWMTJaTTcuNSAxLjVDNy43NzYxNCAxLjUgOCAxLjcyMzg2IDggMkM4IDIuMjc2MTQgNy43NzYxNCAyLjUgNy41IDIuNUg3VjExQzcgMTEuODI4NCA2LjMyODQzIDEyLjUgNS41IDEyLjVDNC42NzE1NyAxMi41IDQgMTEuODI4NCA0IDExVjIuNUgzLjVDMy4yMjM4NiAyLjUgMyAyLjI3NjE0IDMgMkMzIDEuNzIzODYgMy4yMjM4NiAxLjUgMy41IDEuNUg3LjVaTTE0IDExQzE0IDExLjgyODQgMTMuMzI4NCAxMi41IDEyLjUgMTIuNUMxMS42NzE2IDEyLjUgMTEgMTEuODI4NCAxMSAxMVYyLjVIMTAuNUMxMC4yMjM5IDIuNSAxMCAyLjI3NjE0IDEwIDJDMTAgMS43MjM4NiAxMC4yMjM5IDEuNSAxMC41IDEuNUgxNC41QzE0Ljc3NjEgMS41IDE1IDEuNzIzODYgMTUgMkMxNSAyLjI3NjE0IDE0Ljc3NjEgMi41IDE0LjUgMi41SDE0VjExWiIgZmlsbD0id2hpdGUiLz48L3N2Zz4K') ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);

-- =============================================
-- 二级菜单配置 - 留样管理子菜单
-- 描述:留样管理模块下的子菜单
-- 创建时间:2026-02-09
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130050001, '留样接收', '130050001', 130050, 0, 1, 0, NULL, 150125010, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130050002, '留样样品管理', '130050002', 130050, 0, 1, 0, NULL, 150125020, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130050003, '留样观察', '130050003', 130050, 0, 1, 0, NULL, 150125030, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130050004, '留样接收台账', '130050004', 130050, 0, 1, 0, NULL, 150125040, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130050005, '留样观察台账', '130050005', 130050, 0, 1, 0, NULL, 150125050, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130050006, '留样领用台账', '130050006', 130050, 0, 1, 0, NULL, 150125060, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130050007, '留样销毁台账', '130050007', 130050, 0, 1, 0, NULL, 150125070, NOW(), NOW(), '1', '1', 0, NULL, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias), icon = VALUES(icon);

-- =============================================
-- 按钮权限配置 - 留样接收
-- 描述:留样接收菜单下的按钮权限配置
-- 创建时间:2026-02-09
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130050001000001, '接收', '130050001000001', 130050001, 0, 0, 0, NULL, 130050001010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130050001000002, '批量接收', '130050001000002', 130050001, 0, 0, 0, NULL, 130050001020, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 留样样品管理
-- 描述:留样样品管理菜单下的按钮权限配置
-- 创建时间:2026-02-09
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130050002000001, '延期', '130050002000001', 130050002, 0, 0, 0, NULL, 130050002010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130050002000002, '领用', '130050002000002', 130050002, 0, 0, 0, NULL, 130050002020, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130050002000003, '销毁', '130050002000003', 130050002, 0, 0, 0, NULL, 130050002030, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130050002000004, '批量销毁', '130050002000004', 130050002, 0, 0, 0, NULL, 130050002040, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130050002000005, '标签补打', '130050002000005', 130050002, 0, 0, 0, NULL, 130050002050, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130050002000006, '历史', '130050002000006', 130050002, 0, 0, 0, NULL, 130050002060, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 留样观察
-- 描述:留样观察菜单下的按钮权限配置
-- 创建时间:2026-02-09
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130050003000001, '观察', '130050003000001', 130050003, 0, 0, 0, NULL, 130050003010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130050003000002, '批量观察', '130050003000002', 130050003, 0, 0, 0, NULL, 130050003020, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 留样接收台账
-- 描述:留样接收台账菜单下的按钮权限配置
-- 创建时间:2026-02-09
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130050004000001, '导出', '130050004000001', 130050004, 0, 0, 0, NULL, 130050004010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 留样观察台账
-- 描述:留样观察台账菜单下的按钮权限配置
-- 创建时间:2026-02-09
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130050005000001, '导出', '130050005000001', 130050005, 0, 0, 0, NULL, 130050005010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 留样领用台账
-- 描述:留样领用台账菜单下的按钮权限配置
-- 创建时间:2026-02-09
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130050006000001, '导出', '130050006000001', 130050006, 0, 0, 0, NULL, 130050006010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);

-- =============================================
-- 按钮权限配置 - 留样销毁台账
-- 描述:留样销毁台账菜单下的按钮权限配置
-- 创建时间:2026-02-09
-- =============================================
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130050007000001, '导出', '130050007000001', 130050007, 0, 0, 0, NULL, 130050007010, NOW(), NOW(), '1', '1', 0, NULL) ON DUPLICATE KEY UPDATE name = VALUES(name), code = VALUES(code), parent_id = VALUES(parent_id), terminal_type = VALUES(terminal_type), is_menu = VALUES(is_menu), is_outside = VALUES(is_outside), outside_url = VALUES(outside_url), sort = VALUES(sort), update_time = NOW(), update_by = VALUES(update_by), alias = VALUES(alias);
INSERT INTO bmos_platform.bp_tag_scene (id, tag_scene_name, tag_scene_desc, tag_type_id, data_source_service_name,
                                        data_source_interface, qr_code_field, sort, create_time, update_time, create_by,
                                        update_by, is_deleted)
VALUES (130020003, '留样样品信息打印标签', '留样样品信息打印标签', 7, 'bmos-lims2-service', '/api/app/lims2/sample/retention/printTag',
        'sampleNo', 7110, '2025-09-22 10:35:08', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003001, 130020003, 'materialName', '检品名称', 'String', '氯化钠', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003002, 130020003, 'materialCode', '检品编码', 'String', '0001', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003003, 130020003, 'fullMaterialName', '检品信息', 'String', '0001-氯化钠', '2025-09-22 11:39:28', null, null,
        null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003005, 130020003, 'materialSpec', '检品规格', 'String', '500g/袋', '2025-09-22 11:39:28', null, null, null,
        0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003006, 130020003, 'batchNo', '批号', 'String', '20250901', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003007, 130020003, 'sampleNo', '样品编号', 'String', 'Sample20250901', '2025-09-22 11:39:28', null, null,
        null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003012, 130020003, 'quantityWithUnit', '样品数量', 'String', '100g', '2025-09-22 11:39:28', null, null, null,
        0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003014, 130020003, 'samplingTime', '取样时间', 'String', '2025-09-22 11:39:28', '2025-09-22 11:39:28', null,
        null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003015, 130020003, 'retentionUserName', '留样人', 'String', '2025-09-22 11:39:28', '2025-09-22 11:39:28', null,
        null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003016, 130020003, 'retentionTime', '留样时间', 'String', '2025-09-22 11:39:28', '2025-09-22 11:39:28', null,
        null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003017, 130020003, 'retentionExpiryDate', '留样期限', 'String', '2025-09-22 11:39:28', '2025-09-22 11:39:28', null,
        null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020003018, 130020003, 'storageLocation', '储存位置', 'String', '2025-09-22 11:39:28', '2025-09-22 11:39:28', null,
        null, null, 0);


INSERT INTO bmos_platform.bp_tag_instance (id, tag_name, tag_type_id, tag_scene_id, tag_define_id, config_fields,
                                           is_enable, create_time, update_time, create_by, update_by, is_deleted)
VALUES (130020003, '留样样品信息打印标签', 7, 130020003, 2,
        '[{"label":"","defineField":"field1","dataSourceField":"fullMaterialName","consumeValue":null},{"label":"","defineField":"field2","dataSourceField":"quantityWithUnit","consumeValue":null},{"label":"","defineField":"field3","dataSourceField":"retentionTime","consumeValue":null},{"label":"","defineField":"field4","dataSourceField":"retentionExpiryDate","consumeValue":null}]',
        'TRUE', '2025-09-22 11:19:01', '2025-09-22 14:22:24', null, '888888888888888873', 0);



