# 登录密码近效期提醒天数参数 by lilong 241211
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100031, 'platform.user.pwd-expired.remind-period', '7', 'NUMBER', 'BUSINESS', '平台', '登录密码近效期提醒天数，单位为：天', 100310, '', 1, null, null, now(), now(), 0);
