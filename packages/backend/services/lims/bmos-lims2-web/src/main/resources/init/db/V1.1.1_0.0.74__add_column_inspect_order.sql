-- lm_inspection_order 新增 scheme_source 字段（检验单来源：REGULAR=常规请验；STABILITY=稳定性考察时间点自动创建）
ALTER TABLE `lm_inspection_order` ADD COLUMN `scheme_source` VARCHAR(20) DEFAULT NULL COMMENT '检验单来源（REGULAR:常规请验 STABILITY:稳定性考察）' AFTER `retention_expiry_date`;
