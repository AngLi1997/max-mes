# 电子签名组件日期格式 by lilong 241024
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100027, 'platform.sys.signature.time-format', 'yyyy-MM-dd HH:mm:ss', 'STRING', 'BUSINESS', '平台', '电子签名组件日期格式', 100270, '', 1, '1', '1', now(), now(), 0);
# 时间差公式/时间组件格式 by lilong 241031
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100028, 'platform.sys.time-format.configuration', '{"day":"日", "hour":"时", "minute":"分", "second":"秒"}', 'JSON', 'BUSINESS', '平台', '时间差公式/时间组件格式', 100280, '', 1, '1', '1', now(), now(), 0);

# 参数配置增加参数名称字段 by lilong 241125
UPDATE bmos_platform.bp_business_parameter SET name = '日期组件默认格式', description = '日期组件的格式，配置工艺配置中日期组件的默认格式和批记录执行中日期组件的默认格式' WHERE id = 100001;
UPDATE bmos_platform.bp_business_parameter SET name = '移动端锁屏时间', description = '移动端锁屏时间，单位：分钟' WHERE id = 100004;
UPDATE bmos_platform.bp_business_parameter SET name = '网页端锁屏时间', description = '网页端锁屏时间，单位：分钟' WHERE id = 100005;
UPDATE bmos_platform.bp_business_parameter SET name = '网页端锁屏快捷键', description = '网页端锁屏快捷键，形式：["Ctrl","L"]' WHERE id = 100006;
UPDATE bmos_platform.bp_business_parameter SET name = '部门管理根节点名称', description = '部门管理根节点名称' WHERE id = 100007;
UPDATE bmos_platform.bp_business_parameter SET name = '系统语言', description = '系统语言' WHERE id = 100008;
UPDATE bmos_platform.bp_business_parameter SET name = '登录密码复杂度配置', description = '用户登录密码复杂度校验配置：lowerCase：是否必须有小写字母，值：true/false；upperCase：是否必须有大写字母，值：true/false；digit：是否必须有数字，值：true/false；specialCharacters：是否必须有specialCharacters配置的字符，值：["!","."]' WHERE id = 100011;
UPDATE bmos_platform.bp_business_parameter SET name = '登录密码最小长度', description = '用户登录密码最小密码长度，限制6-24内整数' WHERE id = 100012;
UPDATE bmos_platform.bp_business_parameter SET name = '登录密码尝试次数', description = '用户登录密码尝试次数，限制0-24内整数' WHERE id = 100013;
UPDATE bmos_platform.bp_business_parameter SET name = '登录密码历史密码个数', description = '用户历史登录密码个数，限制0-6内整数' WHERE id = 100014;
UPDATE bmos_platform.bp_business_parameter SET name = '用户登录密码有效期', description = '用户密码有效期，单位为：天' WHERE id = 100015;
UPDATE bmos_platform.bp_business_parameter SET name = '外链地址配置', description = '外链地址配置，示例：{"998":"http://172.30.1.30:7899", "999":"http://172.30.1.88:38180"}' WHERE id = 100016;
UPDATE bmos_platform.bp_business_parameter SET name = '移动端消息轮询时间', description = '移动端消息轮询时间，单位：秒' WHERE id = 100017;
UPDATE bmos_platform.bp_business_parameter SET name = '网页端消息轮询时间', description = '网页端消息轮询时间，单位：秒' WHERE id = 100018;
UPDATE bmos_platform.bp_business_parameter SET name = '系统项目配置', description = '系统项目配置' WHERE id = 100019;
UPDATE bmos_platform.bp_business_parameter SET name = '系统是否需要授权', description = '系统是否需要授权' WHERE id = 100020;
UPDATE bmos_platform.bp_business_parameter SET name = '平台数据采集地址配置', description = '平台数据采集地址配置' WHERE id = 100021;
UPDATE bmos_platform.bp_business_parameter SET name = '系统版本号', description = '系统版本号' WHERE id = 100022;
UPDATE bmos_platform.bp_business_parameter SET name = 'MQTT服务地址', description = 'MQTT服务地址' WHERE id = 100023;
UPDATE bmos_platform.bp_business_parameter SET name = '称量协议类型配置', description = '称量协议类型配置，示例：[{"name":"赛多利斯","type":"","clear":"1B540D0A","peel":"1B540D0A","read":"1B500D0A","value":"","sendType":"unit8Array"},{"name":"梅特勒","type":"ASCII","clear":"T","peel":"Z","read":"R","value":"","sendType":""}]' WHERE id = 100024;
UPDATE bmos_platform.bp_business_parameter SET name = '日期组件默认样式', description = '日期组件的默认样式，配置工艺配置中日期组件的默认样式' WHERE id = 100025;
UPDATE bmos_platform.bp_business_parameter SET name = '系统服务激活监测配置', description = '配置系统信息菜单中监测的服务，示例：["PMP","MES","LIMS","WMS"]' WHERE id = 100026;
UPDATE bmos_platform.bp_business_parameter SET name = '电子签名组件日期格式', description = '电子签名组件日期格式，配置批记录执行中电子签名组件的日期格式' WHERE id = 100027;
UPDATE bmos_platform.bp_business_parameter SET name = '时间组件格式', description = '时间差公式/时间组件格式，配置批记录执行中日期组件的展示格式' WHERE id = 100028;
UPDATE bmos_platform.bp_business_parameter SET name = '批记录页边距', description = '批记录页边距，单位：毫米' WHERE id = 120001;
UPDATE bmos_platform.bp_business_parameter SET name = '批记录作业空值符号', description = '批记录作业空值符号' WHERE id = 120002;
UPDATE bmos_platform.bp_business_parameter SET name = '批记录作业计算异常值', description = '批记录作业计算异常值' WHERE id = 120003;
UPDATE bmos_platform.bp_business_parameter SET name = '生产指令单作废标记', description = '生产指令单作废标记' WHERE id = 120004;
UPDATE bmos_platform.bp_business_parameter SET name = '批签发单元格空值符号', description = '批签发单元格空值符号' WHERE id = 120005;
UPDATE bmos_platform.bp_business_parameter SET name = '生产指令单类型标识', description = '生产指令单类型标识:PRODUCT-生产批次、EXPERIMENT-实验批次、VERIFY-验证批次' WHERE id = 120006;
UPDATE bmos_platform.bp_business_parameter SET name = '批记录组件默认字号', description = '批记录组件默认字号,字号范围[5,72]' WHERE id = 120007;
UPDATE bmos_platform.bp_business_parameter SET name = '批记录组件默认字体', description = '批记录组件默认字体，宋体：SimSun，黑体：SimHei，微软雅黑：YaHei，仿宋：FangSong' WHERE id = 120008;
UPDATE bmos_platform.bp_business_parameter SET name = '称量需求提前规划时间', description = '称量需求提前规划时间，单位：日，范围[0,999]' WHERE id = 120009;
UPDATE bmos_platform.bp_business_parameter SET name = '物料临期提醒默认时间', description = '物料临期提醒默认时间，单位：日，范围[0,999]' WHERE id = 120010;
UPDATE bmos_platform.bp_business_parameter SET name = '生产指令单是否需要确认', description = '生产指令单是否需要确认' WHERE id = 120012;
UPDATE bmos_platform.bp_business_parameter SET name = '数值组件趋势分析批次数', description = '数值组件趋势分析近n个批次' WHERE id = 120014;
# 更新参数值类型 by lilong 241125
UPDATE bmos_platform.bp_business_parameter SET value_type = 'NUMBER' WHERE id = 100012;
UPDATE bmos_platform.bp_business_parameter SET value_type = 'NUMBER' WHERE id = 100013;
UPDATE bmos_platform.bp_business_parameter SET value_type = 'NUMBER' WHERE id = 100014;
# 删除批签发宏配置参数 by lilong 241125
DELETE FROM bmos_platform.bp_business_parameter WHERE id = 120013;

