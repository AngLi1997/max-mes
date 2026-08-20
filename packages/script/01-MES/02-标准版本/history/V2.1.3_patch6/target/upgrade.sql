# 调整清场QA复核人员为复核人员 by lilong 241211
UPDATE bmos_platform.bp_menu SET name = '清场-复核人员签名' WHERE id = 121010001002023;

# 操作规程增加直接生效的按钮 by lilong 241211
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020011000015, '直接生效', '120020011000015', 120020011, 0, 0, 0, null, 120020011150, now(), now(), '1', '1', 0, null);

# 快捷录入按钮权限 by lilong 241216
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (121010001001018, '快捷录入', '121010001001018', 121010001001, 1, 0, 0, null, 121010001001280, now(), now(), '1', '1', 0, null);

UPDATE bmos_platform.bp_menu SET name = '清场-复核人签名' WHERE id = 121010001002023;

# 工艺配置的确认版本可以重新编辑 by lilong 241112
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (120020006000016, '重新编辑', '120020006000016', 120020006, 0, 0, 0, null, 120020006270, now(), now(), '1', '1', 0, null);
# 调整设备标签字段 by lilong 241125
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"设备名称：","defineField":"field1","dataSourceField":"equipmentName","consumeValue":null},{"label":"设备编号：","defineField":"field2","dataSourceField":"equipmentCode","consumeValue":null},{"label":"打印时间：","defineField":"field3","dataSourceField":"printDate","consumeValue":null},{"label":null,"defineField":"field4","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field5","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field6","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field7","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field8","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field9","dataSourceField":null,"consumeValue":null}]' WHERE id = 160004001;
DELETE FROM bmos_platform.bp_tag_scene_field WHERE id IN (
'160004001003',
'160004001004',
'160004001005',
'160004001006'
);
# 登录密码近效期提醒天数参数 by lilong 241211
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100031, 'platform.user.pwd-expired.remind-period', '7', 'NUMBER', 'BUSINESS', '平台', '登录密码近效期提醒天数，单位为：天', 100310, '', 1, null, null, now(), now(), 0);
