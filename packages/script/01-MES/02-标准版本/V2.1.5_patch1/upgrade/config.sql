# 水印字体存放路径 by zhangruoyu 250221
INSERT INTO bmos_platform.bp_business_parameter (id, code, name, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100033, 'platform.sys.watermark-font-path', '水印字体存放路径', '/SimSun.ttf', 'STRING', 'BUSINESS', '生产', '数值组件趋势分析近n个批次', 100330, null, 1, '1', '1', now(), now(), 0);
# 系统AI服务地址 by lilong 250221
INSERT INTO bmos_platform.bp_business_parameter (id, code, name, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100034, 'platform.sys.AI-url', '系统AI服务地址', 'http://172.30.1.137/chatbot/7wQ8VGXuhydA4VEf', 'STRING', 'BUSINESS', '平台', '系统AI服务地址', 100340, '', 1, null, null, now(), now(), 0);
# 更新操作规程水印字体存放路径 by lilong 250226
UPDATE bmos_platform.bp_business_parameter SET name = '操作规程水印字体存放路径', value = '/usr/share/fonts/chinese/SimSun.ttf' WHERE id = 100033;

# 新增批记录照片归档正则表达式配置 by lilong 250228
INSERT INTO bmos_platform.bp_business_parameter (id, code, name, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120015, 'mes.record.archive-photos-regular', '批记录照片归档正则表达式配置', '{"take_photo":"\$\{<take_photo>(\[[\u4e00-\u9fa5a-zA-Z0-9#(),%.*（），、\\-\\s]+]){2}(\[\d*]){3}}","evidence_photo":"\$\{<evidence_photo>(\[[\u4e00-\u9fa5a-zA-Z0-9#(),%.*（），、\\-\\s]+]){2}(\[\d*]){3}}"}', 'JSON', 'BUSINESS', '生产', '批记录照片归档正则表达式配置:take_photo-拍照组件,evidence_photo-拍照取证', 120250, '', 1, '1', '1', now(), now(), 0);
UPDATE bmos_platform.bp_business_parameter SET value = '{"evidence_photo":"\\$\\{<evidence_photo>(\\[[\\u4e00-\\u9fa5a-zA-Z0-9#()（）%.*,，、\\-\\s]+]){2}(\\[\\d*]){3}}","take_photo":"\\$\\{<take_photo>(\\[[\\u4e00-\\u9fa5a-zA-Z0-9#()（）%.*,，、\\-\\s]+]){2}(\\[\\d*]){3}}"}' WHERE id = 120015;

# 更新操作规程水印字体存放路径参数描述信息 by lilong 250319
UPDATE bmos_platform.bp_business_parameter SET description = '操作规程水印字体存放路径' WHERE id = 100033;
# 批记录照片归档正则表达式配置 by dengke 250321
UPDATE `bmos_platform`.`bp_business_parameter` SET `value` = '{\"evidence_photo\":\"\\\\$\\\\{<evidence_photo>(\\\\[[\\\\u4e00-\\\\u9fa5a-zA-Z0-9#()（）%.*,，、\\\\-\\\\s]+]){2}(\\\\[\\\\d*]){3}}\",\"take_photo\":\"\\\\$\\\\{<take_photo>(\\\\[[\\\\u4e00-\\\\u9fa5a-zA-Z0-9#()（）%.*,，、\\\\-\\\\s]+]){2}(\\\\[\\\\d*]){3}}\"}' WHERE `id` = 120015;
