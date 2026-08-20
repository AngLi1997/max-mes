# 密码锁定用户自动解锁时间 by lilong 241217
INSERT INTO bmos_platform.bp_business_parameter (id, code, name, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100032, 'platform.user.login-auto-unlock-time', '密码锁定用户自动解锁时间', '60', 'NUMBER', 'BUSINESS', '平台', '密码锁定用户自动解锁时间，单位：分钟', 100320, '', 1, null, null, now(), now(), 0);
# 调整密码锁定用户自动解锁时间参数描述 by lilong 241223
UPDATE bmos_platform.bp_business_parameter SET description = '密码锁定用户自动解锁时间，单位：分钟；值为-1，代表永久锁定' WHERE id = 100032;
# 登录密码近效期提醒天数参数 by lilong 241211
REPLACE INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100031, 'platform.user.pwd-expired.remind-period', '7', 'NUMBER', 'BUSINESS', '平台', '登录密码近效期提醒天数，单位为：天', 100310, '', 1, null, null, now(), now(), 0);
UPDATE bmos_platform.bp_business_parameter SET name = '登录密码近效期提醒天数' WHERE id = 100031;
