-- 修改取样量字段类型为varchar
ALTER TABLE `lm_inspection_scheme_sampling`
    MODIFY COLUMN `sampling_amount` VARCHAR(50) NOT NULL COMMENT '取样量';
