-- 增量脚本：分析项数据点 TIME 类型支持与显示格式

-- 1) 为分析项数据点表增加 time_format 字段（若不存在）
ALTER TABLE lm_inspect_parameter_data_point
  ADD COLUMN time_format varchar(64) NULL COMMENT '时间类型显示格式' AFTER standard;

-- 2) 更新 result_type 字段注释，补充 TIME 类型
ALTER TABLE lm_inspect_parameter_data_point
  MODIFY COLUMN result_type varchar(20) NOT NULL COMMENT '数据点类型：NUMBER-数值, TEXT-文本, OPTION-选项, TIME-时间';


