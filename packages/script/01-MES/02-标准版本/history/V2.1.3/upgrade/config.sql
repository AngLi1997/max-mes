# 日期组件的默认样式 by lilong 240912
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100025, 'platform.sys.time.default-format', 'yMdHms', 'STRING', 'BUSINESS', '平台', '日期组件的默认样式', 100250, '', 1, '1', '1', now(), now(), 0);
UPDATE bmos_platform.bp_business_parameter SET description = '日期组件的格式' WHERE id = 100001;
# 系统服务激活监测配置参数 by lilong 240923
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100026, 'platform.sys.actived.service', '["PMP","MES","LIMS","WMS"]', 'STRING', 'BUSINESS', '平台', '系统服务激活监测配置', 100260, '', 1, '1', '1', now(), now(), 0);
# 电子签名组件日期格式 by lilong 241024
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100027, 'platform.sys.signature.time-format', 'yyyy-MM-dd HH:mm:ss', 'STRING', 'BUSINESS', '平台', '电子签名组件日期格式', 100270, '', 1, '1', '1', now(), now(), 0);
# 删除电子签名组件日期格式
delete from bmos_platform.bp_business_parameter where id = 100027;

