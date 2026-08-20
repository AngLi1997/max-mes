-- 为样品表添加储存位置字段
ALTER TABLE lm_sample ADD COLUMN storage_location VARCHAR(200) COMMENT '储存位置';
