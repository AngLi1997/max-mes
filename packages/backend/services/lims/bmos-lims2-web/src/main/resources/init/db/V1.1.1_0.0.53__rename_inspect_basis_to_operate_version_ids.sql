ALTER TABLE lm_report_generate_task
    CHANGE COLUMN inspect_basis operate_version_ids VARCHAR(2000) NULL COMMENT '选择的操作规程版本ID列表（多个用";"分隔），报告渲染时通过ID查询规程信息';
