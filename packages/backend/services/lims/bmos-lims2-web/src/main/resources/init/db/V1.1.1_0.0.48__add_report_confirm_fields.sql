-- 报告生成任务表新增确认相关字段
ALTER TABLE lm_report_generate_task
    ADD COLUMN confirm_by        VARCHAR(64)   NULL COMMENT '确认人ID',
    ADD COLUMN confirm_time      DATETIME      NULL COMMENT '确认时间（即报告生成时间）',
    ADD COLUMN inspection_conclusion VARCHAR(512) NULL COMMENT '检验结论';