# 更新时间差公式/时间组件格式参数配置 by lilong 241128
UPDATE bmos_platform.bp_business_parameter SET value = '[{\"label\":\"day\",\"value\":\"日\"},{\"label\":\"hour\",\"value\":\"时\"},{\"label\":\"minute\",\"value\":\"分钟\"},{\"label\":\"second\",\"value\":\"秒\"}]' WHERE id = 100028;

# 新增签名密码的复杂度校验配置和最小长度参数 by lilong 241126
INSERT INTO bmos_platform.bp_business_parameter (id, code, name, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100029, 'platform.user.sign-pwd-rule.character', '签名密码复杂度配置', '{
  "lowerCase": true,
  "upperCase": true,
  "digit": true,
  "specialCharacters": []
}', 'STRING', 'BUSINESS', '平台', '用户签名密码复杂度校验配置：lowerCase：是否必须有小写字母，值：true/false；upperCase：是否必须有大写字母，值：true/false；digit：是否必须有数字，值：true/false；specialCharacters：是否必须有specialCharacters配置的字符，值：["!","."]', 100290, '', 1, null, null, null, null, 0);
INSERT INTO bmos_platform.bp_business_parameter (id, code, name, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100030, 'platform.user.sign-pwd-rule.minLen', '签名密码最小长度', '6', 'NUMBER', 'BUSINESS', '平台', '用户签名密码最小密码长度，限制6-24内整数', 100300, '', 1, null, null, null, null, 0);
# 更新登录密码有效期参数的值类型  by lilong 241204
UPDATE bmos_platform.bp_business_parameter SET name = '登录密码有效期', value_type = 'NUMBER' WHERE id = 100015;
# 更新平台数据采集地址配置的参数值 by lilong 241204
UPDATE bmos_platform.bp_business_parameter SET value = '{"hub":{"mqttAddress":"172.30.1.167:38080","endpoint":"http://172.30.1.167:38080/","publicKey":"MIIBCgKCAQEA06Xg0IuLuqUbNW2lNoXtYAzb3dgb1wo2bKrWZxhhUSGxbS1NM7HQdLacquMtU/CsTuE80QW00SvzKVcb2y/6OrywSt/085G/McvFx26+JaHzw7GuBzhv41tqF/6fdJLiBmJ5olidUw8ESp1fizomQh6LZsihgjsWd1dnfljZ/snB1KZplo0LngRe2pgFjB7BeT7YjdLlVgpj8nRsTR+LuwK2W8mxE1WA/AymiQPyTlcA8CBbQqt/5O4zhoj0zLaFxvkchgz6qp3suykGc/8R0yEvtDWlm7j5CZzu9XSik5dh350oYbl1u+IIJePISVX/6Vn39JqRnITJT8HYQaTBrwIDAQAB","tenantId":"system","path":{"getData":"/api/iios/hub/tag/getValue","writeData":"/api/iios/hub/tag/write","getAccessToken":"/api/permission/auth/login","tagUpHis":"/api/iios/hub/storage/history/tagUpHis","tagId":"/api/iios/hub/tag/batchGetId","mqttCredential":"/api/iios/hub/mqtt/credential"},"username":"admin","password":"1Sysc0re!"},"supCon":{"mqttAddress":"172.30.1.160:8083"}}' WHERE id = 100021;
