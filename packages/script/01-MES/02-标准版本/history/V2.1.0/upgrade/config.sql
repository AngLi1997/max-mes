# 增加生产计划类型标识参数 by lilong 240522
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120006, 'mes.ProductionPlanType', '{"PRODUCT":"A","EXPERIMENT":"B","VERIFY":"C"}', 'JSON', 'BUSINESS', '生产', '生产计划类型标识:PRODUCT-生产批次、EXPERIMENT-实验批次、VERIFY-验证批次', 120060, '', 1, '1', '1', now(),now(), 0);

# 增加批记录组件默认字号的参数 by lilong 240527
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120007, 'mes.record.font.size', '16', 'NUMBER', 'BUSINESS', '生产', '批记录组件默认字号,字号范围[5,72]', 120070, '', 1, '1', '1', now(), now(), 0);

# 增加批记录组件默认字体的参数 by lilong 240527
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120008, 'mes.record.default.font', 'SimSun', 'STRING', 'BUSINESS', '生产', '批记录组件默认字体，宋体：SimSun，黑体：SimHei，微软雅黑：YaHei，仿宋：FangSong', 120080, '', 1, '1', '1', now(), now(), 0);

# 增加称量需求提前规划时间的参数 by lilong 240612
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120009, 'mes.weigh.require.advance', '15', 'NUMBER', 'BUSINESS', '生产', '称量需求提前规划时间，单位：日，范围[0,999]', 120090, '', 1, '1', '1', now(), now(), 0);

# 物料临期提醒默认时间的参数 by lilong 240620
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (120010, 'mes.material.dying.period', '15', 'NUMBER', 'BUSINESS', '生产', '物料临期提醒默认时间，单位：日，范围[0,999]', 120100, '', 1, '1', '1', now(), now(), 0);

# 记录作业日期组件时间格式 by lilong 240625
UPDATE bmos_platform.bp_business_parameter SET value = 'yyyy-MM-dd HH:mm:ss' WHERE id = 100001;

# 生产计划命名改为生产指令单 by lilong 240710
UPDATE bmos_platform.bp_business_parameter SET description = '生产指令单作废标记' WHERE id = 120004;
UPDATE bmos_platform.bp_business_parameter SET description = '生产指令单类型标识:PRODUCT-生产批次、EXPERIMENT-实验批次、VERIFY-验证批次' WHERE id = 120006;

# 密码复杂度参数配置描述变更 by lilong 240719
UPDATE bmos_platform.bp_business_parameter SET description = '密码复杂度校验配置：lowerCase：是否必须有小写字母；upperCase：是否必须有大写字母；digit：是否必须有数字；specialCharacters：是否必须有specialCharacters配置的字符' WHERE id = 100011;
