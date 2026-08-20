-- 为样品表添加留样期限字段
ALTER TABLE lm_sample ADD COLUMN retention_expiry_date DATE COMMENT '留样期限（有效期至+1年）';
