# 生产指令单是否需要确认 by lilong 240807
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120012, 'mes.schedule.confirm.isRequired', 'true', 'STRING', 'BUSINESS', '平台', '生产指令单是否需要确认', 121210, '', 1, '1', '1', now(), now(), 0);
# 新增批签发excel宏的参数 by lilong 240829
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120013, 'mes.lot.release.VBA', '["宏1","宏2"]', 'STRING', 'BUSINESS', '生产', '批签发excel宏，输入宏命名，按顺序执行', 121220, '', 1, '1', '1', now(), now(), 0);
UPDATE bmos_platform.bp_business_parameter SET belong = '生产' WHERE id = 120012;
# 新增系统版本号的参数 by lilong 240909
REPLACE INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100022, 'platform.sys.version', 'V2.1.1', 'STRING', 'BUSINESS', '平台', '系统版本号', 100220, '', 1, '1', '1', now(), now(), 0);
# 新增MQTT服务地址的参数 by yigaohui 240810
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100023, 'platform.sys.MQTT.address', '', 'STRING', 'BUSINESS', '平台', 'MQTT服务地址', 100230, '', 1, '1', '1', now(), now(), 0);

# 新增称量协议的配置参数 by lilong 240919
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100024, 'platform.sys.weighing.protocol-type', '[
  {
    "name":"赛多利斯",
    "type":"Hex",
    // 清零
    "clear":"",
    // 去皮
    "peel":"",
    // 取值
    "read":"",
	// 正则
	"value":""
  }，
  {
    "name":"常熟双杰",
    "type":"ASCII",
    // 清零
    "clear":"",
    // 去皮
    "peel":"",
    // 取值
    "read":"",
	// 正则
	"value":""
  }，
  {
    "name":"梅特勒",
    "type":"ASCII",
    // 清零
    "clear":"T",
    // 去皮
    "peel":"Z",
    // 取值
    "read":"SI",
	// 正则
	"value":""
  }
]', 'JSON', 'BUSINESS', '平台', '称量协议类型配置', 100240, '', 1, '1', '1', now(), now(), 0);
# 数值组件趋势分析近n个批次 by zhangruoyu 240923
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120014, 'mes.field.trend.analysis', '50', 'NUMBER', 'BUSINESS', null, '数值组件趋势分析近n个批次', 121230, null, 1, '1', '1', now(), now(), 0);
# 更新数值组件趋势分析近n个批次参数所属应用 by lilong 240929
UPDATE bmos_platform.bp_business_parameter SET belong = '生产' WHERE id = 120014;

# 调整称量协议的配置参数 by lilong 241010
UPDATE bmos_platform.bp_business_parameter SET value = '[{"name":"赛多利斯","type":"Hex","clear":"","peel":"","read":"","value":""},{"name":"常熟双杰","type":"ASCII","clear":"","peel":"","read":"","value":""},{"name":"梅特勒","type":"ASCII","clear":"T","peel":"Z","read":"R","value":""}]' WHERE id = 100024;
# 调整称量协议的配置参数 by lilong 241011
UPDATE bmos_platform.bp_business_parameter SET value = '[{"name":"赛多利斯","type":"Hex","clear":"1B540D0A","peel":"1B540D0A","read":"1B500D0A","value":"","sendType":"unit8Array"},{"name":"常熟双杰","type":"ASCII","clear":"","peel":"","read":"","value":"","sendType":""},{"name":"梅特勒","type":"ASCII","clear":"T","peel":"Z","read":"R","value":"","sendType":""}]' WHERE id = 100024;

# 调整称量协议的配置参数 by lilong 241018
UPDATE bmos_platform.bp_business_parameter SET value = '[{"name":"赛多利斯","type":"","clear":"1B540D0A","peel":"1B540D0A","read":"1B500D0A","value":"","sendType":"unit8Array"},{"name":"常熟双杰","type":"ASCII","clear":"","peel":"","read":"","value":"","sendType":""},{"name":"梅特勒","type":"ASCII","clear":"T","peel":"Z","read":"R","value":"","sendType":""}]' WHERE id = 100024;
