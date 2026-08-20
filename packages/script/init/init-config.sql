delete from bmos_platform.bp_business_parameter where is_display != 0;
select * from bmos_platform.bp_business_parameter order by sort;

# 志豪预留参数
# INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (1, 'application', null, null, null, null, null, null, '[{"label": "QMS","value": "QMS","URL": ""}, { "label": "LIMS","value": "LIMS","URL": "" },{"label": "MES","value": "MES","URL": ""}]', 0, null, null, null, null, 0);

# 平台脚本
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (100007, 'platform.sys.client-name', '佰墨思', 'STRING', 'BUSINESS', '平台', '部门管理根节点名称', 100010, '', 1, 0);
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (100001, 'platform.sys.time-format', 'yyyy-MM-dd hh:mm:ss', 'STRING', 'BUSINESS', '平台', '记录作业日期组件时间格式', 100020, '', 1, 0);
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (100004, 'platform.sys.app-lock-screen-time', '15', 'NUMBER', 'BUSINESS', '平台', '移动端锁屏时间，单位：分钟', 100030, '', 1, 0);
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (100005, 'platform.sys.web-lock-screen-time', '15', 'NUMBER', 'BUSINESS', '平台', '网页端锁屏时间，单位：分钟', 10BUSINESS', '平台', '用户密码规则', 100060, '0040, '', 1, 0);
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (100006, 'platform.sys.web-lock-screen-hotkey', '["Ctrl","L"]', 'JSON', 'BUSINESS', '平台', '网页端锁屏快捷键', 100050, '', 1, 0);
# INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (100002, 'platform.user.pwd-rule', '{"":""}', 'JSON', '', 1, 0);
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (100008, 'platform.sys.language', '{"中文":"zh_CN","英文":"en_US","俄文":"ru_RU"}', 'JSON', 'BUSINESS', '平台', '系统语言', 100080, '', 1, 0);

# 生产脚本
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (120001, 'mes.record.margin', '{"left":"10", "right":"10", "top":"10", "down":"10"}', 'JSON', 'BUSINESS', '生产', '批记录页边，单位：毫米', 120010, '', 1, 0);
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (120002, 'mes.record.empty-data', 'N/A', 'STRING', 'BUSINESS', '生产', '记录作业空值', 120020, '', 1, 0);
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (120003, 'mes.record.error-data', 'ERROR!', 'STRING', 'BUSINESS', '生产', '记录作业计算异常值', 120030, '', 1, 0);
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, is_deleted) VALUES (120004, 'mes.schedule.plan-invalid', '-废', 'STRING', 'BUSINESS', '生产', '生产计划作废标记', 120040, '', 1, 0);

# 当前时间刷新
update bmos_platform.bp_business_parameter set create_time = now(), update_time = now(), create_by = 1, update_by = 1 where 1 = 1;
