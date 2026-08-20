use bmos_quarantine_new;
truncate table bmos_quarantine_new.sys_param_config;

INSERT INTO bmos_quarantine_new.sys_param_config (id, param_name, code, value, value_type, description, del_flag, create_by, create_time, update_by, update_time) VALUES (1, '部门管理根节点名称', 'Department.Root_node', '远大蜀阳生命科学（成都）有限公司', 1, '部门管理根节点名称', 0, 1, now(), 1, now());
INSERT INTO bmos_quarantine_new.sys_param_config (id, param_name, code, value, value_type, description, del_flag, create_by, create_time, update_by, update_time) VALUES (2, '血浆无后续信息规则', 'Plasma.No_follow_up.Rule', '{"amount":"1","unit":"year","unqualifiedItem":"CQ-WHX"}', 4, '血浆无后续信息规则，配置“amount-数值”、“unit-单位”和“unqualifiedItem-血浆不合格原因代码”；unit支持年和日，配置为year代表自然年，day代表日', 0, 1, now(), 1, now());
INSERT INTO bmos_quarantine_new.sys_param_config (id, param_name, code, value, value_type, description, del_flag, create_by, create_time, update_by, update_time) VALUES (3, '血浆超有效期规则', 'Plasma.Expiration_date.Rule', '{"amount":"3","unit":"year","unqualifiedItem":"CQ-YXQ"}', 4, '血浆超有效期规则，配置“amount-数值”、“unit-单位”和“unqualifiedItem-血浆不合格原因代码”；unit支持年和日，配置为year代表自然年，day代表日', 0, 1, now(), 1, now());
INSERT INTO bmos_quarantine_new.sys_param_config (id, param_name, code, value, value_type, description, del_flag, create_by, create_time, update_by, update_time) VALUES (4, '血浆用途限制规则', 'Plasma.Restricted_use.Rule', '[{"amount":"1","unit":"year","plasma_use":"Factor_8"},{"amount":"1","unit":"year","plasma_use":"Factor_9"}]', 4, '血浆用途限制规则，配置“amount-数值”、“unit-单位”和“plasma_use-血浆用途数据键值”；unit支持年和日，配置为year代表自然年，day代表日；血浆用途来源为内置字典数据', 0, 1, now(), 1, now());
INSERT INTO bmos_quarantine_new.sys_param_config (id, param_name, code, value, value_type, description, del_flag, create_by, create_time, update_by, update_time) VALUES (5, '不合格血浆挑选默认库房', 'Default.Unqual_warehouse', '不合格血浆暂存间', 1, '不合格血浆挑选默认库房，配置库房编码', 0, 1, now(), 1, now());
INSERT INTO bmos_quarantine_new.sys_param_config (id, param_name, code, value, value_type, description, del_flag, create_by, create_time, update_by, update_time) VALUES (6, '血浆不合格需挑选样品', 'Unqual_plasma.Sample.Selection', '["HEMOLYSIS", "LIPEMIA"]', 4, '血浆不合格需挑选样品，配置血浆不合格代码', 0, 1, now(), 1, now());
INSERT INTO bmos_quarantine_new.sys_param_config (id, param_name, code, value, value_type, description, del_flag, create_by, create_time, update_by, update_time) VALUES (7, '样品请验默认送检科室', 'Default.Sample.Send.Dept', '血源管理部门', 1, '样品请验默认送检科室', 0, 1, now(), 1, now());
INSERT INTO bmos_quarantine_new.sys_param_config (id, param_name, code, value, value_type, description, del_flag, create_by, create_time, update_by, update_time) VALUES (8, '登录密码复杂度配置', 'User.Pwd-rule.Character', '{
  "lowerCase": false,
  "upperCase": false,
  "digit": false,
  "specialCharacters": [],
  "minLength": 6
}', 4, '用户登录密码复杂度校验配置：lowerCase：是否必须有小写字母，值：true/false；upperCase：是否必须有大写字母，值：true/false；digit：是否必须有数字，值：true/false；specialCharacters：是否必须有specialCharacters配置的字符，值：["!","."]；minLength：用户登录密码最小密码长度，限制6-24内整数', 0, 1, now(), 1, now());

