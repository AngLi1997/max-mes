-- 请验单模板字段新增“字段来源”列
ALTER TABLE lm_document_config_field
    ADD COLUMN field_source VARCHAR(32) NULL COMMENT '字段来源';


