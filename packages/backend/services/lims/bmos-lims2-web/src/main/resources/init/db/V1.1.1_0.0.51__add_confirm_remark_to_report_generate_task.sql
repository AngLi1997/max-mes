ALTER TABLE lm_report_generate_task
    ADD COLUMN confirm_remark VARCHAR(512) NULL COMMENT '确认备注' AFTER inspection_conclusion;
