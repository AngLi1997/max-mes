-- 检验单自定义字段表新增字典分类代码字段
ALTER TABLE lm_inspection_order_custom_field
    ADD COLUMN dict_code VARCHAR(128) NULL COMMENT '字段所属字典分类代码';
