# 增加用户密码控制相关的配置 by yuxiaorong 240229
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (100011, 'platform.user.pwd-rule.character', 'U#L#N', 'STRING', 'BUSINESS', '平台', '用户密码字符集（多选，分隔符#），大写字符：U，小写字符：L，数字：N，其他字符：E（!,#,$,%,*,(,),[,],{,},,,.,;,:,-,_,?,=,+,-,|）', 100110, '', 1, 0);
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (100012, 'platform.user.pwd-rule.minLen', '6', 'STRING', 'BUSINESS', '平台', '用户密码最小密码长度，限制6-24内整数', 100120, '', 1, 0);
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (100013, 'platform.user.pwd-rule.tryNum', '5', 'STRING', 'BUSINESS', '平台', '用户密码尝试次数，限制0-24内整数', 100130, '', 1, 0);
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (100014, 'platform.user.pwd-rule.hisNum', '0', 'STRING', 'BUSINESS', '平台', '用户历史密码个数，限制0-6内整数', 100140, '', 1, 0);
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (100015, 'platform.user.pwd-rule.validity', '365', 'STRING', 'BUSINESS', '平台', '用户密码有效期，单位为：天', 100150, '', 1, 0);

# 增加外链的参数配置 by lilei 240312
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100016, 'platform.sys.outside_url', '{"999999999":"http://172.30.1.160"}', 'JSON', 'BUSINESS', '平台', '外链地址', 100160, '', 1, '1', '1', now(),now(), 0);

# 增加消息轮询时间的参数配置 by lilei 240312
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100017, 'platform.sys.app-msg-polling-time', '30', 'NUMBER', 'BUSINESS', '平台', '移动端消息轮询时间，单位：秒', 100160, '', 1, '1', '1', now(),now(), 0);
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100018, 'platform.sys.web-msg-polling-time', '30', 'NUMBER', 'BUSINESS', '平台', '网页端消息轮询时间，单位：秒', 100170, '', 1, '1', '1', now(),now(), 0);

# 增加项目配置的参数配置 by zhongjie 240312
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100019, 'platform.sys.project-config', '[{"label":"佰墨思","key":"bmos"},{"label":"白俄","key":"be"},{"label":"康盛科泰","key":"kskt"},{"label":"华兰生物","key":"hlsw"}]', 'JSON', 'BUSINESS', '平台', '系统项目配置', 100190, '', 1, '1', '1', now(),now(), 0);

# 更新记录配置页边距 by pangzhiwei 240313
UPDATE bmos_platform.bp_business_parameter SET value = '{"left":"10", "right":"10", "top":"10", "bottom":"10"}' WHERE id = 120001;

# 删除外链测试的参数配置 by lilong 240312
delete from bmos_platform.bp_business_parameter where id = 100016;

# 批签发单元格空值 by lilong 240301
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (120005, 'mes.release.over-level-data', '/', 'STRING', 'BUSINESS', '生产', '批签发单元格空值', 120050, '', 1, 0);
