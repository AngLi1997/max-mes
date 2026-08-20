-- 增量脚本：TIME 类型支持与显示格式

-- 1) 为方案数据点表增加 time_format 字段（若不存在）
ALTER TABLE lm_inspection_scheme_data_point
  ADD COLUMN time_format varchar(64) NULL COMMENT '时间类型显示格式' AFTER options;

-- 2) 更新 point_type 字段注释，补充 TIME 类型
ALTER TABLE lm_inspection_scheme_data_point
  MODIFY COLUMN point_type varchar(20) NOT NULL COMMENT '数据点类型：NUMBER-数值, TEXT-文本, OPTION-选项, TIME-时间';

-- 3) 可选：更新录入记录表 point_type 字段注释，补充 TIME 类型（仅注释变更，不改结构）
ALTER TABLE lm_inspection_entry_record
  MODIFY COLUMN point_type varchar(20) NOT NULL COMMENT '数据点类型：NUMBER/TEXT/OPTION/TIME';


