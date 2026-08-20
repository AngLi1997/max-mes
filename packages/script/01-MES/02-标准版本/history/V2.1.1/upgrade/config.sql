# 新增平台数据采集地址配置的参数 by renjinguang 240807
INSERT INTO `bmos_platform`.`bp_business_parameter` (`id`, `code`, `value`, `value_type`, `business_type`, `belong`, `description`, `sort`, `value_range`, `is_display`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (100021, 'platform.sys.acquisition-address', '[{\"key\":\"hub\",\"value\":{\"endpoint\":\"http://172.30.1.167:38080/\",\"publicKey\":\"MIIBCgKCAQEA06Xg0IuLuqUbNW2lNoXtYAzb3dgb1wo2bKrWZxhhUSGxbS1NM7HQdLacquMtU/CsTuE80QW00SvzKVcb2y/6OrywSt/085G/McvFx26+JaHzw7GuBzhv41tqF/6fdJLiBmJ5olidUw8ESp1fizomQh6LZsihgjsWd1dnfljZ/snB1KZplo0LngRe2pgFjB7BeT7YjdLlVgpj8nRsTR+LuwK2W8mxE1WA/AymiQPyTlcA8CBbQqt/5O4zhoj0zLaFxvkchgz6qp3suykGc/8R0yEvtDWlm7j5CZzu9XSik5dh350oYbl1u+IIJePISVX/6Vn39JqRnITJT8HYQaTBrwIDAQAB\",\"tenantId\":\"system\",\"path\":{\"getData\":\"/api/iios/hub/tag/getValue\",\"writeData\":\"/api/iios/hub/tag/write\",\"getAccessToken\":\"/api/core/authorization/auth/accessToken\",\"tagUpHis\":\"/api/iios/hub/storage/history/tagUpHis\",\"tagId\":\"/api/iios/hub/tag/batchGetId\"}}}]', 'JSON', 'BUSINESS', '平台', '平台数据采集地址配置', 100210, '', 1, '1', '1', now(), now(), 0);
# 新增外链地址的参数 by lilong 240807
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100016, 'platform.sys.outside_url', '{"190":"http://172.30.1.160:3000"}', 'JSON', 'BUSINESS', '平台', '外链地址', 100160, '', 1, '1', '1', now(),now(), 0);
# 日期组件的默认格式参数更新 by lilong 240815
UPDATE bmos_platform.bp_business_parameter SET value = '
{"yMdHms":"yyyy-MM-dd HH:mm:ss",
"yMdHm":"yyyy-MM-dd HH:mm",
"yMdH":"yyyy-MM-dd HH",
"yMd":"yyyy-MM-dd",
"yM":"yyyy-MM",
"y":"yyyy",
"MdHms":"MM-dd HH:mm:ss",
"MdHm":"MM-dd HH:mm",
"MdH":"MM-dd HH",
"Md":"MM-dd",
"M":"MM",
"dHms":"dd HH:mm:ss",
"dHm":"dd HH:mm",
"dH":"dd HH",
"d":"dd",
"Hms":"HH:mm:ss",
"Hm":"HH:mm",
"H":"HH",
"ms":"mm:ss",
"m":"mm",
"s":"ss"}', value_type = 'JSON', description = '日期组件的默认格式', update_time = now() WHERE id = 100001;

