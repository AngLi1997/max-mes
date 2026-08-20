ALTER TABLE lm_report_generate_task
    ADD COLUMN inspect_basis VARCHAR(2000) NULL COMMENT '检验依据（操作规程快照：名称-编码-版本号，多个用";"分隔）' AFTER confirm_remark;
