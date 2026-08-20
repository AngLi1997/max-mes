ALTER TABLE lm_report_validation_task
    ADD COLUMN operate_version_ids VARCHAR(500) NULL COMMENT '选中的操作规程版本ID列表，分号分隔';
