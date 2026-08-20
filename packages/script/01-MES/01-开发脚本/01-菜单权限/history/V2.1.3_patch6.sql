# 调整清场QA复核人员为复核人员 by lilong 241211
UPDATE bmos_platform.bp_menu SET name = '清场-复核人员签名' WHERE id = 121010001002023;

# 操作规程增加直接生效的按钮 by lilong 241211
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000015, '直接生效', '120020011000015', 120020011, 0, 0, 0, null, 120020011150, now(), now(), '1', '1', 0, null);

# 快捷录入按钮权限 by lilong 241216
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001018, '快捷录入', '121010001001018', 121010001001, 1, 0, 0, null, 121010001001280, now(), now(), '1', '1', 0, null);

UPDATE bmos_platform.bp_menu SET name = '清场-复核人签名' WHERE id = 121010001002023;

# 工艺配置的确认版本可以重新编辑 by lilong 241112
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020006000016, '重新编辑', '120020006000016', 120020006, 0, 0, 0, null, 120020006270, now(), now(), '1', '1', 0, null);
